package io.krito.com.rezetopia.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.application.AppConfig;
import io.krito.com.rezetopia.models.pojo.news_feed.NewsFeedItem;
import io.krito.com.rezetopia.views.CustomTextView;

/**
 * Created by Mona Abdallh on 6/28/2018.
 */

public class Share extends DialogFragment implements View.OnClickListener{

    NewsFeedItem item;
    ArrayList<String> imagesList;
    String userId;

    ImageView close;
    EditText textShare;
    RecyclerView images;
    TextView username;
    TextView date;
    ImageView privacyIcon;
    TextView ownerText;
    CustomTextView share;
    CustomTextView cancel;

    public static Share createShareFragment(NewsFeedItem i){
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", i);
        Share share = new Share();
        share.setArguments(bundle);
        return share;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        close = view.findViewById(R.id.closeShare);
        textShare = view.findViewById(R.id.shareText);
        images = view.findViewById(R.id.imagesRecView);
        username = view.findViewById(R.id.postOwnerNameView);
        date = view.findViewById(R.id.shareDateView);
        privacyIcon = view.findViewById(R.id.sharePrivacy);
        ownerText = view.findViewById(R.id.ownerPostText);
        share = view.findViewById(R.id.share);
        cancel = view.findViewById(R.id.cancel);

        userId = getActivity().getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, null);

        item = (NewsFeedItem) getArguments().getSerializable("item");

        if (item != null){
            if (item.getPostAttachment() != null && item.getPostAttachment().getImages() != null && item.getPostAttachment().getImages().length > 0){

            }

            if (item.getPostText() != null && !item.getPostText().isEmpty()){
                ownerText.setText(item.getPostText());
            }

            username.setText(item.getOwnerName());
            date.setText(item.getCreatedAt());

        }

        share.setOnClickListener(this);
        cancel.setOnClickListener(this);
        return view;
    }


    private void sharePost(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://rezetopia.dev-krito.com/app/reze/user_post.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("share_post", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")){
                                dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volley error", "onErrorResponse: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();

                String sText = textShare.getText().toString();
                if (!sText.isEmpty()){
                    sText = sText.trim();
                }
                map.put("method", "share_post");
                map.put("user_id", userId);
                map.put("text", sText);
                map.put("friend_id", "0");
                map.put("post_id", item.getPostId());


                return map;
            }
        };

        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                dismiss();
                break;
            case R.id.share:
                sharePost();
                break;
        }
    }
}
