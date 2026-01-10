//package com.smartstudy.app.storage;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * BookmarkStore
// *
// * Purpose:
// * - Store multiple bookmarked pages per document
// * - Uses SharedPreferences (lightweight persistence)
// *
// * NOTE:
// * - Resume reading is handled by ReadingStateStore
// * - This class is ONLY for bookmarks
// */
//public class BookmarkStore {
//
//    private static final String PREF_NAME = "smartstudy_bookmarks";
//    private static final String KEY_PREFIX = "bookmarks_"; // per document
//
//    private final SharedPreferences prefs;
//
//    public BookmarkStore(Context context) {
//        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//    }
//
//    // ============================
//    // Add bookmark for a document
//    // ============================
//    public void addBookmark(String documentUri, int pageIndex) {
//        Set<String> bookmarks = getBookmarks(documentUri);
//        bookmarks.add(String.valueOf(pageIndex));
//
//        prefs.edit()
//                .putStringSet(KEY_PREFIX + documentUri, bookmarks)
//                .apply();
//    }
//
//    // ============================
//    // Get all bookmarks for a document
//    // ============================
//    public Set<String> getBookmarks(String documentUri) {
//        return new HashSet<>(
//                prefs.getStringSet(
//                        KEY_PREFIX + documentUri,
//                        new HashSet<>()
//                )
//        );
//    }
//
//    // ============================
//    // Check if page is bookmarked
//    // ============================
//    public boolean isBookmarked(String documentUri, int pageIndex) {
//        return getBookmarks(documentUri)
//                .contains(String.valueOf(pageIndex));
//    }
//
//    // ============================
//    // Remove a specific bookmark
//    // ============================
//    public void removeBookmark(String documentUri, int pageIndex) {
//        Set<String> bookmarks = getBookmarks(documentUri);
//        bookmarks.remove(String.valueOf(pageIndex));
//
//        prefs.edit()
//                .putStringSet(KEY_PREFIX + documentUri, bookmarks)
//                .apply();
//    }
//
//    // ============================
//    // Clear all bookmarks for a document
//    // ============================
//    public void clearBookmarks(String documentUri) {
//        prefs.edit()
//                .remove(KEY_PREFIX + documentUri)
//                .apply();
//    }
//    public int getLastBookmarkedPage(String documentUri) {
//        Set<String> bookmarks = getBookmarks(documentUri);
//        int last = -1;
//
//        for (String p : bookmarks) {
//            try {
//                int page = Integer.parseInt(p);
//                if (page > last) {
//                    last = page;
//                }
//            } catch (NumberFormatException ignored) {}
//        }
//        return last;
//    }
//
//}

package com.smartstudy.app.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class BookmarkStore {

    private static final String PREF_NAME = "bookmarks";
    private static final String KEY_PREFIX = "bookmark_";

    private final SharedPreferences prefs;

    public BookmarkStore(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Save bookmark for a specific document
    public void addBookmark(String documentUri, int pageIndex) {
        prefs.edit()
                .putInt(KEY_PREFIX + documentUri, pageIndex)
                .apply();
    }

    // Get bookmarked page for a document
    public int getLastBookmarkedPage(String documentUri) {
        return prefs.getInt(KEY_PREFIX + documentUri, -1);
    }

    public boolean hasBookmark(String documentUri) {
        return prefs.contains(KEY_PREFIX + documentUri);
    }

    public void clearBookmark(String documentUri) {
        prefs.edit()
                .remove(KEY_PREFIX + documentUri)
                .apply();
    }
}

