package com.example.andysoft.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andysoft.R;
import com.example.andysoft.db.Book;
import com.example.andysoft.images.ImageAdapter;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesHolder> {

    private List<Book> filteredmovieList;
    Context context;

    public MoviesAdapter(List<Book> movieList, Context context) {
        this.context = context;
        this.filteredmovieList = movieList;
    }

    @Override
    public MoviesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rv_layout_movies, parent, false);
        MoviesHolder mh = new MoviesHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(MoviesHolder holder, int position) {

        holder.txt_book_name.setText(filteredmovieList.get(position).getBook_name());
        holder.txt_author_name.setText(filteredmovieList.get(position).getAuthor_name());
        holder.txt_date.setText(filteredmovieList.get(position).getDoi());
        holder.txt_price.setText(filteredmovieList.get(position).getPrice());

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        holder.slider.setLayoutManager(layoutManager);

        ListAdapter adapter = new ListAdapter(filteredmovieList.get(position).getImagesList(), context);
        holder.slider.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return filteredmovieList.size();
    }


    public class MoviesHolder extends RecyclerView.ViewHolder {

        TextView txt_book_name, txt_author_name, txt_price, txt_date;
        RecyclerView slider;

        public MoviesHolder(View v) {
            super(v);
            txt_book_name = (TextView) v.findViewById(R.id.txt_book_name);
            txt_author_name = (TextView) v.findViewById(R.id.txt_author_name);
            txt_price = (TextView) v.findViewById(R.id.txt_price);
            txt_date = v.findViewById(R.id.txt_date);
            slider = v.findViewById(R.id.slider);
        }
    }
}