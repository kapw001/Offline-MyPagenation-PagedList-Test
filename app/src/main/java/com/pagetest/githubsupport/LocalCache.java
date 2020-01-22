package com.pagetest.githubsupport;

import android.arch.paging.DataSource;
import android.util.Log;

import com.pagetest.db.RepoDao;

import java.util.List;
import java.util.concurrent.Executor;

public class LocalCache {

    private RepoDao repoDao;

    private Executor executor;

    public LocalCache(RepoDao repoDao, Executor executor) {
        this.repoDao = repoDao;
        this.executor = executor;
    }

    void insert(final List<RepoModel> reposlist, final OnInsertFinished onInsertFinished) {

        executor.execute(new Runnable() {
            @Override
            public void run() {

                repoDao.insert(reposlist);
                if (onInsertFinished != null)
                    onInsertFinished.insertFinished();
                Log.d("GithubLocalCache", "inserting ${reposlist.size()} repos " + reposlist.size());

//                List<RepoModel> list = repoDao.reposByNameTest("%android%");
//
//                Log.e(TAG, "run: service " + list.size());
            }
        });

//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("GithubLocalCache", "inserting ${reposlist.size()} repos " + reposlist.size());

//                repoDao.insert(reposlist);
//                if (onInsertFinished != null)
//                    onInsertFinished.insertFinished();
//
//            }
//        });

    }

    /**
     * Request a LiveData<List<RepoModel>> from the Dao, based on a repo name.
     */
    DataSource.Factory<Integer, RepoModel> reposByName(String name) {
        // appending '%' so we can allow other characters to be before and after the query string
//        String query = "%${name.replace(' ', '%')}%";
        String query = "%" + name.replace(' ', '%') + "%";
        return repoDao.reposByName(query);
    }

    DataSource.Factory<Integer, RepoModel> getAllRepoTest() {

        return repoDao.getAllRepoTest();
    }
}
