package com.smartstudy.app.database;

import androidx.room.*;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM notes WHERE pageNumber = :page ORDER BY updatedAt DESC")
    List<NoteEntity> getNotesByPage(int page);

    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    List<NoteEntity> getAllNotes();

    @Insert
    long insert(NoteEntity note);

    @Update
    void update(NoteEntity note);

    @Delete
    void delete(NoteEntity note);

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    NoteEntity getById(int id);
}
