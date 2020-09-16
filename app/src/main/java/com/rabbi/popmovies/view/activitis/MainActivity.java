package com.rabbi.popmovies.view.activitis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.rabbi.popmovies.R;
import com.rabbi.popmovies.adapters.MoviePageLisAdapter;
import com.rabbi.popmovies.interfaces.OnItemClick;
import com.rabbi.popmovies.models.Result;
import com.rabbi.popmovies.viewdodels.MainViewModel;
import com.rabbi.popmovies.viewdodels.MainViewModelFactory;

public class MainActivity extends AppCompatActivity implements OnItemClick {

    private RecyclerView recyclerView;
    private MoviePageLisAdapter adapter;
    private MainViewModel viewModel;
    private GridLayoutManager gridLayoutManager;
    private String sort_criteria = "popular";
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this,new MainViewModelFactory(sort_criteria))
                .get(MainViewModel.class);

        recyclerView = findViewById(R.id.popular_recyclerview);

        if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gridLayoutManager = new GridLayoutManager(this,2);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setHasFixedSize(true);
        }else {
            gridLayoutManager = new GridLayoutManager(this,3);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setHasFixedSize(true);
        }


        adapter = new MoviePageLisAdapter(this);
        recyclerView.setAdapter(adapter);

        viewModel.getListLiveData().observe(this, new Observer<PagedList<Result>>() {
            @Override
            public void onChanged(PagedList<Result> results) {

                if(results != null){
                    adapter.submitList(results);
                }
            }
        });
    }

    @Override
    public void OnMovieItemClick(Result movie)
    {
        String movieID = String.valueOf(movie.getId());
        intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("movie_id",movieID);
        startActivity(intent);
    }
}
