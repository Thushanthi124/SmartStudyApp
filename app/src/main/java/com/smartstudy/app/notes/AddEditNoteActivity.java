package com.smartstudy.app.notes;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.smartstudy.app.R;
import com.smartstudy.app.database.AppDatabase;
import com.smartstudy.app.database.NoteDao;
import com.smartstudy.app.database.NoteEntity;

public class AddEditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE_ID = "extra_note_id";
    public static final String EXTRA_PAGE = "extra_page";

    private int noteId;
    private int pageNumber;

    private NoteDao noteDao;

    private EditText edtTitle, edtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        noteDao = AppDatabase.getInstance(this).noteDao();

        noteId = getIntent().getIntExtra(EXTRA_NOTE_ID, -1);
        pageNumber = getIntent().getIntExtra(EXTRA_PAGE, 0);

        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);

        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);

        if (noteId != -1) {
            NoteEntity existing = noteDao.getById(noteId);
            if (existing != null) {
                edtTitle.setText(existing.title);
                edtContent.setText(existing.content);
            }
        }

        btnSave.setOnClickListener(v -> saveNote());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveNote() {
        long now = System.currentTimeMillis();

        if (noteId == -1) {
            NoteEntity note = new NoteEntity();
            note.pageNumber = pageNumber;
            note.title = edtTitle.getText().toString().trim();
            note.content = edtContent.getText().toString().trim();
            note.createdAt = now;
            note.updatedAt = now;

            noteDao.insert(note);
        } else {
            NoteEntity note = noteDao.getById(noteId);
            if (note != null) {
                note.title = edtTitle.getText().toString().trim();
                note.content = edtContent.getText().toString().trim();
                note.updatedAt = now;
                noteDao.update(note);
            }
        }

        finish();
    }
}
