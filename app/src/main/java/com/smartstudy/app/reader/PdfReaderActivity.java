package com.smartstudy.app.reader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.smartstudy.app.R;
import com.smartstudy.app.context.ContextRuleEngine;
import com.smartstudy.app.focus.StudyProfileEngine;
import com.smartstudy.app.location.LocationHelper;
import com.smartstudy.app.notes.HighlightOverlayView;
import com.smartstudy.app.notes.HighlightRepository;
import com.smartstudy.app.notes.NotesNavigator;
import com.smartstudy.app.sensors.AccelerometerMonitor;
import com.smartstudy.app.sensors.AmbientLightSensorManager;
import com.smartstudy.app.sensors.ProximitySensorManager;
import com.smartstudy.app.storage.BookmarkStore;
import com.smartstudy.app.storage.ReadingStateStore;
import com.github.chrisbanes.photoview.PhotoView;


import java.io.IOException;
import java.util.List;

public class PdfReaderActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> pdfPickerLauncher;

    private static final String TAG = "PdfReaderActivity";


    private boolean isPickingPdf = false;

    // UI
    private PhotoView pdfView;
    private HighlightOverlayView highlightOverlay;
    private Button btnPrev, btnNext, btnBookmark, btnNotes, btnClearLastHighlight;

    private boolean highlightEnabled = false;



    // PDF
    private PdfRenderer pdfRenderer;
    private PdfRenderer.Page currentPage;
    private ParcelFileDescriptor fileDescriptor;
    private int pageIndex = 0;
    private String currentDocumentUri;

    // Storage
    private ReadingStateStore stateStore;
    private BookmarkStore bookmarkStore;
    private HighlightRepository highlightRepo;

    // Sensors & Context
    private ProximitySensorManager proximitySensor;
    private AmbientLightSensorManager lightSensor;
    private AccelerometerMonitor accelerometer;
    private ContextRuleEngine contextEngine;

    // Location
    private LocationHelper locationHelper;
    private StudyProfileEngine studyProfileEngine;




    // =====================================================
    // Activity Lifecycle
    // =====================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_reader);

        // UI
        pdfView = findViewById(R.id.pdfView);
        pdfView.setZoomable(true);
        pdfView.setMaximumScale(5.0f);
        pdfView.setMediumScale(2.5f);
        pdfView.setMinimumScale(1.0f);

        highlightOverlay = findViewById(R.id.highlightOverlay);

        Button btnOpen = findViewById(R.id.btnOpen);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnBookmark = findViewById(R.id.btnBookmark);
        btnNotes = findViewById(R.id.btnNotes);
        btnClearLastHighlight = findViewById(R.id.btnClearLastHighlight);

        btnPrev.setEnabled(false);
        btnNext.setEnabled(false);
        btnBookmark.setEnabled(false);
        btnNotes.setEnabled(false);
        btnClearLastHighlight.setEnabled(false);

        // Storage
        stateStore = new ReadingStateStore(this);
        bookmarkStore = new BookmarkStore(this);
        highlightRepo = new HighlightRepository(this);

        // ================= Highlight Listener =================
        highlightOverlay.setOnNewHighlightListener(normalizedRect -> {

            // 🔒 Highlight only when mode is ON
            if (!highlightEnabled) return;

            // 🔒 Safety check
            if (pdfRenderer == null || currentDocumentUri == null) return;

            // ✅ Save highlight
            highlightRepo.addHighlight(
                    currentDocumentUri,
                    pageIndex,
                    normalizedRect,
                    Color.argb(90, 255, 235, 59)
            );

            Toast.makeText(this, "Highlighted", Toast.LENGTH_SHORT).show();


        });

        Button btnHighlight = findViewById(R.id.btnHighlight);

        btnHighlight.setOnClickListener(v -> {
            highlightEnabled = !highlightEnabled;

            highlightOverlay.setHighlightMode(highlightEnabled);

            // ✅ Update button text instead of Toast
            btnHighlight.setText(
                    highlightEnabled ? "Highlight ON" : "Highlight OFF"
            );
        });




        // ================= Buttons =================
        btnOpen.setOnClickListener(v -> openPdf());
        btnPrev.setOnClickListener(v -> showPage(pageIndex - 1));
        btnNext.setOnClickListener(v -> showPage(pageIndex + 1));

        btnBookmark.setOnClickListener(v -> {
            if (currentDocumentUri == null) {
                Toast.makeText(this, "Open a PDF first", Toast.LENGTH_SHORT).show();
                return;
            }

            bookmarkStore.addBookmark(currentDocumentUri, pageIndex);

            int displayPage = pageIndex + 1;

            Toast.makeText(
                    this,
                    "Bookmarked page " + displayPage,
                    Toast.LENGTH_SHORT
            ).show();


        });





        btnNotes.setOnClickListener(v ->
                NotesNavigator.openNotesForPage(this, pageIndex + 1)
        );

        btnClearLastHighlight.setOnClickListener(v -> {
            if (currentDocumentUri == null) return;
            highlightRepo.deleteLastHighlight(currentDocumentUri, pageIndex);
            loadHighlightsForPage(pageIndex);
        });

        // ================= Context Engine =================
        contextEngine = new ContextRuleEngine(new ContextRuleEngine.ActionListener() {
            @Override
            public void enableNightMode() {
                if (isPickingPdf) return;
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }

            @Override
            public void disableNightMode() {
                if (isPickingPdf) return;
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            @Override
            public void triggerFocusMode() {}

            @Override
            public void exitFocusMode() {}

            @Override
            public void showStretchReminder() {
                new AlertDialog.Builder(PdfReaderActivity.this)
                        .setTitle("Stretch Reminder")
                        .setMessage("Take a short break.")
                        .setPositiveButton("OK", null)
                        .show();
            }


        });


        // ================= Sensors =================
        proximitySensor = new ProximitySensorManager(this, new ProximitySensorManager.Listener() {
            @Override public void onNear() { contextEngine.onPhoneNearFace(); }
            @Override public void onFar() { contextEngine.onPhoneAwayFromFace(); }
        });

        lightSensor = new AmbientLightSensorManager(this, new AmbientLightSensorManager.Listener() {
            @Override public void onLowLight() { contextEngine.onLowLightDetected(); }
            @Override public void onNormalLight() { contextEngine.onNormalLightDetected(); }
        });

        accelerometer = new AccelerometerMonitor(this, new AccelerometerMonitor.Listener() {
            @Override public void onUserInactive() { contextEngine.onUserInactiveTooLong(); }
            @Override public void onMovementDetected() {}
        });

        // ================= Location =================
        locationHelper = new LocationHelper(this);
        studyProfileEngine = new StudyProfileEngine(
                new StudyProfileEngine.ProfileListener() {

                    @Override
                    public void onDeepFocus() {
                        Log.d(TAG, "Study Profile: Deep Focus");
                    }

                    @Override
                    public void onQuickRead() {
                        Log.d(TAG, "Study Profile: Quick Read");
                    }

                    @Override
                    public void onNormalMode() {
                        Log.d(TAG, "Study Profile: Normal Mode");
                    }
                }
        );

        // ================= ROTATION RESTORE =================
        if (savedInstanceState != null) {
            currentDocumentUri = savedInstanceState.getString("doc_uri");
            pageIndex = savedInstanceState.getInt("page_index", 0);

            if (currentDocumentUri != null) {
                restorePdf(Uri.parse(currentDocumentUri));
            }
        }

        pdfPickerLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            isPickingPdf = false;

                            if (result.getResultCode() != Activity.RESULT_OK) return;
                            Intent data = result.getData();
                            if (data == null) return;

                            Uri uri = data.getData();
                            if (uri == null) return;

                            String mimeType = getContentResolver().getType(uri);
                            Log.d("FILE_PICKER", "Selected MIME = " + mimeType);

                            if (mimeType == null) {
                                Toast.makeText(this, "Unknown file type", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            switch (mimeType) {

                                case "application/pdf":
                                    openDocumentFromUri(uri);
                                    break;

                                case "image/png":
                                case "image/jpeg":
                                    openImageFromUri(uri);
                                    break;

                                default:
                                    Toast.makeText(this, "Unsupported file type", Toast.LENGTH_SHORT).show();
                            }


                        }
                );



    }

    // =====================================================
    // PDF Handling
    // =====================================================
    private void openPdf() {
        isPickingPdf = true;

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");

        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{
                "application/pdf",
                "image/png",
                "image/jpeg"
        });
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION |
                        Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        );

        // 🔥 THIS LINE MUST BE HERE
        pdfPickerLauncher.launch(intent);
    }




    private void restorePdf(Uri uri) {
        try {
            closePdfResources();

            fileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
            if (fileDescriptor == null) return;

            pdfRenderer = new PdfRenderer(fileDescriptor);

            btnPrev.setEnabled(true);
            btnNext.setEnabled(true);
            btnBookmark.setEnabled(true);
            btnNotes.setEnabled(true);
            btnClearLastHighlight.setEnabled(true);

            // Restore exact page (used for rotation)
            showPage(pageIndex);

        } catch (Exception e) {
            Log.e(TAG, "Failed to restore PDF", e);
        }
    }




    private void showPage(int index) {
        if (pdfRenderer == null) return;
        if (index < 0 || index >= pdfRenderer.getPageCount()) return;

        if (currentPage != null) currentPage.close();
        currentPage = pdfRenderer.openPage(index);

        int targetW = pdfView.getWidth();
        int targetH = pdfView.getHeight();

        // fallback for first layout pass
        if (targetW <= 0 || targetH <= 0) {
            targetW = 1080;
            targetH = 1920;
        }

        Bitmap bitmap = Bitmap.createBitmap(
                targetW,
                targetH,
                Bitmap.Config.ARGB_8888
        );

        currentPage.render(
                bitmap,
                null,
                null,
                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
        );

        pdfView.setImageBitmap(bitmap);
        highlightOverlay.setImageMatrix(pdfView.getImageMatrix());

        pdfView.setOnMatrixChangeListener(rect -> {
            highlightOverlay.setImageMatrix(pdfView.getImageMatrix());
        });



        // ✅ UPDATE CURRENT PAGE
        pageIndex = index;

        // 🔥 THIS IS THE MISSING LINE (CRITICAL)
        if (currentDocumentUri != null) {
            stateStore.saveLastPage(currentDocumentUri, pageIndex);
        }

        loadHighlightsForPage(index);

        updateBookmarkUI();

    }

    private void updateBookmarkUI() {
        if (currentDocumentUri == null) return;

        int bookmarkedPage =
                bookmarkStore.getLastBookmarkedPage(currentDocumentUri);

        if (bookmarkedPage == pageIndex) {
            btnBookmark.setText("★ Page " + (pageIndex + 1));
        } else {
            btnBookmark.setText("☆ Bookmark");
        }
    }


    private void loadHighlightsForPage(int page) {
        if (currentDocumentUri == null) return;
        List<RectF> rects =
                highlightRepo.getHighlightsNormalized(currentDocumentUri, page);
        highlightOverlay.setHighlights(rects);
    }

    private void closePdfResources() {
        try {
            if (currentPage != null) currentPage.close();
            if (pdfRenderer != null) pdfRenderer.close();
            if (fileDescriptor != null) fileDescriptor.close();
        } catch (IOException ignored) {}
    }

    // =====================================================
    // Sensor Lifecycle
    // =====================================================
    @Override
    protected void onResume() {
        super.onResume();
        if (!isPickingPdf) {
            proximitySensor.start();
            lightSensor.start();
            accelerometer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        proximitySensor.stop();
        lightSensor.stop();
        accelerometer.stop();
    }

    @Override
    protected void onDestroy() {
        closePdfResources();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("doc_uri", currentDocumentUri);
        outState.putInt("page_index", pageIndex);
    }

    private void openDocumentFromUri(Uri uri) {
        try {
            // 🔥 FULL RESET before opening new PDF
            closePdfResources();

            if (highlightOverlay != null) {
                highlightOverlay.clear();
            }

            pdfView.setImageDrawable(null);

            currentDocumentUri = uri.toString();
            pageIndex = 0;

            // Persist permission
            getContentResolver().takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );

            fileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
            if (fileDescriptor == null) {
                Toast.makeText(this, "Failed to open file", Toast.LENGTH_SHORT).show();
                return;
            }

            pdfRenderer = new PdfRenderer(fileDescriptor);

            if (pdfRenderer.getPageCount() == 0) {
                Toast.makeText(this, "PDF has no pages", Toast.LENGTH_SHORT).show();
                return;
            }

            // Enable controls
            btnPrev.setEnabled(true);
            btnNext.setEnabled(true);
            btnBookmark.setEnabled(true);
            btnNotes.setEnabled(true);
            btnClearLastHighlight.setEnabled(true);

            // 🔥 Render after layout
            pdfView.post(() -> showPage(0));

        } catch (Exception e) {
            Log.e("PDF", "Failed to open PDF", e);
            Toast.makeText(this, "Failed to open PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImageFromUri(Uri uri) {

        try {
            closePdfResources();
            highlightOverlay.clear();

            currentDocumentUri = uri.toString();
            pageIndex = 0;

            pdfView.setImageURI(uri);

            btnPrev.setEnabled(false);
            btnNext.setEnabled(false);
            btnBookmark.setEnabled(false);
            btnNotes.setEnabled(true);
            btnClearLastHighlight.setEnabled(true);

        } catch (Exception e) {
            Log.e("IMAGE", "Failed to open image", e);
            Toast.makeText(this, "Failed to open image", Toast.LENGTH_SHORT).show();
        }
    }

    private void openOfficeExternally(Uri uri) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, getContentResolver().getType(uri));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No app found to open this file", Toast.LENGTH_SHORT).show();
        }
    }






}
