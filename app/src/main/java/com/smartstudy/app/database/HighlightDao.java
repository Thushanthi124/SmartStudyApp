package com.smartstudy.app.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HighlightDao {

    @Insert
    long insert(HighlightEntity entity);

    @Query("SELECT * FROM highlights WHERE documentUri = :doc AND pageNumber = :page ORDER BY id ASC")
    List<HighlightEntity> getByDocumentAndPage(String doc, int page);

    @Query("DELETE FROM highlights WHERE id = (SELECT id FROM highlights WHERE documentUri = :doc AND pageNumber = :page ORDER BY id DESC LIMIT 1)")
    void deleteLast(String doc, int page);

    @Query("DELETE FROM highlights WHERE documentUri = :doc")
    void deleteAllForDocument(String doc);
}
