package com.rabbi.popmovies.view.activitis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rabbi.popmovies.R;
import com.rabbi.popmovies.api.ApiClient;
import com.rabbi.popmovies.models.MovieDetailsResponse;
import com.rabbi.popmovies.models.Trailer;
import com.rabbi.popmovies.models.TrailersResponse;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.rabbi.popmovies.constant.Constant.APIKEY;
import static com.rabbi.popmovies.constant.Constant.IMAGE_SIZE;
import static com.rabbi.popmovies.constant.Constant.IMAGE_URL;
import static com.rabbi.popmovies.constant.Constant.LANGUAGE;

public class DetailActivity extends AppCompatActivity {

    private TextView textYear, txtMovieName,txtDuration,txt_rating,txtMovieDetails;
    private ImageView imgposter;

    private String movie_id;

    private RecyclerView recyclerView;
    private List<Trailer> trailerList;
    private TrailersAdapter trailersAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        //int all
        init();
        //get intent
        Intent intent = getIntent();
        if(intent.getExtras() != null){
            movie_id = intent.getStringExtra("movie_id");
            Log.e("movieid",movie_id);

            int pasID = Integer.parseInt(movie_id);
            ReciveMovieDetails(pasID);

            getTrailers(pasID);

        }


        trailerList = new ArrayList<>();
        trailersAdapter = new TrailersAdapter(this,trailerList);

        //set up recyclerview
        recyclerView = findViewById(R.id.recycler_trailers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    public void init(){
        textYear = findViewById(R.id.txt_year);
        txtMovieName = findViewById(R.id.txt_moviename);
        txt_rating = findViewById(R.id.txt_rating);
        txtMovieDetails = findViewById(R.id.txt_overview);
        txtDuration = findViewById(R.id.txt_duration);
        imgposter = findViewById(R.id.movie_poster);

    }

    private void ReciveMovieDetails(int pasID)
    {
        ApiClient.getInstance().getApiService().getMovieDetails(pasID,APIKEY,LANGUAGE)
                .enqueue(new Callback<MovieDetailsResponse>() {
                    @Override
                    public void onResponse(Call<MovieDetailsResponse> call, Response<MovieDetailsResponse> response) {

                        if(response.isSuccessful())
                        {

                            MovieDetailsResponse detailsResponse = response.body();


                            //date
                            String getDate = detailsResponse.getReleaseDate();

                            SimpleDateFormat df = new SimpleDateFormat("yyyy");
                            Date d = null;
                            try {
                                d = df.parse(getDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            //duration
                            String duration = String.valueOf(detailsResponse.getRuntime());

                            //rating
                            String rat = (detailsResponse.getVoteAverage()+"/10");

                            //poster
                            String thumbURL = IMAGE_URL + IMAGE_SIZE + detailsResponse.getPosterPath();

                            //set data
                            textYear.setText(df.format(d));
                            txtMovieName.setText(detailsResponse.getOriginalTitle());
                            txtDuration.setText(duration);
                            txt_rating.setText(rat);
                            txtMovieDetails.setText(detailsResponse.getOverview());

                            //posterset
                         Picasso.get().load(thumbURL).into(imgposter);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieDetailsResponse> call, Throwable t) {

                    }
                });

    }

    private void getTrailers(int pasID) {
        ApiClient.getInstance().getApiService().getMovieTrailers(pasID,APIKEY,LANGUAGE)
                .enqueue(new Callback<TrailersResponse>() {
                    @Override
                    public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {

                        List<Trailer> trailers = response.body().getResults();
                        recyclerView.setAdapter(new TrailersAdapter(getApplicationContext(),trailers));
                        recyclerView.smoothScrollToPosition(0);
                    }

                    @Override
                    public void onFailure(Call<TrailersResponse> call, Throwable t) {

                    }
                });
    }
    class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterVH> {

        private List<Trailer> trailerList;
        private Context context;

        public TrailersAdapter(Context context, List<Trailer> trailerList) {
            this.context = context;
            this.trailerList = trailerList;
        }

        @NonNull
        @Override
        public TrailersAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_trailers_item,parent,false);
            return new TrailersAdapterVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TrailersAdapterVH holder, int position)
        {
            final Trailer trailerResponse = trailerList.get(position);

            int add = position+1;
            holder.title.setText("Trailer "+add);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getApplicationContext(), TrailersShowActivity.class);
                    intent.putExtra("video_id", trailerResponse.getKey());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    Log.e("video_id",trailerResponse.getKey());
                }
            });



        }

        @Override
        public int getItemCount() {
            return trailerList.size();
        }

        public class TrailersAdapterVH extends RecyclerView.ViewHolder {

            private TextView title;

            public TrailersAdapterVH(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.trailersPosition);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
