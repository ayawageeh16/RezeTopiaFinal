package io.krito.com.reze.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import io.krito.com.reze.R;
import io.krito.com.reze.application.AppConfig;
import io.krito.com.reze.application.RezetopiaApp;
import io.krito.com.reze.helper.ListPopupWindowAdapter;
import io.krito.com.reze.helper.MenuCustomItem;
import io.krito.com.reze.models.pojo.post.PostResponse;
import io.krito.com.reze.views.CustomEditText;
import io.krito.com.reze.views.CustomTextView;

public class CreatePost extends AppCompatActivity implements View.OnClickListener {

    TextView createPost;
    TextView privacyText;
    ImageView privacyIcon;
    ImageView back;
    EditText postText;
    CustomTextView image;
    CustomTextView video;
    CustomTextView location;
    CustomTextView tag;
    PageLoader loader;
    RecyclerView imagesRecView;

    private String userId;
    private List<Image> selectedImages;
    private ArrayList<String> encodedImages;
    private RecyclerView.Adapter imageAdapter;

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
        image = findViewById(R.id.pickImage);
        video = findViewById(R.id.pickVideo);
        location = findViewById(R.id.pickLocation);
        tag = findViewById(R.id.tagFriend);
        loader = findViewById(R.id.pageLoader);
        imagesRecView = findViewById(R.id.imagesRecView);

        createPost.setOnClickListener(this);
        privacyText.setOnClickListener(this);
        privacyIcon.setOnClickListener(this);
        back.setOnClickListener(this);
        image.setOnClickListener(this);
    }

    private void showPostPopupWindow(View anchor) {
        final ListPopupWindow popupWindow = new ListPopupWindow(this);

        List<MenuCustomItem> itemList = new ArrayList<>();
        itemList.add(new MenuCustomItem(getResources().getString(R.string.public_), R.drawable.ic_earth));
        itemList.add(new MenuCustomItem(getResources().getString(R.string.friends), R.drawable.ic_account_check));
        itemList.add(new MenuCustomItem(getResources().getString(R.string.friends_of_friends), R.drawable.ic_account_switch));
        itemList.add(new MenuCustomItem(getResources().getString(R.string.only_me), R.drawable.ic_lock));


        ListAdapter adapter = new ListPopupWindowAdapter(this, itemList, R.layout.dark_custom_menu);
        popupWindow.setAnchorView(anchor);
        popupWindow.setAdapter(adapter);
        popupWindow.setWidth(500);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){

                } else if (i == 1){

                } else if (i == 2){

                } else if (i == 3){

                }

                popupWindow.dismiss();
            }
        });
        popupWindow.show();
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
                showPostPopupWindow(privacyText);
                break;
            case R.id.privacyIcon:
                showPostPopupWindow(privacyText);
                break;
            case R.id.backView:
                onBackPressed();
                break;
            case R.id.pickImage:
                openImagePicker();
                break;
            case R.id.pickVideo:
                break;
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
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        ImagePicker.create(CreatePost.this)
                                .folderMode(true) // folder mode (false by default)
                                .toolbarFolderTitle("Folder") // folder selection title
                                .toolbarImageTitle("Tap to select")
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            selectedImages = ImagePicker.getImages(data);
            if (selectedImages != null && selectedImages.size() > 0){
                imagesRecView.setVisibility(View.VISIBLE);
                updateImageHolder();
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void performUserUpload() {
        encodedImages = new ArrayList<>();
        if (selectedImages != null) {
            if (selectedImages.size() > 0) {
                for (Image image : selectedImages) {
                    Bitmap bm = null;
                    bm = BitmapFactory.decodeFile(image.getPath());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    String encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                    encodedImages.add(encodedImage);
                }

                HashMap<String, ArrayList<String>> jsonMap = new HashMap<>();
                jsonMap.put("imageList", encodedImages);
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

    private class ImageViewHolder extends RecyclerView.ViewHolder{

        ImageView close;
        ImageView image;

        public ImageViewHolder(View itemView) {
            super(itemView);

            close = itemView.findViewById(R.id.closeView);
            image = itemView.findViewById(R.id.imageView);
        }

        public void bind(final Image i){
            Bitmap bm = BitmapFactory.decodeFile(i.getPath());
            image.setImageBitmap(bm);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedImages.remove(i);
                    imageAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageViewHolder>{

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(CreatePost.this).inflate(R.layout.image_card, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            holder.bind(selectedImages.get(position));
        }

        @Override
        public int getItemCount() {
            return selectedImages.size();
        }
    }

    private void updateImageHolder(){
        if (imageAdapter == null){
            imageAdapter = new ImageRecyclerAdapter();
            imagesRecView.setAdapter(imageAdapter);
            imagesRecView.setLayoutManager(new GridLayoutManager(this, 5));
        } else {
            imageAdapter.notifyDataSetChanged();
        }
    }

}
