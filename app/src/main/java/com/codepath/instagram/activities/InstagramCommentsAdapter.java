package com.codepath.instagram.activities;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramComment;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by mmar on 10/29/15.
 */
public class InstagramCommentsAdapter extends RecyclerView.Adapter<InstagramCommentsAdapter.CommentsViewHolder> {
    private ArrayList<InstagramComment> comments;
    private Context context;

    public InstagramCommentsAdapter(ArrayList<InstagramComment> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View commentsView = inflater.inflate(R.layout.activity_layout_item_comment, parent, false);

        CommentsViewHolder commentsViewHolder = new CommentsViewHolder(commentsView);

        return commentsViewHolder;
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, int i) {
        InstagramComment comment = comments.get(i);

        //post.mediald;

        Uri uri = Uri.parse(comment.user.profilePictureUrl);
        holder.sdvUserPic.setImageURI(uri);

        holder.tvUserId.setText(comment.user.userName + " " + comment.text);
        holder.tvTime.setText(DateUtils.getRelativeTimeSpanString(comment.createdTime * 1000).toString());
    }

    public static class CommentsViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView sdvUserPic;
        TextView tvUserId;
        TextView tvTime;

        public CommentsViewHolder(View v) {
            super(v);

            sdvUserPic = (SimpleDraweeView) v.findViewById(R.id.sdvUserPic);
            tvUserId = (TextView) v.findViewById(R.id.tvUserId);
            tvTime = (TextView) v.findViewById(R.id.tvTime);
        }
    }
}
