package com.example.melanieh.movieapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by melanieh on 1/19/17.
 */

public class VideoRecyclerAdapter extends RecyclerView.Adapter<VideoRecyclerAdapter.VideoHolder> {

    private ArrayList<Video> videos;

    public VideoRecyclerAdapter(ArrayList<Video> videos) {
        this.videos = videos;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_recyclerview_item_row, parent, false);
        return new VideoHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        Video itemVideo = videos.get(position);
        holder.bindVideo(itemVideo);
    }

    /*** Created by melanieh on 1/19/17. */

    public static class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Video video;
        ImageButton playButton;
        TextView videoTitle;

        private static final String LOG_TAG = VideoHolder.class.getSimpleName();
        public VideoHolder(View itemView) {
            super(itemView);
            playButton = (ImageButton) itemView.findViewById(R.id.play_button);
            videoTitle = (TextView) itemView.findViewById(R.id.video_title);
            playButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.v(LOG_TAG, "VideoHolder: onClick");
            Context context = view.getContext();
            // click is on play button, open browser or app
            Intent playVideo = new Intent(Intent.ACTION_VIEW);
            Uri webpage = Uri.parse(context.getUrlString());
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            context.startActivity(playVideo);
        }

        public void bindVideo(Video video) {
            this.video = video;
            videoTitle.setText(video.getVideoTitle());
        }
    }
}
