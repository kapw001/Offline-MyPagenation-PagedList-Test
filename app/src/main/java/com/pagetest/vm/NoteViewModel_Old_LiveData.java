package com.pagetest.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.pagetest.db.Note;
import com.pagetest.db.NoteDao;

public class NoteViewModel_Old_LiveData extends ViewModel {

    private NoteDao noteDao;

    public final LiveData<PagedList<Note>> noteList;


    public NoteViewModel_Old_LiveData(NoteDao noteDao) {
        this.noteDao = noteDao;
        this.noteList = new LivePagedListBuilder<>(
                noteDao.getAllNotes(), 50).build();
    }
}
