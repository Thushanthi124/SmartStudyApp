package com.smartstudy.app.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "highlights")
public class HighlightEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String documentUri;

    public int pageNumber;

    // Normalized coordinates (0..1) relative to PDF view area
    public float left;
    public float top;
    public float right;
    public float bottom;

    public int color;      // ARGB color int
    public long createdAt;
}
