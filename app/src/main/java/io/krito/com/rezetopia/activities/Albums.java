package io.krito.com.rezetopia.activities;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnimBarBuilder;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.application.AppConfig;
import io.krito.com.rezetopia.application.RezetopiaApp;
import io.krito.com.rezetopia.helper.VolleyCustomRequest;
import io.krito.com.rezetopia.models.pojo.post.Album;
import io.krito.com.rezetopia.models.pojo.post.ApiAlbums;
import io.krito.com.rezetopia.views.CustomTextView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Albums extends AppCompatActivity {

    private static final String USER_ID_EXTRA = "user_id";

    String userId;
    ArrayList<Album> albums;
    RecyclerView recyclerView;
    AlbumsAdapter adapter;

    public static Intent createIntent(Context context, String id){
        Intent intent = new Intent(context, Albums.class);
        intent.putExtra(id, USER_ID_EXTRA);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

        networkListener();

        userId = getIntent().getExtras().getString("id");

        recyclerView = findViewById(R.id.albumsRecView);
        getAlbums();
    }

    private void getAlbums(){
        VolleyCustomRequest request = new VolleyCustomRequest(Request.Method.POST, "http://rezetopia.dev-krito.com/app/reze/user_post.php", ApiAlbums.class,
                new Response.Listener<ApiAlbums>() {
                    @Override
                    public void onResponse(ApiAlbums response) {
                        if (!response.isError()){
                            albums = new ArrayList<>(Arrays.asList(response.getAlbums()));
                            updateUi();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("method", "get_albums");
                map.put("profileOwnerUserId", userId);
                return map;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RezetopiaApp.getInstance().getRequestQueue().add(request);
    }

    private class AlbumsHolder extends RecyclerView.ViewHolder{

        ImageView image;
        CustomTextView name;

        public AlbumsHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.albumCover);
            name = itemView.findViewById(R.id.albumName);
        }

        public void bind(Album album){
            String url;
            name.setText(album.getName());
            if (album.getName().contentEquals("profile pictures") || album.getName().contentEquals("cover photos")){
                url = "http://rezetopia.dev-krito.com/".concat(album.getUrl());
            } else {
                url = "http://rezetopia.dev-krito.com/app/".concat(album.getUrl());
            }

            Picasso.with(Albums.this).load(url).into(image);
        }
    }

    private class AlbumsAdapter extends RecyclerView.Adapter<AlbumsHolder>{

        @NonNull
        @Override
        public AlbumsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(Albums.this).inflate(R.layout.album_card, parent, false);
            return new AlbumsHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AlbumsHolder holder, int position) {
            holder.bind(albums.get(position));
        }

        @Override
        public int getItemCount() {
            return albums.size();
        }
    }

    private void updateUi(){
        if (adapter == null){
            adapter = new AlbumsAdapter();
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {

        }
    }

    private void networkListener(){
        ReactiveNetwork.observeNetworkConnectivity(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(connectivity -> {
                    if (connectivity.getState() == NetworkInfo.State.CONNECTED){
                        Log.i("internetC", "onNext: " + "Connected");
                    } else if (connectivity.getState() == NetworkInfo.State.SUSPENDED){
                        Log.i("internetC", "onNext: " + "LowNetwork");
                    } else {
                        Log.i("internetC", "onNext: " + "NoInternet");
                        Flashbar.Builder builder = new Flashbar.Builder(this);
                        builder.gravity(Flashbar.Gravity.BOTTOM)
                                .backgroundColor(R.color.red2)
                                .enableSwipeToDismiss()
                                .message(R.string.checkingNetwork)
                                .enterAnimation(new FlashAnimBarBuilder(Albums.this).slideFromRight().duration(200))
                                .build().show();
                    }
                });
    }
}