package com.codepath.instagram.networking;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.codepath.instagram.helpers.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mmar on 11/3/15.
 */
public class NetworkedIntentService extends IntentService {
    private AsyncHttpClient aClient = new SyncHttpClient();

    public NetworkedIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Send synchronous request
        String url = "https://api.instagram.com/v1/media/popular?client_id=5783bc65d4fc4b0d9fc6b987329e5810";
        final SyncHttpClient client = new SyncHttpClient();

        client.get(this, url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
//                allPosts.addAll(Utils.decodePostsFromJsonResponse(response));
//                db.emptyAllTables();
//                db.addInstagramPosts(allPosts);
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable t) {
                super.onFailure(statusCode, headers, responseString, t);
                Log.d("DEBUG", "Fetch timeline error: " + t.toString());
            }
        });
    }
}
