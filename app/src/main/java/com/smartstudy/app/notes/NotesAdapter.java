package com.smartstudy.app.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartstudy.app.R;
import com.smartstudy.app.database.NoteEntity;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.VH> {

    public interface NoteActions {
        void onEdit(NoteEntity note);
        void onDelete(NoteEntity note);
    }

    private final List<NoteEntity> notes;
    private final NoteActions actions;

    public NotesAdapter(List<NoteEntity> notes, NoteActions actions) {
        this.notes = notes;
        this.actions = actions;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        NoteEntity n = notes.get(position);
        h.txtTitle.setText(n.title == null || n.title.isEmpty() ? "(No title)" : n.title);
        h.txtContent.setText(n.content == null ? "" : n.content);

        h.itemView.setOnClickListener(v -> actions.onEdit(n));
        h.itemView.setOnLongClickListener(v -> {
            actions.onDelete(n);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView txtTitle, txtContent;

        VH(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtNoteTitle);
            txtContent = itemView.findViewById(R.id.txtNoteContent);
        }
    }
}
