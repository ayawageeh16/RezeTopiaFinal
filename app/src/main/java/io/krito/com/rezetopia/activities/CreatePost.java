package io.krito.com.rezetopia.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tangxiaolv.telegramgallery.GalleryActivity;
import com.tangxiaolv.telegramgallery.GalleryConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.arieridwan.lib.PageLoader;
import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.application.AppConfig;
import io.krito.com.rezetopia.application.RezetopiaApp;
import io.krito.com.rezetopia.models.pojo.post.PostResponse;
import io.krito.com.rezetopia.views.CustomTextView;

public class CreatePost extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST_CODE = 1006;
    TextView createPost;
    TextView privacyText;
    ImageView privacyIcon;
    ImageView back;
    EditText postText;
    CustomTextView image;
    //CustomTextView video;
    CustomTextView location;
    CustomTextView tag;
    PageLoader loader;
    RecyclerView imagesVideoRecView;

    private String userId;
    private List<String> selectedImages;
    private String selectedVideo;
    List<MediaType> media;
    private ArrayList<String> encodedImages;
    private RecyclerView.Adapter imageVideoAdapter;
    private String decodedVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        userId = getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, null);

        createPost = findViewById(R.id.createPostView);
        privacyText = findViewById(R.id.privacy);
        privacyIcon = findViewById(R.id.privacyIcon);
        back = findViewById(R.id.backView);
        postText = findViewById(R.id.postText);
        image = findViewById(R.id.pickImageVideo);
        //video = findViewById(R.id.pickVideo);
        location = findViewById(R.id.pickLocation);
        tag = findViewById(R.id.tagFriend);
        loader = findViewById(R.id.pageLoader);
        imagesVideoRecView = findViewById(R.id.imagesRecView);

        createPost.setOnClickListener(this);
        privacyText.setOnClickListener(this);
        privacyIcon.setOnClickListener(this);
        back.setOnClickListener(this);
        image.setOnClickListener(this);
    }

    private void showPostPopupWindow(View anchor) {
        final ListPopupWindow popupWindow = new ListPopupWindow(this);

//        List<MenuCustomItem> itemList = new ArrayList<>();
//        itemList.add(new MenuCustomItem(getResources().getString(R.string.public_), R.drawable.ic_earth));
//        itemList.add(new MenuCustomItem(getResources().getString(R.string.friends), R.drawable.ic_account_check));
//        itemList.add(new MenuCustomItem(getResources().getString(R.string.friends_of_friends), R.drawable.ic_account_switch));
//        itemList.add(new MenuCustomItem(getResources().getString(R.string.only_me), R.drawable.ic_lock));


//        ListAdapter adapter = new ListPopupWindowAdapter(this, itemList, R.layout.dark_custom_menu);
//        popupWindow.setAnchorView(anchor);
//        popupWindow.setAdapter(adapter);
//        popupWindow.setWidth(500);
//        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (i == 0){
//
//                } else if (i == 1){
//
//                } else if (i == 2){
//
//                } else if (i == 3){
//
//                }
//
//                popupWindow.dismiss();
//            }
//        });
//        popupWindow.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.createPostView:
                if (validPost()){
                    loader.startProgress();
                    performUserUpload();
                }
                break;
            case R.id.privacy:
                //showPostPopupWindow(privacyText);
                break;
            case R.id.privacyIcon:
                //showPostPopupWindow(privacyText);
                break;
            case R.id.backView:
                onBackPressed();
                break;
            case R.id.pickImageVideo:
                openImagePicker();
                break;
//            case R.id.pickVideo:
//                break;
            case R.id.pickLocation:
                break;
            case R.id.tagFriend:
                break;

            default:
                break;
        }
    }

    private boolean validPost(){
        if (!postText.getText().toString().isEmpty()){
            return true;
        }

        return false;
    }

    private void openImagePicker() {
        GalleryConfig config = new GalleryConfig.Build()
                .singlePhoto(false)
                .hintOfPick("Pick image/s")
                //.filterMimeTypes(new String[]{"image/jpeg"})
                .build();
        GalleryActivity.openActivity(CreatePost.this, PICK_IMAGE_REQUEST_CODE, config);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST_CODE && data != null) {
            selectedImages = (List<String>) data.getSerializableExtra(GalleryActivity.PHOTOS);
            selectedVideo = data.getExtras().getString(GalleryActivity.VIDEO);
            media = new ArrayList<>();

            if (selectedImages != null && selectedImages.size() > 0){
                Log.i("ImageSize", "onActivityResult: " + selectedImages.size());
                imagesVideoRecView.setVisibility(View.VISIBLE);
                for (String path:selectedImages) {
                    MediaType mediaType = new MediaType();
                    mediaType.path = path;
                    mediaType.type = MediaType.IMAGE_TYPE;
                    media.add(mediaType);
                }
            }

            if (selectedVideo != null && !selectedVideo.isEmpty()) {
                Log.i("videoPath", "onActivityResult: " + selectedVideo);
                imagesVideoRecView.setVisibility(View.VISIBLE);
                MediaType mediaType = new MediaType();
                mediaType.path = selectedVideo;
                mediaType.type = MediaType.VIDEO_TYPE;
                media.add(mediaType);
            }

            if (media != null && media.size() > 0){
                updateImageHolder();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void performUserUpload() {
        encodedImages = new ArrayList<>();
        if (selectedImages != null) {
            if (selectedImages.size() > 0) {
                for (String path : selectedImages) {
                    Bitmap bm = null;
                    bm = BitmapFactory.decodeFile(path);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    String encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                    encodedImages.add(encodedImage);
                }

                HashMap<String, ArrayList<String>> jsonMap = new HashMap<>();
                jsonMap.put("imageList", encodedImages);
            }
        }

        if (selectedVideo != null && !selectedVideo.isEmpty()) {
            File originalFile = new File(selectedVideo);
            try {
                FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
                byte[] bytes = new byte[(int) originalFile.length()];
                fileInputStreamReader.read(bytes);
                decodedVideo = Base64.encodeToString(bytes, Base64.DEFAULT);
                Log.i("DecodedVideo", "performUserUpload: " + decodedVideo);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://rezetopia.dev-krito.com/app/reze/user_post.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("volley response", "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            PostResponse postResponse = new PostResponse();
                            //todo add
                            postResponse.setUsername(jsonObject.getString("username"));
                            postResponse.setCreatedAt(jsonObject.getString("createdAt"));
                            postResponse.setText(jsonObject.getString("text"));
                            postResponse.setPostId(jsonObject.getInt("post_id"));
                            postResponse.setCreatedAt(jsonObject.getString("createdAt"));
                            postResponse.setUserId(userId);
                            Intent intent = new Intent();
                            intent.putExtra("post", postResponse);
                            setResult(RESULT_OK, intent);
                            onBackPressed();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volley error", "onErrorResponse: " + error.getMessage());
                loader.stopProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();

                if (encodedImages.size() > 0) {
                    for (String value : encodedImages) {
                        map.put(String.valueOf(encodedImages.indexOf(value)), value);
                    }
                    map.put("images_size", String.valueOf(encodedImages.size()));
                }

                if (decodedVideo != null && !decodedVideo.isEmpty()){
                    map.put("video", decodedVideo);
                }

                map.put("method", "create_post");
                map.put("userId", userId);
                if (postText.getText().toString().length() > 0) {
                    map.put("post_text", postText.getText().toString());
                }

                return map;
            }
        };

        RezetopiaApp.getInstance().getRequestQueue().add(stringRequest);
    }

    private class ImageVideoViewHolder extends RecyclerView.ViewHolder{

        ImageView close;
        ImageView image;
        VideoView video;

        public ImageVideoViewHolder(View itemView) {
            super(itemView);

            close = itemView.findViewById(R.id.closeView);
            image = itemView.findViewById(R.id.imageView);
            video = itemView.findViewById(R.id.videoView);
        }

        public void bind(final MediaType i){
            if (i.type == MediaType.IMAGE_TYPE) {
                video.setVisibility(View.GONE);
                Bitmap bm = BitmapFactory.decodeFile(i.path);
                image.setVisibility(View.VISIBLE);
                image.setImageBitmap(bm);

            } else {
                image.setVisibility(View.GONE);
                video.setVisibility(View.VISIBLE);
                video.setVideoPath(i.path);
                video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setVolume(0, 0);
                    }
                });
                video.start();

            }

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    media.remove(i);
                    imageVideoAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private class ImageVideoRecyclerAdapter extends RecyclerView.Adapter<ImageVideoViewHolder>{

        @NonNull
        @Override
        public ImageVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(CreatePost.this).inflate(R.layout.image_card, parent, false);
            return new ImageVideoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageVideoViewHolder holder, int position) {
            holder.bind(media.get(position));
        }

        @Override
        public int getItemCount() {
            return media.size();
        }
    }

    private void updateImageHolder(){
        if (imageVideoAdapter == null){
            imageVideoAdapter = new ImageVideoRecyclerAdapter();
            imagesVideoRecView.setAdapter(imageVideoAdapter);
            imagesVideoRecView.setLayoutManager(new GridLayoutManager(this, 5));
        } else {
            imageVideoAdapter.notifyDataSetChanged();
        }
    }

    private class MediaType{
        public static final int IMAGE_TYPE = 0;
        public static final int VIDEO_TYPE = 1;

        public int type;
        public String path;
    }

}
