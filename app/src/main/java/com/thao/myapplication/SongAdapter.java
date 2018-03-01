package com.thao.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import model.SongItem;

/**
 * Created by AnhThao on 02/02/2018.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder>{

    ArrayList<SongItem> mSongs;
    Context mContext;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public SongAdapter(ArrayList<SongItem> songs, Context context) {
        mSongs = songs;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
         Glide.with(mContext).load(mSongs.get(position).getImgRes()).into(holder.imgSong);
         holder.singer.setText(mSongs.get(position).getSinger());
         holder.songTitle.setText(mSongs.get(position).getTitleSong() );
         holder.songTitle.setSelected(true);
         holder.songTitle.requestFocus();
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgSong;
        TextView songTitle, singer;

        public ViewHolder(View itemView) {
            super(itemView);
            imgSong = (ImageView) itemView.findViewById(R.id.image_list_song);
            songTitle = (TextView) itemView.findViewById(R.id.title_song);
            songTitle.setSelected(true);
            songTitle.requestFocus();
            singer = (TextView) itemView.findViewById(R.id.singer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
