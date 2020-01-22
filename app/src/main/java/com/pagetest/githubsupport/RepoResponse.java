package com.pagetest.githubsupport;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RepoResponse {

    @SerializedName("total_count")
    private int total = 0;
    @SerializedName("items")
    private List<RepoModel> items = new ArrayList<>();
    private int nextPage = 0;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RepoModel> getItems() {
        return items;
    }

    public void setItems(List<RepoModel> items) {
        this.items = items;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }
}
