package io.krito.com.rezetopia.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.krito.com.rezetopia.R;

public class GroupDescription extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.home_btn)
    Button  home;
    @BindView(R.id.add_friend_btn)
    Button  addFriend;
    @BindView(R.id.group_name_tv)
    TextView groupName_tv ;
    @BindView(R.id.group_privacy_tv)
    TextView groupPrivacy_tv;
    @BindView(R.id.group_members_tv)
    TextView groupMembers_tv;
    @BindView(R.id.description_tv)
    TextView descriptiontv;
    @BindView(R.id.settings_icon)
    ImageView settings;

    int membersNumber ;
    String groupId;
    String groupPrivacy;
    String groupName;
    String description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_description);
        ButterKnife.bind(this);

        home.setOnClickListener(this);
        addFriend.setOnClickListener(this);
        settings.setOnClickListener(this);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            membersNumber = extras.getInt("membersNumber");
            groupId = extras.getString("groupId");
            groupPrivacy = extras.getString("groupPrivacy");
            groupName = extras.getString("groupName");
            description = extras.getString("description");
        }

        setView();
    }

    private void setView() {
        groupName_tv.setText(groupName);
        groupPrivacy_tv.setText(groupPrivacy);
        groupMembers_tv.setText(String.valueOf(membersNumber)+ " " +getString(R.string.member));
        descriptiontv.setText(description);
    }

    @Override
    public void onClick(View v) {
        if (v == home){
            Intent intent = new Intent(GroupDescription.this, Group.class);
            startActivity(intent);
        }else if (v == addFriend){
            Intent intent = new Intent(GroupDescription.this, GroupAddMember.class);
            intent.putExtra("groupName",groupName);
            intent.putExtra("membersNumber", membersNumber);
            intent.putExtra("groupPrivacy", groupPrivacy);
            intent.putExtra("groupId",groupId);
            startActivity(intent);
        }else if (v == settings){
            Intent intent = new Intent(GroupDescription.this, GroupSettings.class);
            intent.putExtra("groupName",groupName);
            intent.putExtra("membersNumber", membersNumber);
            intent.putExtra("groupPrivacy", groupPrivacy);
            intent.putExtra("groupId",groupId);
            startActivity(intent);
        }
    }
}
