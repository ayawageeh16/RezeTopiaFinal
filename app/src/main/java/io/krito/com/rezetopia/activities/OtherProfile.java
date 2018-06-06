package io.krito.com.rezetopia.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.application.AppConfig;
import io.krito.com.rezetopia.fragments.AlertFragment;
import io.krito.com.rezetopia.models.operations.ProfileOperations;
import io.krito.com.rezetopia.models.pojo.User;

public class OtherProfile extends AppCompatActivity implements View.OnClickListener {

    private static final String USER_ID_EXTRA = "user_id";

    CircleImageView ppView;
    TextView usernameView;
    ImageView coverView;
    TextView addFriendBtn;

    String userId;
    String loggedInUserId;

    boolean isFriend = false;
    boolean friendState = false;

    public static Intent createIntent(String id, Context context){
        Intent intent = new Intent(context, OtherProfile.class);
        intent.putExtra(USER_ID_EXTRA, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        userId = getIntent().getExtras().getString(USER_ID_EXTRA);
        loggedInUserId = getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE).getString(AppConfig.LOGGED_IN_USER_ID_SHARED, null);

        ppView = findViewById(R.id.ppView);
        usernameView = findViewById(R.id.usernameView);
        coverView = findViewById(R.id.coverView);
        addFriendBtn = findViewById(R.id.addFriendBtn);

        addFriendBtn.setOnClickListener(this);

        getInfo();
    }

    private void getInfo(){
        ProfileOperations.getInfo(userId);
        ProfileOperations.setInfoCallback(new ProfileOperations.UserInfoCallback() {
            @Override
            public void onSuccess(User user) {
                if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()){
                    Picasso.with(OtherProfile.this).load("http://rezetopia.dev-krito.com/images/profileImgs/" + user.getImageUrl() + ".JPG").into(ppView);
                }

                if (user.getCover() != null && !user.getCover().isEmpty()){
                    Picasso.with(OtherProfile.this).load(user.getCover()).into(coverView);
                } else {
                    coverView.setBackground(getResources().getDrawable(R.drawable.cover));
                }

                usernameView.setText(user.getName());

                ProfileOperations.isFriend(loggedInUserId, userId);

                ProfileOperations.setIsFriendCallback(new ProfileOperations.IsFriendCallback() {
                    @Override
                    public void onSuccess(boolean[] is_friend) {
                        Log.e("IsFriendActivity", "onSuccess: " + String.valueOf(isFriend) + "-" + String.valueOf(friendState));
                        isFriend = is_friend[0];
                        friendState = is_friend[1];
                        if (isFriend && friendState){
                            addFriendBtn.setText(getResources().getString(R.string.remove_friend));
                        } else if (isFriend && !friendState){
                            addFriendBtn.setText(getResources().getString(R.string.cancel_friend_request));
                        } else {
                            addFriendBtn.setText(getResources().getString(R.string.add));
                        }
                    }

                    @Override
                    public void onError(int error) {
                        AlertFragment.createFragment(getResources().getString(error)).show(getFragmentManager(), null);
                    }
                });
            }

            @Override
            public void onError(int error) {
                AlertFragment.createFragment(getResources().getString(error)).show(getFragmentManager(), null);
            }
        });
    }

    private void sendFriendRequest(){
        addFriendBtn.setEnabled(false);
        ProfileOperations.sendFriendRequest(loggedInUserId, userId);

        ProfileOperations.setFriendRequestCallback(new ProfileOperations.SendFriendRequestCallback() {
            @Override
            public void onSuccess(boolean result) {
                addFriendBtn.setEnabled(true);
                if (result){
                    addFriendBtn.setText(getResources().getString(R.string.cancel_friend_request));
                    isFriend = true;
                } else {

                }
            }

            @Override
            public void onError(int error) {
                addFriendBtn.setEnabled(true);
            }
        });
    }

    private void cancelDeleteFriendship(){
        addFriendBtn.setEnabled(false);
        ProfileOperations.cancelFriendRequest(loggedInUserId, userId);

        ProfileOperations.setCancelDeleteFriendRequestCallback(new ProfileOperations.CancelDeleteFriendRequestCallback() {
            @Override
            public void onSuccess(boolean result) {
                addFriendBtn.setText(getResources().getString(R.string.add));
                addFriendBtn.setEnabled(true);
                isFriend = false;
            }

            @Override
            public void onError(int error) {
                addFriendBtn.setEnabled(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addFriendBtn:
                if (isFriend){
                    cancelDeleteFriendship();
                } else {
                    sendFriendRequest();
                }
                break;
            case R.id.ppView:
                break;
        }
    }
}
