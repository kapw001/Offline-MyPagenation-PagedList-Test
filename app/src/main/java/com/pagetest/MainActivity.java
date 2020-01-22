package com.pagetest;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pagetest.db.MyDatabase;
import com.pagetest.db.Note;
import com.pagetest.db.NoteDao;
import com.pagetest.vm.MyViewModelFactory;
import com.pagetest.vm.NoteViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.edittext)
    EditText edittext;
    @BindView(R.id.add)
    Button add;

    private final NoteAdapter adapter = new NoteAdapter();

    private NoteViewModel viewModel;
    private CompositeDisposable disposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        final NoteDao noteDao = MyDatabase.getAppDatabase(this).noteDao();


//        NoteViewModel viewModel =
//                ViewModelProviders.of(this).get(NoteViewModel.class);
        viewModel = ViewModelProviders.of(this, new MyViewModelFactory(noteDao)).get(NoteViewModel.class);


        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        recyclerview.setAdapter(adapter);

//        viewModel.noteList.observe(this, new Observer<PagedList<Note>>() {
//            @Override
//            public void onChanged(@Nullable PagedList<Note> notes) {
//
//                adapter.submitList(notes);
//
//            }
//        });


//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//
//
//                for (int i = 1; i <= 1000; i++) {
//                    Note note = new Note();
//                    note.setNotes("Note " + i);
//
//                    noteDao.insert(note);
//                }
//
//
//            }
//        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String s = edittext.getText().toString();

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        Note note = new Note();
                        note.setNotes(s);

                        noteDao.insert(note);

                        recyclerview.post(new Runnable() {
                            @Override
                            public void run() {
                                recyclerview.smoothScrollToPosition(adapter.getItemCount());

                            }
                        });


                    }
                });


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        disposable.add(viewModel.noteList
                .subscribe(new Consumer<PagedList<Note>>() {
                               @Override
                               public void accept(PagedList<Note> notes) throws Exception {
                                   adapter.submitList(notes);
                               }
                           }
                ));
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposable.clear();
    }


    public class NoteAdapter
            extends PagedListAdapter<Note, NoteAdapter.NoteViewHolder> {
        protected NoteAdapter() {
            super(DIFF_CALLBACK);
        }

        @NonNull
        @Override
        public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(parent.getContext());
            View view = li.inflate(R.layout.row_item, parent, false);
            return new NoteViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NoteViewHolder holder,
                                     int position) {
            Note note = getItem(position);
            if (note != null) {
                holder.bindTo(note);
            } else {
                // Null defines a placeholder item - PagedListAdapter automatically
                // invalidates this row when the actual object is loaded from the
                // database.
                holder.clear();
            }
        }

        protected class NoteViewHolder extends RecyclerView.ViewHolder {

            private TextView txt_note;

            public NoteViewHolder(View itemView) {
                super(itemView);

                txt_note = itemView.findViewById(R.id.note);
            }

            public void bindTo(Note note) {

                txt_note.setText(note.getNotes());
            }

            public void clear() {
            }
        }


    }

    private static DiffUtil.ItemCallback<Note> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Note>() {
                // Concert details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(Note oldConcert, Note newConcert) {
                    return oldConcert.getId().equals(newConcert.getId());
                }

                @Override
                public boolean areContentsTheSame(Note oldConcert,
                                                  Note newConcert) {
                    return oldConcert.equals(newConcert);
                }
            };

}
