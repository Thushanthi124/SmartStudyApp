package com.smartstudy.app.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * ReadingStateStore
 *
 * Stores last-read page PER DOCUMENT
 */
public class ReadingStateStore {

    private static final String PREF_NAME = "smartstudy_reading_state";
    private static final String KEY_PREFIX = "last_page_";

    private final SharedPreferences prefs;

    public ReadingStateStore(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Save last page for a document
    public void saveLastPage(String documentUri, int pageIndex) {
        prefs.edit()
                .putInt(KEY_PREFIX + documentUri, pageIndex)
                .apply();
    }

    // Get last page for a document
    public int getLastPage(String documentUri) {
        return prefs.getInt(KEY_PREFIX + documentUri, 0);
    }

    // Optional: clear reading state for a document
    public void clear(String documentUri) {
        prefs.edit()
                .remove(KEY_PREFIX + documentUri)
                .apply();
    }
}
