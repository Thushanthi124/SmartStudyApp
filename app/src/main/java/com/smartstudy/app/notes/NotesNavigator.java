package com.smartstudy.app.notes;

import android.content.Context;
import android.content.Intent;

public class NotesNavigator {
    public static final String EXTRA_PAGE = "extra_page";

    public static void openNotesForPage(Context context, int pageNumber) {
        Intent i = new Intent(context, NotesActivity.class);
        i.putExtra(EXTRA_PAGE, pageNumber);
        context.startActivity(i);
    }
}
