package com.pagetest.db;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(Note t);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Note> list);

    @Update
    void update(Note t);

    @Delete
    void delete(Note t);

    @Query("SELECT * FROM note")
    List<Note> getAllData();

    // The Integer type parameter tells Room to use a
    // PositionalDataSource object.
    @Query("SELECT * FROM note")
    DataSource.Factory<Integer, Note> getAllNotes();

}
