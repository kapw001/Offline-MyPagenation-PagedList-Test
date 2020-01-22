package com.pagetest.githubsupport;

import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;

public class RepoResult {

    private LiveData<PagedList<RepoModel>> data;
    private LiveData<String> networkErrors;

    public RepoResult(LiveData<PagedList<RepoModel>> data, LiveData<String> networkErrors) {
        this.data = data;
        this.networkErrors = networkErrors;
    }

    public LiveData<PagedList<RepoModel>> getData() {
        return data;
    }

    public void setData(LiveData<PagedList<RepoModel>> data) {
        this.data = data;
    }

    public LiveData<String> getNetworkErrors() {
        return networkErrors;
    }

    public void setNetworkErrors(LiveData<String> networkErrors) {
        this.networkErrors = networkErrors;
    }
}
