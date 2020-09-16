package com.rabbi.popmovies.data;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.rabbi.popmovies.api.ApiClient;
import com.rabbi.popmovies.models.MovieResponse;
import com.rabbi.popmovies.models.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.rabbi.popmovies.constant.Constant.APIKEY;
import static com.rabbi.popmovies.constant.Constant.LANGUAGE;
import static com.rabbi.popmovies.constant.Constant.PAGE_ONE;
import static com.rabbi.popmovies.constant.Constant.PREVIOUS_PAGE_KEY_ONE;
import static com.rabbi.popmovies.constant.Constant.PREVIOUS_PAGE_KEY_TOW;

public class MovieDataSource extends PageKeyedDataSource<Integer, Result> {
    private String sort_criteria;
    public MovieDataSource(String sort_criteria){
        this.sort_criteria= sort_criteria;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Result> callback)
    {
        ApiClient.getInstance().getApiService().getAllMovie(sort_criteria,APIKEY,LANGUAGE,PAGE_ONE)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                        if(response.isSuccessful())
                        {
                            callback.onResult(response.body().getResults(),
                                    PREVIOUS_PAGE_KEY_ONE,PREVIOUS_PAGE_KEY_TOW);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Result> callback)
    {


    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Result> callback)
    {
        final int currentPage= params.key;

        ApiClient.getInstance().getApiService().getAllMovie(sort_criteria,APIKEY,LANGUAGE,currentPage)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                        if(response.isSuccessful()){
                            int nextPage = currentPage+1;
                            callback.onResult(response.body().getResults(),nextPage);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });


    }
}
