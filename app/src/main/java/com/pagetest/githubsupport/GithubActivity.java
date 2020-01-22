package com.pagetest.githubsupport;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pagetest.MainActivity;
import com.pagetest.R;
import com.pagetest.db.MyDatabase;
import com.pagetest.db.Note;
import com.pagetest.db.RepoDao;
import com.pagetest.vm.MyViewModelFactory;
import com.pagetest.vm.NoteViewModel;

import java.util.List;
import java.util.concurrent.Executor;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GithubActivity extends AppCompatActivity {

    private static final String TAG = "GithubActivity";
    private static final String LAST_SEARCH_QUERY = "last_search_query";
    private static final String DEFAULT_QUERY = "Kotlin";

    @BindView(R.id.search_repo)
    EditText searchRepo;
    @BindView(R.id.input_layout)
    TextInputLayout inputLayout;
    @BindView(R.id.list)
    RecyclerView recyclerview;
    @BindView(R.id.emptyList)
    TextView emptyList;


    ViewModelSearch viewModel;

    GithubRepoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github);
        ButterKnife.bind(this);

        GithubService githubService = RetofitGitHub.getRetofitGitHub();

        final RepoDao repoDao = MyDatabase.getAppDatabase(this).repoDao();

        LocalCache localCache = new LocalCache(repoDao, new MainThreadExecutor());


//        new MainThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//
//                List<RepoModel> list = repoDao.reposByNameTest("%android%");
//
//                Log.e(TAG, "run: service " + list.size());
//            }
//        });


        Repository repository = new Repository(githubService, localCache);

        adapter = new GithubRepoAdapter();

        viewModel = ViewModelProviders.of(this, new MyGitHubViewModelFactory(repository)).get(ViewModelSearch.class);


        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        recyclerview.setAdapter(adapter);

        initAdapter();

        String query = savedInstanceState == null ? DEFAULT_QUERY : savedInstanceState.getString(LAST_SEARCH_QUERY);
        viewModel.searchRepo(query);
        initSearch(query);


//        searchRepo.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                if (s.length() > 0) {
//
//
//                    viewModel.searchRepo(s.toString());
////                    updateRepoListFromInput();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

//        viewModel.repoResult.observe(this, new Observer<RepoResult>() {
//            @Override
//            public void onChanged(@Nullable RepoResult repoResult) {
//
//                Log.e(TAG, "onChanged: " + repoResult.getData().toString());
//
//            }
//        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LAST_SEARCH_QUERY, viewModel.lastQueryValue());
    }

    private void initAdapter() {

        viewModel.repos.observe(this, new Observer<PagedList<RepoModel>>() {
            @Override
            public void onChanged(@Nullable PagedList<RepoModel> repoModels) {

                Log.e(TAG, "onChanged: callBack " + repoModels.size());

                adapter.submitList(repoModels);
                showEmptyList(repoModels.size() == 0);
            }
        });

        viewModel.networkErrors.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                Toast.makeText(getApplicationContext(), "\uD83D\uDE28 Wooops " + s, Toast.LENGTH_LONG).show();
            }
        });

//        viewModel.repos.observe(this, Observer < PagedList < RepoModel >> {
//                Log.d("Activity", "list: ${it?.size}")
//                showEmptyList(it ?.size == 0)
//        adapter.submitList(it)
//        })
//        viewModel.networkErrors.observe(this, Observer < String > {
//                Toast.makeText(this, "\uD83D\uDE28 Wooops $it", Toast.LENGTH_LONG).show()
//        })
    }

    private void initSearch(String query) {
        searchRepo.setText(query);

        searchRepo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_GO) {
                    updateRepoListFromInput();
                    return true;
                } else {
                    return false;
                }


            }
        });


//        searchRepo.setOnEditorActionListener {
//            _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_GO) {
//                updateRepoListFromInput()
//                true
//            } else {
//                false
//            }
//        }

        searchRepo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    updateRepoListFromInput();
                    return false;
                } else {
                    return false;
                }
            }
        });
//        search_repo.setOnKeyListener {
//            _, keyCode, event ->
//            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//                updateRepoListFromInput()
//                true
//            } else {
//                false
//            }
//        }
    }

    private void updateRepoListFromInput() {


        String s = searchRepo.getText().toString().trim();

        if (s.length() > 0) {
            recyclerview.scrollToPosition(0);
            viewModel.searchRepo(s.toString());
            adapter.submitList(null);
        }


//        Toast.makeText(this, "" + s, Toast.LENGTH_SHORT).show();

//        searchRepo.getText().toString().trim().let {
//            if (it.isNotEmpty()) {
//                list.scrollToPosition(0)
//                viewModel.searchRepo(it.toString())
//                adapter.submitList(null)
//            }
//        }
    }

    private void showEmptyList(Boolean show) {
        if (show) {
            emptyList.setVisibility(View.VISIBLE);
            recyclerview.setVisibility(View.GONE);
        } else {
            emptyList.setVisibility(View.GONE);
            recyclerview.setVisibility(View.VISIBLE);
//            emptyList.visibility = View.GONE
//            list.visibility = View.VISIBLE
        }
    }

    public class GithubRepoAdapter
            extends PagedListAdapter<RepoModel, GithubRepoAdapter.GitHubViewHolder> {
        protected GithubRepoAdapter() {
            super(DIFF_CALLBACK);
        }

        @NonNull
        @Override
        public GithubRepoAdapter.GitHubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(parent.getContext());
            View view = li.inflate(R.layout.row_github_item, parent, false);
            return new GithubRepoAdapter.GitHubViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GithubRepoAdapter.GitHubViewHolder holder,
                                     int position) {
            RepoModel repoModel = getItem(position);
            if (repoModel != null) {
                holder.bindTo(repoModel);
            } else {
                // Null defines a placeholder item - PagedListAdapter automatically
                // invalidates this row when the actual object is loaded from the
                // database.
                holder.clear();
            }
        }

        protected class GitHubViewHolder extends RecyclerView.ViewHolder {

            private TextView repoNmae;

            public GitHubViewHolder(View itemView) {
                super(itemView);

                repoNmae = itemView.findViewById(R.id.repo_name);
            }

            public void bindTo(RepoModel repoModel) {

                repoNmae.setText(repoModel.getName() + "\n" + repoModel.getFullName() + "\n" + repoModel.getUrl());
            }

            public void clear() {
            }
        }


    }

    private static DiffUtil.ItemCallback<RepoModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<RepoModel>() {
                // Concert details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(RepoModel oldConcert, RepoModel newConcert) {
                    return oldConcert.getFullName().equals(newConcert.getFullName());
                }

                @Override
                public boolean areContentsTheSame(RepoModel oldConcert,
                                                  RepoModel newConcert) {
                    return oldConcert.equals(newConcert);
                }
            };


}
