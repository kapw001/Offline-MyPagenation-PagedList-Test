package com.pagetest.githubsupport;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GithubService {

    String BASE_URL = "https://api.github.com/";

    @GET("search/repositories?sort=stars")
    Call<RepoResponse> searchRepos(
            @Query("q") String query,
            @Query("page") int page,
            @Query("per_page") int itemsPerPage
    );


}
