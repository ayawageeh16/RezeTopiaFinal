package io.krito.com.rezetopia.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.arieridwan.lib.PageLoader;
import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.application.AppConfig;
import io.krito.com.rezetopia.application.RezetopiaApp;
import io.krito.com.rezetopia.helper.ListPopupWindowAdapter;
import io.krito.com.rezetopia.helper.MenuCustomItem;
import io.krito.com.rezetopia.models.pojo.news_feed.NewsFeedItem;
import ru.whalemare.sheetmenu.SheetMenu;

public class CreatePp extends AppCompatActivity implements View.OnClickListener {

    public static boolean skip = false;

    String userId;
    String privacyText = "public";
    Image image;

    ImageView backView;
    TextView createPostView;
    ImageView privacyIcon;
    TextView privacy;
    ImageView ppView;
    ImageView removePpImage;
    EditText ppText;
    PageLoader pageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pp);
        /*if (skip){
            skip = false;
            pickPpImage();
        } else {

        }*/

        userId = getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, null);

        backView = findViewById(R.id.backView);
        createPostView = findViewById(R.id.createPostView);
        privacyIcon = findViewById(R.id.privacyIcon);
        privacy = findViewById(R.id.privacy);
        ppView = findViewById(R.id.ppView);
        removePpImage = findViewById(R.id.removePpImage);
        ppText = findViewById(R.id.ppText);
        pageLoader = findViewById(R.id.pageLoader);

        //createPostView.setEnabled(false);
        privacy.setOnClickListener(this);
        privacyIcon.setOnClickListener(this);
        removePpImage.setOnClickListener(this);
        createPostView.setOnClickListener(this);

        pickPpImage();
    }

    private void pickPpImage(){
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        ImagePicker.create(CreatePp.this)
                                .folderMode(true)
                                .limit(1)
                                .theme(R.style.CustomImagePickerTheme)
                                .start();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    private void uploadPp(final String path){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://rezetopia.dev-krito.com/app/reze/user_post.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("CreatePpPost", "onResponse: " + response);
                        NewsFeedItem item = new NewsFeedItem();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getInt("post_id") > 0){
                                item.setOwnerId(userId);
                                item.setPostId(jsonObject.getString("post_id"));
                                item.setOwnerName(jsonObject.getString("username"));
                                item.setCreatedAt(jsonObject.getString("createdAt"));
                                if (jsonObject.getString("text") != null && !jsonObject.getString("text").isEmpty()){
                                    item.setPostText(jsonObject.getString("text"));
                                }

                                if (jsonObject.getString("url") != null && !jsonObject.getString("url").isEmpty()){
                                    String ppUrl = jsonObject.getString("url");
                                    item.setItemImage(ppUrl);
                                }

                            } else {
                                pageLoader.stopProgress();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (Integer.parseInt(item.getPostId()) > 0){
                            Intent intent = new Intent();
                            intent.putExtra("post", item);
                            setResult(RESULT_OK, intent);
                            onBackPressed();
                        } else {
                            pageLoader.stopProgress();
                        }
                        //pageLoader.stopProgress();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pageLoader.stopProgress();
                //Log.i("volley error", "onErrorResponse: " + error.getMessage());

                if (error instanceof NetworkError) {
                    Log.i("CreateError",  getResources().getString(R.string.connection_error));
                } else if (error instanceof ServerError) {
                    Log.i("CreateError",  getResources().getString(R.string.server_error));
                } else if (error instanceof AuthFailureError) {
                    Log.i("CreateError",  getResources().getString(R.string.connection_error));
                } else if (error instanceof ParseError) {
                    Log.i("CreateError",  getResources().getString(R.string.parsing_error));
                } else if (error instanceof NoConnectionError) {
                    Log.i("CreateError",  getResources().getString(R.string.connection_error));
                } else if (error instanceof TimeoutError) {
                    Log.i("CreateError",  getResources().getString(R.string.time_out));
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();

                map.put("method", "create_pp_post");
                map.put("userId", userId);
                if (ppText.getText().toString().length() > 0) {
                    map.put("post_text", ppText.getText().toString());
                }

                map.put("privacy", privacyText);

                if (path != null && !path.isEmpty()) {
                    Bitmap bm = null;
                    bm = BitmapFactory.decodeFile(path);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    String encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

                    map.put("image", encodedImage);
                }

                return map;
            }
        };

        RezetopiaApp.getInstance().getRequestQueue().add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)){
            image = ImagePicker.getFirstImageOrNull(data);
            createPostView.setEnabled(true);

            Bitmap bm = null;
            bm = BitmapFactory.decodeFile(image.getPath());
            ppView.setImageBitmap(bm);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.privacy:
                //showPostPopupWindow(privacy);
                createSheet();
                break;
            case R.id.privacyIcon:
                createSheet();
                //showPostPopupWindow(privacy);
                break;
            case R.id.removePpImage:
                pickPpImage();
                break;
            case R.id.createPostView:
                if (image != null) {
                    uploadPp(image.getPath());
                    pageLoader.startProgress();
                }
                break;
        }
    }

    private void createSheet(){
        SheetMenu.with(this)
                .setMenu(R.menu.post_privacy_menu)
                .setAutoCancel(true)
                .setClick(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.publicId:
                                privacyText = "public";
                                privacy.setText(R.string.public_);
                                privacyIcon.setBackground(getResources().getDrawable(R.drawable.earth));
                                break;
                            case R.id.friendsId:
                                privacyText = "friends";
                                privacy.setText(R.string.friends);
                                privacyIcon.setBackground(getResources().getDrawable(R.drawable.account_check));
                                break;
                            case R.id.friends_of_friendsId:
                                privacyText = "friends_of_friends";
                                privacy.setText(R.string.friends_of_friends);
                                privacyIcon.setBackground(getResources().getDrawable(R.drawable.account_multiple_check));
                                break;
                            case R.id.only_meId:
                                privacyText = "only_me";
                                privacy.setText(R.string.only_me);
                                privacyIcon.setBackground(getResources().getDrawable(R.drawable.lock));
                                break;
                        }
                        return false;
                    }
                }).show();
    }
}
