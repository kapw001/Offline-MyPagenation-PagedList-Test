package com.pagetest.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.arch.paging.RxPagedListBuilder;

import com.pagetest.db.Note;
import com.pagetest.db.NoteDao;

import io.reactivex.Observable;

public class NoteViewModel extends ViewModel {

    private NoteDao noteDao;

    public final Observable<PagedList<Note>> noteList;


    public NoteViewModel(NoteDao noteDao) {
        this.noteDao = noteDao;
        this.noteList = new RxPagedListBuilder<>(
                noteDao.getAllNotes(), 50).buildObservable();
    }
}
