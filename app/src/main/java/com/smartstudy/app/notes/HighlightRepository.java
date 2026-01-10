//package com.smartstudy.app.notes;
//
//import android.content.Context;
//import android.graphics.RectF;
//
//import com.smartstudy.app.database.AppDatabase;
//import com.smartstudy.app.database.HighlightDao;
//import com.smartstudy.app.database.HighlightEntity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class HighlightRepository {
//
//    private final HighlightDao dao;
//
//    public HighlightRepository(Context context) {
//        dao = AppDatabase.getInstance(context).highlightDao();
//    }
//
//    // =========================
//    // ADD HIGHLIGHT
//    // =========================
//    public void addHighlight(
//            String documentUri,
//            int page,
//            RectF normalized,
//            int color
//    ) {
//        HighlightEntity h = new HighlightEntity();
//        h.documentUri = documentUri;
//        h.pageNumber = page;
//        h.left = normalized.left;
//        h.top = normalized.top;
//        h.right = normalized.right;
//        h.bottom = normalized.bottom;
//        h.color = color;
//        h.createdAt = System.currentTimeMillis();
//
//        dao.insert(h);
//    }
//
//    // =========================
//    // GET HIGHLIGHTS (PDF + PAGE)
//    // =========================
//    public List<RectF> getHighlightsNormalized(
//            String documentUri,
//            int page
//    ) {
//        List<HighlightEntity> list =
//                dao.getHighlightsByDocumentAndPage(documentUri, page);
//
//        List<RectF> out = new ArrayList<>();
//        for (HighlightEntity e : list) {
//            out.add(new RectF(e.left, e.top, e.right, e.bottom));
//        }
//        return out;
//    }
//
//    // =========================
//    // DELETE LAST HIGHLIGHT (UNDO)
//    // =========================
//    public void deleteLastHighlight(
//            String documentUri,
//            int page
//    ) {
//        HighlightEntity last =
//                dao.getLastHighlight(documentUri, page);
//
//        if (last != null) {
//            dao.delete(last);
//        }
//    }
//
//    // =========================
//    // CLEAR ALL HIGHLIGHTS ON PAGE
//    // =========================
//    public void clearHighlightsForPage(
//            String documentUri,
//            int page
//    ) {
//        dao.deleteByDocumentAndPage(documentUri, page);
//    }
//}

package com.smartstudy.app.notes;

import android.content.Context;
import android.graphics.RectF;

import com.smartstudy.app.database.AppDatabase;
import com.smartstudy.app.database.HighlightDao;
import com.smartstudy.app.database.HighlightEntity;

import java.util.ArrayList;
import java.util.List;

public class HighlightRepository {

    private final HighlightDao dao;

    public HighlightRepository(Context context) {
        dao = AppDatabase.getInstance(context).highlightDao();
    }

    public void addHighlight(String documentUri, int page, RectF normalized, int color) {
        if (documentUri == null) return;

        HighlightEntity h = new HighlightEntity();
        h.documentUri = documentUri;
        h.pageNumber = page;
        h.left = normalized.left;
        h.top = normalized.top;
        h.right = normalized.right;
        h.bottom = normalized.bottom;
        h.color = color;
        h.createdAt = System.currentTimeMillis();
        dao.insert(h);
    }

    public List<RectF> getHighlightsNormalized(String documentUri, int page) {
        if (documentUri == null) return new ArrayList<>();

        List<HighlightEntity> list = dao.getByDocumentAndPage(documentUri, page);
        List<RectF> out = new ArrayList<>();
        for (HighlightEntity e : list) {
            out.add(new RectF(e.left, e.top, e.right, e.bottom));
        }
        return out;
    }

    public void deleteLastHighlight(String documentUri, int page) {
        if (documentUri == null) return;
        dao.deleteLast(documentUri, page);
    }

    public void deleteAllForDocument(String documentUri) {
        if (documentUri == null) return;
        dao.deleteAllForDocument(documentUri);
    }
}

