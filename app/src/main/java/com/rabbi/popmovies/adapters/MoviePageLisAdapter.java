package com.rabbi.popmovies.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rabbi.popmovies.R;
import com.rabbi.popmovies.interfaces.OnItemClick;
import com.rabbi.popmovies.models.Result;
import com.squareup.picasso.Picasso;

import static com.rabbi.popmovies.constant.Constant.IMAGE_SIZE;
import static com.rabbi.popmovies.constant.Constant.IMAGE_URL;

public class MoviePageLisAdapter extends PagedListAdapter<Result, MoviePageLisAdapter.MViewMOdel> {
    private OnItemClick itemClick;

    public MoviePageLisAdapter(OnItemClick itemClick){
        super(diffCallback);
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public MViewMOdel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_popular_item,parent,false);
        return new MViewMOdel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MViewMOdel holder, int position) {
        holder.bind(getItem(position));

    }

    public static DiffUtil.ItemCallback<Result> diffCallback = new
            DiffUtil.ItemCallback<Result>() {
                @Override
                public boolean areItemsTheSame(@NonNull Result oldItem, @NonNull Result newItem) {
                    return oldItem.getId()==newItem.getId();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull Result oldItem, @NonNull Result newItem) {
                    return oldItem.equals(newItem);
                }
            };


    public class MViewMOdel extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView thumb;
        public MViewMOdel(@NonNull View itemView) {
            super(itemView);

            thumb = itemView.findViewById(R.id.thumb);
            itemView.setOnClickListener(this);
        }
        public void bind(Result item){

            if(item != null){
                String thumbURL = IMAGE_URL + IMAGE_SIZE + item.getBackdropPath();
                Picasso.get().load(thumbURL).into(thumb);
            }
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            Result movie = getItem(pos);
            itemClick.OnMovieItemClick(movie);
        }
    }
}
