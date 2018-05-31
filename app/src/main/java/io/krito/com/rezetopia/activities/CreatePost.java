package io.krito.com.rezetopia.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
import com.tangxiaolv.telegramgallery.GalleryActivity;
import com.tangxiaolv.telegramgallery.GalleryConfig;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

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
import java.util.UUID;

import id.arieridwan.lib.PageLoader;
import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.application.AppConfig;
import io.krito.com.rezetopia.application.RezetopiaApp;
import io.krito.com.rezetopia.helper.EncodeBase64;
import io.krito.com.rezetopia.helper.Upload;
import io.krito.com.rezetopia.models.pojo.post.PostResponse;
import io.krito.com.rezetopia.views.CustomTextView;

public class CreatePost extends AppCompatActivity implements View.OnClickListener {

    private static final int TYPE_TEXT = 0;
    private static final int TYPE_VIDEO = 1;
    private static final int TYPE_TEXT_VIDEO = 2;
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
    RelativeLayout createPostNav;

    private String decodedVideo;
    private String userId;
    private List<String> selectedImages;
    private String selectedVideo;
    List<MediaType> media;
    private ArrayList<String> encodedImages;
    private RecyclerView.Adapter imageVideoAdapter;
    private int postType;

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
        createPostNav = findViewById(R.id.createPostNav);

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
//                    if (selectedVideo != null && !selectedVideo.isEmpty()){
//                        if (decodeVideo()){
//                            loader.startProgress();
//                            performUserUpload();
//                            return;
//                        }
//                    }
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
        if (!postText.getText().toString().isEmpty() || (selectedImages != null && selectedImages.size() > 0)
                || (selectedVideo != null && !selectedVideo.isEmpty())){
            postType = TYPE_TEXT;

            if (selectedVideo != null && !selectedVideo.isEmpty()){
                postType = TYPE_VIDEO;
            }

            if (selectedVideo != null && !selectedVideo.isEmpty() && !postText.getText().toString().isEmpty()){
                postType = TYPE_TEXT_VIDEO;
            }

            return true;
        } else if (postText.getText().toString().isEmpty()){
            Snackbar.make(createPostNav, R.string.empty_post, BaseTransientBottomBar.LENGTH_SHORT).show();
            return false;
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
                imagesVideoRecView.setVisibility(View.VISIBLE);
                Log.i("videoPath", "onActivityResult: " + selectedVideo);
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
            }
        }

        if (postType == TYPE_TEXT) {
            createPost();
        } else
            uploadVideo(postText.getText().toString());
            //uploadMultipart(postText.getText().toString());
    }

    private void createPost(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://rezetopia.dev-krito.com/app/reze/user_post.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("volley response", "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            PostResponse postResponse = new PostResponse();
                            //todo add
                            postResponse.setPostId(jsonObject.getInt("post_id"));
                            postResponse.setUsername(jsonObject.getString("username"));
                            postResponse.setCreatedAt(jsonObject.getString("createdAt"));
                            postResponse.setUserId(userId);
                            postResponse.setText(null);
                            if (postType == TYPE_TEXT) {
                                postResponse.setText(jsonObject.getString("text"));
                            }
                            Intent intent = new Intent();
                            intent.putExtra("post", response);
                            setResult(RESULT_OK, intent);
                            onBackPressed();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i("volley error", "onErrorResponse: " + error.getMessage());
                loader.stopProgress();
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

                if (encodedImages != null && encodedImages.size() > 0) {
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

    private boolean decodeVideo(){
        if (selectedVideo != null && !selectedVideo.isEmpty()) {
            File originalFile = new File(selectedVideo);

            int file_size = Integer.parseInt(String.valueOf(originalFile.length()/1024));

            if (file_size <= EncodeBase64.availableMemoryMB() - EncodeBase64.SAFETY_MEMORY_BUFFER){
                try {
                    FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
                    byte[] bytes = new byte[(int) originalFile.length()];
                    fileInputStreamReader.read(bytes);
                    decodedVideo = Base64.encodeToString(bytes, Base64.DEFAULT);
                    Log.i("DecodedVideo", "performUserUpload: " + decodedVideo);
                    return true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.i("AviSize", "performUserUpload: " + EncodeBase64.availableMemoryMB());
            Log.i("fileSize", "performUserUpload: " + file_size);
            return false;
        }

        return false;
    }

    private void uploadVideo(final String postText) {
        class UploadVideo extends AsyncTask<Void, Void, Boolean> {
            @Override
            protected void onPostExecute(Boolean s) {
                super.onPostExecute(s);
                loader.stopProgress();
            }


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                Upload u = new Upload();
                return u.uploadVideo(selectedVideo, postText);
            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute();
    }

    public void uploadMultipart(String post) {
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, "http://rezetopia.dev-krito.com/app/reze/user_post.php")
                    .addFileToUpload(selectedVideo, "MP4")
                    .addParameter("post_text", post)
                    .addParameter("userId", userId)
                    .addParameter("method", "video_post")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setUtf8Charset()
                    .startUpload();

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
