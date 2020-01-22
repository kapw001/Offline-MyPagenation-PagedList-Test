package com.pagetest.githubsupport;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class MyGitHubViewModelFactory implements ViewModelProvider.Factory{

    private Repository repository;

    public MyGitHubViewModelFactory(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ViewModelSearch(repository);
    }
}
