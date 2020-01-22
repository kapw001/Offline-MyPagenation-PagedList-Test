package com.pagetest.vm;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.pagetest.db.NoteDao;

public class MyViewModelFactory implements ViewModelProvider.Factory {
    private NoteDao noteDao;


    public MyViewModelFactory(NoteDao noteDao) {
        this.noteDao = noteDao;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new NoteViewModel(noteDao);
    }
}