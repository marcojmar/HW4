package com.codepath.instagram.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by mmar on 10/26/15.
 */
public class InstagramPostsAdapter extends RecyclerView.Adapter<InstagramPostsAdapter.PostItemViewHolder> {
    private List<InstagramPost> posts;
    private Context context;

    public InstagramPostsAdapter(List<InstagramPost> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public PostItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // inflate the custom layout
        View contactView = inflater.inflate(R.layout.activity_layout_item_post, parent, false);

        //Return a new holder instance
        final PostItemViewHolder viewHolder = new PostItemViewHolder(contactView);

        // To update the Todo task
        viewHolder.btnAllComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CommentsActivity.class);
                intent.putExtra("MediaId", viewHolder.tvMediaId.getText().toString());
                v.getContext().startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PostItemViewHolder holder, int i) {
        final InstagramPost post = posts.get(i);


        holder.tvDate.setText(DateUtils.getRelativeTimeSpanString(post.createdTime * 1000).toString());

        Uri uri = Uri.parse(post.user.profilePictureUrl);
        holder.sdvImage.setImageURI(uri);

        uri = Uri.parse(post.image.imageUrl);
        holder.sdvBigImage.setImageURI(uri);

        holder.tvLikes.setText(post.likesCount + " likes");

        ForegroundColorSpan blueForgroundColor = new ForegroundColorSpan(context.getResources().getColor(android.R.color.holo_blue_dark));
        SpannableStringBuilder ssb = new SpannableStringBuilder(post.user.userName);
//        ssb.setSpan(new StyleSpan(Typeface.BOLD), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new TypefaceSpan("bold"), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ssb.setSpan(blueForgroundColor,
                0,
                ssb.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        holder.tvUserName.setText(ssb, TextView.BufferType.EDITABLE);
        holder.tvLikes.setText(post.likesCount + " likes");

        if (post.caption != null) {
            String[] captions = post.caption.split(" #");
            ssb.append(" " + captions[0]);
            holder.tvComment.setText(ssb, TextView.BufferType.EDITABLE);

            if (captions.length > 1) {
                holder.tvHashtag.setText("#" + captions[1]);
            }
            else {
                holder.tvHashtag.setVisibility(View.GONE);
            }
        }
        else {
            holder.tvComment.setText(ssb, TextView.BufferType.EDITABLE);
            holder.tvHashtag.setVisibility(View.GONE);
        }

        // populate the comments into llComments
        holder.llComments.removeAllViews();

        if (post.comments.size() > 1) {
            for (int ind=0; ind<2; ind++) {
                View tempView = LayoutInflater.from(context).inflate(R.layout.activity_layout_item_text_comment, holder.llComments, false);
                TextView tvOneComemnt = (TextView) tempView.findViewById(R.id.tvOneComment);
                tvOneComemnt.setText(post.comments.get(ind).user.userName + " " + post.comments.get(ind).text);
                holder.llComments.addView(tempView);
            }
        }
        else {
            holder.llComments.setVisibility(View.INVISIBLE);
        }

        if (post.comments.size() >= 2) {
           holder.btnAllComments.setText("View all " + post.commentsCount + " comments");
        }
        else {
            holder.btnAllComments.setVisibility(View.GONE);
        }

        // defined as View.INVISIBLE
        holder.tvMediaId.setText(post.mediaId.toString());
    }

    public static class PostItemViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView sdvImage;
        TextView tvUserName;
        TextView tvDate;
        SimpleDraweeView sdvBigImage;
        TextView tvLikes;
        TextView tvComment;
        TextView tvHashtag;
        LinearLayout llComments;
        Button btnAllComments;
        TextView tvMediaId;

        public PostItemViewHolder(View v) {
            super(v);
            tvUserName = (TextView) v.findViewById(R.id.tvUserName);
            tvDate = (TextView) v.findViewById(R.id.tvDate);
            sdvImage = (SimpleDraweeView) v.findViewById(R.id.sdvImage);
            sdvBigImage = (SimpleDraweeView) v.findViewById(R.id.sdvBigImage);
            tvLikes = (TextView) v.findViewById(R.id.tvLikes);
            //tvUserName2 = (TextView) v.findViewById(R.id.tvUserName2);
            tvComment = (TextView) v.findViewById(R.id.tvComment);
            tvHashtag = (TextView) v.findViewById(R.id.tvHashtag);
            btnAllComments = (Button) v.findViewById(R.id.btnAllComments);
            tvMediaId = (TextView) v.findViewById(R.id.tvMediaId);

            llComments = (LinearLayout) v.findViewById(R.id.llComments);


        }
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();;
        this.notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<InstagramPost> list) {
        posts.addAll(list);
        this.notifyDataSetChanged();
    }
}
