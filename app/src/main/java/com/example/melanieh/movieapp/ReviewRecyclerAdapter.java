package com.example.melanieh.movieapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/*** Created by melanieh on 1/19/17. */

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ReviewHolder> {

    private ArrayList<Review> reviews;

    public ReviewRecyclerAdapter(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_recyclerview_item_row, parent, false);
        return new ReviewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        Review itemReview = reviews.get(position);
        holder.bindReview(itemReview);

    }

    /*** Created by melanieh on 1/19/17. */

    public static class ReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Review review;
        TextView authorView;
        TextView textView;

        private static final String LOG_TAG = ReviewHolder.class.getSimpleName();

        public ReviewHolder(View itemView) {
            super(itemView);
            authorView = (TextView) itemView.findViewById(R.id.review_author);
            textView = (TextView) itemView.findViewById(R.id.review_text);
        }

        @Override
        public void onClick(View view) {
            // no click action necessary; just text
            Log.v(LOG_TAG, "ReviewHolder: onClick");
        }

        public void bindReview(Review review) {
            this.review = review;
            authorView.setText(review.getAuthor());
            textView.setText(review.getText());
        }
    }
}
