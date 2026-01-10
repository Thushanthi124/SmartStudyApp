package com.smartstudy.app.notes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smartstudy.app.R;
import com.smartstudy.app.database.AppDatabase;
import com.smartstudy.app.database.NoteDao;
import com.smartstudy.app.database.NoteEntity;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity {

    private int pageNumber;
    private NoteDao noteDao;

    private NotesAdapter adapter;
    private final List<NoteEntity> notes = new ArrayList<>();

    private ActivityResultLauncher<Intent> addEditLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        pageNumber = getIntent().getIntExtra(NotesNavigator.EXTRA_PAGE, 1);

        TextView title = findViewById(R.id.txtNotesTitle);
        title.setText("Notes - Page " + pageNumber);

        noteDao = AppDatabase.getInstance(this).noteDao();

        RecyclerView recycler = findViewById(R.id.recyclerNotes);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NotesAdapter(notes, new NotesAdapter.NoteActions() {
            @Override
            public void onEdit(NoteEntity note) {
                Intent i = new Intent(NotesActivity.this, AddEditNoteActivity.class);
                i.putExtra(AddEditNoteActivity.EXTRA_NOTE_ID, note.id);
                i.putExtra(AddEditNoteActivity.EXTRA_PAGE, pageNumber);
                addEditLauncher.launch(i);
            }

            @Override
            public void onDelete(NoteEntity note) {
                noteDao.delete(note);
                loadNotes();
            }
        });

        recycler.setAdapter(adapter);

        addEditLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> loadNotes()
        );

        FloatingActionButton fab = findViewById(R.id.fabAddNote);
        fab.setOnClickListener(v -> {
            Intent i = new Intent(this, AddEditNoteActivity.class);
            i.putExtra(AddEditNoteActivity.EXTRA_PAGE, pageNumber);
            addEditLauncher.launch(i);
        });

        loadNotes();
    }

    private void loadNotes() {
        notes.clear();
        notes.addAll(noteDao.getNotesByPage(pageNumber));
        adapter.notifyDataSetChanged();
    }
}
