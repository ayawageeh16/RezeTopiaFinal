package io.krito.com.rezetopia.activities;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.krito.com.rezetopia.R;

public class GroupSettings extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE = 100;
    TextView groupName_tv;
    TextView groupPrivacy_tv;
    TextView group_membersNumber_tv;
    Button   group_edit_cover_btn;
    ImageView cover_image;

    private String groupName;
    private String groupPrivacy;
    private int membersNumber;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        groupName_tv = findViewById(R.id.group_name_tv);
        groupPrivacy_tv = findViewById(R.id.group_privacy_tv);
        group_membersNumber_tv = findViewById(R.id.group_members_tv);
        cover_image = findViewById(R.id.cover_image_im);
        group_edit_cover_btn = findViewById(R.id.edit_cover_btn);
        group_edit_cover_btn.setOnClickListener(this);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            membersNumber = extras.getInt("membersNumber");
            groupName = extras.getString("groupName");
            groupPrivacy = extras.getString("groupPrivacy");
            setView();
        }
    }

    private void setView() {
        groupName_tv.setText(groupName);
        groupPrivacy_tv.setText(groupPrivacy);
        group_membersNumber_tv.setText(String.valueOf(membersNumber));
    }

    @Override
    public void onClick(View v) {
        if (v == group_edit_cover_btn){
            openGallery();
        }
    }

    //Pick Cover Photo From gallery
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery , PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            cover_image.setImageURI(imageUri);
        }
    }
}
