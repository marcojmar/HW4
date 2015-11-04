package com.codepath.instagram.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramClient;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.persistence.InstagramClientDatabase;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    ArrayList<InstagramPost> allPosts = new ArrayList<InstagramPost>() ;
    InstagramPostsAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private Context context;

    // DB related
    Integer app_version;
    InstagramClientDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_home);
        context = getApplicationContext();

        app_version = getResources().getInteger(R.integer.app_version);
        db = InstagramClientDatabase.getInstance(this);
        db.onUpgrade(db.getWritableDatabase(), 1, app_version);
        //db.emptyAllTables();

        final RecyclerView rvPosts = (RecyclerView) findViewById(R.id.rvPosts);

        adapter = new InstagramPostsAdapter(allPosts, context);

        if (isNetworkAvailable(this)) {
            fetchPost();
        } else {
            allPosts.clear();
            adapter.clear();
            allPosts.addAll(db.getAllInstagramPosts());
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Network Unavailable", Toast.LENGTH_SHORT).show();
        }

        // create adapter
        rvPosts.setAdapter(adapter);

        // set adapter
        rvPosts.setLayoutManager(new LinearLayoutManager(this));

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable(context)) {
                    InstagramClient.getPopularFeed(new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            allPosts.clear();
                            adapter.clear();
                            allPosts.addAll(Utils.decodePostsFromJsonResponse(response));
                            adapter.notifyDataSetChanged();
                            db.emptyAllTables();
                            db.addInstagramPosts(allPosts);
                            swipeContainer.setRefreshing(false);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable t) {
                            allPosts.clear();
                            adapter.clear();
                            allPosts.addAll(db.getAllInstagramPosts());
                            adapter.notifyDataSetChanged();
                            Log.d("DEBUG", "Fetch timeline error: " + t.toString());
                        }
                    });
                }
                else {
                    allPosts.clear();
                    adapter.clear();
                    allPosts.addAll(db.getAllInstagramPosts());
                    adapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(context, "Network Unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

    public void fetchPost() {

        try {
            InstagramClient.getPopularFeed(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    allPosts.addAll(Utils.decodePostsFromJsonResponse(response));
                    db.emptyAllTables();
                    db.addInstagramPosts(allPosts);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    try {
                        allPosts.clear();
                        adapter.clear();
                        allPosts.addAll(db.getAllInstagramPosts());
                        adapter.notifyDataSetChanged();
                        Log.e("getPoularFeed", "OnFailure", t);
                    }
                    catch (Throwable e) {
                        Log.e("Error", e.getMessage());
                    }
                }
            });
        }
        catch (Throwable e) {
            e.getStackTrace();
        }
    }

    private static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

}
