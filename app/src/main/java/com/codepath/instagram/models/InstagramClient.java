package com.codepath.instagram.models;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.instagram.helpers.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

/**
 * Created by mmar on 10/28/15.
 */
public class InstagramClient {
    public static String mediaId;

    public static void getCommentsFeed(String media, JsonHttpResponseHandler jsonHttpResponseHandler) {
        // client ID: 5783bc65d4fc4b0d9fc6b987329e5810
        String url = "https://api.instagram.com/v1/media/" + media + "/comments?client_id=5783bc65d4fc4b0d9fc6b987329e5810";
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, null, jsonHttpResponseHandler);
    }

    public static void getPopularFeed(JsonHttpResponseHandler jsonHttpResponseHandler) {
        // client ID: 5783bc65d4fc4b0d9fc6b987329e5810
        String url = "https://api.instagram.com/v1/media/popular?client_id=5783bc65d4fc4b0d9fc6b987329e5810";
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, null, jsonHttpResponseHandler);
    }
}
