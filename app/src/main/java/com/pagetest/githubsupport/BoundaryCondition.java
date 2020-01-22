package com.pagetest.githubsupport;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoundaryCondition extends PagedList.BoundaryCallback<RepoModel> {


    private static final String TAG = "GithubService";
    private static final String IN_QUALIFIER = "in:name,description";

    private static final int NETWORK_PAGE_SIZE = 50;

    String query;

    GithubService githubService;

    LocalCache cache;

    int lastRequestedPage = 1;

    MutableLiveData<String> _networkErrors = new MutableLiveData<String>();

    boolean isRequestInProgress = false;


    public BoundaryCondition(String query, GithubService githubService, LocalCache cache) {
        this.query = query;
        this.githubService = githubService;
        this.cache = cache;


    }

    @Override
    public void onZeroItemsLoaded() {
//        super.onZeroItemsLoaded();

        requestAndSaveData(query);

    }

    @Override
    public void onItemAtEndLoaded(@NonNull RepoModel itemAtEnd) {
//        super.onItemAtEndLoaded(itemAtEnd);
        requestAndSaveData(query);

    }


    private void requestAndSaveData(String query) {


        if (isRequestInProgress) return;

        Log.e(TAG, "requestAndSaveData: called  " + lastRequestedPage);

        isRequestInProgress = true;
        searchRepos(githubService, query, lastRequestedPage, NETWORK_PAGE_SIZE, new OnItemListener() {
            @Override
            public void setRepos(List<RepoModel> repos) {

//                cache.insert(repos, null);
//                lastRequestedPage++;
//                isRequestInProgress = false;
                cache.insert(repos, new OnInsertFinished() {
                    @Override
                    public void insertFinished() {
                        lastRequestedPage++;
                        isRequestInProgress = false;
                    }
                });

            }

            @Override
            public void onError(String error) {
                _networkErrors.setValue(error);
                isRequestInProgress = false;

            }
        });

//        githubService.searchRepos(query, lastRequestedPage, NETWORK_PAGE_SIZE).enqueue(new Callback<RepoResponse>() {
//            @Override
//            public void onResponse(Call<RepoResponse> call, Response<RepoResponse> response) {
//
//                if (response.isSuccessful()) {
//                    lastRequestedPage++;
//                    cache.insert(response.body().getItems());
//                } else {
//                    isRequestInProgress = false;
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RepoResponse> call, Throwable t) {
//                isRequestInProgress = false;
//            }
//        });

//        searchRepos(service, query, lastRequestedPage, NETWORK_PAGE_SIZE, {repos ->
//                cache.insert(repos){
//                lastRequestedPage++
//                isRequestInProgress = false
//        }
//        }, {error ->
//                isRequestInProgress = false
//        })
    }

    interface OnItemListener {

        void setRepos(List<RepoModel> repos);

        void onError(String error);
    }


    private void searchRepos(GithubService service, String query, int page, int itemsPerPage, final OnItemListener onItemListener) {

        Log.e(TAG, "query: $query, page: $page, itemsPerPage: $itemsPerPage");

        String apiQuery = query + IN_QUALIFIER;


        service.searchRepos(apiQuery, page, itemsPerPage).enqueue(new Callback<RepoResponse>() {
            @Override
            public void onResponse(Call<RepoResponse> call, Response<RepoResponse> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<RepoModel> repos = response.body().getItems() != null ? response.body().getItems() : new ArrayList<RepoModel>();

                        onItemListener.setRepos(repos);

                    }
                } else {

                    try {
                        if (response.errorBody() != null) {
                            onItemListener.onError(response.errorBody().string());
                        } else {
                            onItemListener.onError("Unknown error");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RepoResponse> call, Throwable t) {

                onItemListener.onError(t.getMessage());
            }
        });

    }


    //    public String getQuery() {
//        return query;
//    }
//
//    public void setQuery(String query) {
//        this.query = query;
//    }
//
//    public GithubService getGithubService() {
//        return githubService;
//    }
//
//    public void setGithubService(GithubService githubService) {
//        this.githubService = githubService;
//    }
//
//    public LocalCache getCache() {
//        return cache;
//    }
//
//    public void setCache(LocalCache cache) {
//        this.cache = cache;
//    }
}
