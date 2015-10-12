package com.example.dgunda.instagramclient;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {

    public static final String CLIENT_ID="26d1e042015c48e1b2daab5ab9cc9a01";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotoAdapter aPhoto;
    private SwipeRefreshLayout swipeContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        photos = new ArrayList<>();
        aPhoto = new InstagramPhotoAdapter(this,photos);
        ListView lvPhotos = (ListView)findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhoto);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPopularPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



        fetchPopularPhotos();
    }

    public void fetchPopularPhotos()
    {
        String url = "https://api.instagram.com/v1/media/popular?client_id="+CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url,null,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.i("DEBUG",response.toString());
                // Remember to CLEAR OUT old items before appending in the new ones
                aPhoto.clear();
                // ...the data has come back, add new items to your adapter...
                aPhoto.addAll();
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);

                JSONArray photosJSON = null;
                try{
                    photosJSON=response.getJSONArray("data");
                    for(int i=0;i<photosJSON.length();i++)
                    {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        JSONArray commentJsonArray = photosJSON.getJSONObject(i).getJSONObject("comments").getJSONArray("data");
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username=photoJSON.getJSONObject("user").getString("username");
                        photo.caption=photoJSON.getJSONObject("caption").getString("text");
                        photo.imageUrl=photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight=photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("height");
                        photo.likesCount=photoJSON.getJSONObject("likes").getInt("count");
                        photo.profileimageUrl=photoJSON.getJSONObject("user").getString("profile_picture");
                        if(commentJsonArray.length()!=0)
                            photo.comment1=commentJsonArray.getJSONObject(0).getString("text");
                        if(commentJsonArray.length()>2) {
                            photo.comment2 = commentJsonArray.getJSONObject(1).getString("text");
                        }
                        photos.add(photo);
                    }
                }catch(Exception e)
                {
                    Log.e("ERROR", e.getMessage());
                }
                aPhoto.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //super.onFailure(statusCode, headers, responseString, throwable);
                Log.i("DEBUG", responseString.toString());
                Log.e("ERROR",throwable.toString());
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar   if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
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
}
