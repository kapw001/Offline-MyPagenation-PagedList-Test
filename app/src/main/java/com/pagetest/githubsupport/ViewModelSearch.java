package com.pagetest.githubsupport;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.util.Log;

public class ViewModelSearch extends ViewModel {

    private static final String TAG = "ViewModelSearch";
    private MutableLiveData<String> queryLiveData = new MutableLiveData<>();

    private Repository repository;

    public LiveData<RepoResult> repoResult = Transformations.map(queryLiveData, new Function<String, RepoResult>() {
        @Override
        public RepoResult apply(String input) {

//            Log.e(TAG, "apply: " + input);

            return repository.search(input);
        }
    });

    public ViewModelSearch(final Repository repository) {
        this.repository = repository;

//        queryLiveData.setValue("Android");

//        repoResult = Transformations.map(queryLiveData, new Function<String, RepoResult>() {
//            @Override
//            public RepoResult apply(String input) {
//
//                Log.e(TAG, "apply: " + input);
//
//                return repository.search(input);
//            }
//        });

    }


    public LiveData<PagedList<RepoModel>> repos = Transformations.switchMap(repoResult, new Function<RepoResult, LiveData<PagedList<RepoModel>>>() {
        @Override
        public LiveData<PagedList<RepoModel>> apply(RepoResult input) {
            return input.getData();
        }
    });


    public LiveData<String> networkErrors = Transformations.switchMap(repoResult, new Function<RepoResult, LiveData<String>>() {
        @Override
        public LiveData<String> apply(RepoResult input) {
            return input.getNetworkErrors();
        }
    });

    /**
     * Search a repository based on a query string.
     */
    void searchRepo(String queryString) {
        queryLiveData.postValue(queryString);

//        Log.e(TAG, "searchRepo: " + queryString);

    }


    /**
     * Get the last query value.
     */
    String lastQueryValue() {
        return queryLiveData.getValue();
    }
}
