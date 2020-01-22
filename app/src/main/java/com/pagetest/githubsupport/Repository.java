package com.pagetest.githubsupport;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.util.Log;

public class Repository {

    private static final String TAG = "Repository";
    private static final int DATABASE_PAGE_SIZE = 20;

    private GithubService githubService;

    private LocalCache localCache;


    public Repository(GithubService githubService, LocalCache localCache) {
        this.githubService = githubService;
        this.localCache = localCache;
    }

    /**
     * Search repositories whose names match the query.
     */
    public RepoResult search(String query) {
        Log.e(TAG, "New query: $query " + query);

        // Get data source factory from the local cache
//        DataSource.Factory<Integer, RepoModel> dataSourceFactory = localCache.getAllRepoTest();
        DataSource.Factory<Integer, RepoModel> dataSourceFactory = localCache.reposByName(query);

        // Construct the boundary callback
        BoundaryCondition boundaryCallback = new BoundaryCondition(query, githubService, localCache);
        LiveData<String> networkErrors = boundaryCallback._networkErrors;

//        PagedList.Config pagedListConfig =
//                new PagedList.Config.Builder()
//                        .setEnablePlaceholders(true)
//                        .setInitialLoadSizeHint(10)
//                        .setPageSize(DATABASE_PAGE_SIZE).build();
        // Get the paged list
        LiveData<PagedList<RepoModel>> data = new LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE).setBoundaryCallback(boundaryCallback).build();
//        LiveData<PagedList<RepoModel>> data = new LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE).setBoundaryCallback(boundaryCallback).build();
//        LivePagedListBuilder<Integer, RepoModel> data = new LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE);
//        data.setBoundaryCallback(boundaryCallback);
//        data.setFetchExecutor(new MainThreadExecutor());
        // Get the network errors exposed by the boundary callback
        return new RepoResult(data, networkErrors);
    }
}
