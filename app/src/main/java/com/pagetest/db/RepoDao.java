package com.pagetest.db;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pagetest.githubsupport.RepoModel;

import java.util.List;


@Dao
public interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<RepoModel> posts);

    // Do a similar query as the search API:
    // Look for repos that contain the query string in the name or in the description
    // and order those results descending, by the number of stars and then by name
//    @Query("SELECT * FROM repos WHERE (name LIKE :queryString) OR (description LIKE :queryString) ORDER BY stars DESC, name ASC")
//    DataSource.Factory<Integer, RepoModel> reposByName(String queryString);

    @Query("SELECT * FROM repomodel WHERE (name LIKE :queryString) OR (description LIKE :queryString) ORDER BY stars DESC, name ASC")
    DataSource.Factory<Integer, RepoModel> reposByName(String queryString);

    @Query("SELECT * FROM repomodel")
    List<RepoModel> getAllRepo();

    @Query("SELECT * FROM repomodel")
    DataSource.Factory<Integer, RepoModel> getAllRepoTest();

    @Query("SELECT * FROM RepoModel WHERE (name LIKE :queryString) OR (description LIKE " +
            ":queryString) ORDER BY stars DESC, name ASC")
    List<RepoModel> reposByNameTest(String queryString);


}
