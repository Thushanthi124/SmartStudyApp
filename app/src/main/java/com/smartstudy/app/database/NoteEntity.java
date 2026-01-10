package com.smartstudy.app.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class NoteEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int pageNumber;       // NOTE-to-page mapping
    public String title;
    public String content;

    public long createdAt;
    public long updatedAt;
}
