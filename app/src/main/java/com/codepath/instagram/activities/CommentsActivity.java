package com.codepath.instagram.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.instagram.R;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramClient;
import com.codepath.instagram.models.InstagramComment;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CommentsActivity extends AppCompatActivity {
    RecyclerView rvComments;
    ArrayList<InstagramComment> allComments = new ArrayList<InstagramComment>();
    InstagramCommentsAdapter commandAdapter;
    String mediaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_comments);

        mediaId = getIntent().getStringExtra("MediaId");

        rvComments = (RecyclerView) findViewById(R.id.rvComments);

        fetchPost();
        commandAdapter = new InstagramCommentsAdapter(allComments, this);

        rvComments.setAdapter(commandAdapter);

        rvComments.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comments, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchPost() {

        try {
            InstagramClient.getCommentsFeed(mediaId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    allComments.addAll(Utils.decodeCommentsFromJsonResponse(response));
                    commandAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    try {
                        Log.e("getPoularFeed", "OnFailure", t);
                    } catch (Throwable e) {
                        Log.e("Error", e.getMessage());
                    }
                }
            });
        }
        catch (Throwable e) {
            e.getStackTrace();
        }
    }
}
