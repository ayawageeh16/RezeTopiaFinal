package io.krito.com.rezetopia.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.helper.Group.AddAdminRecyclerViewAdapter;
import io.krito.com.rezetopia.helper.Group.AddAdminsListViewAdapter;
import io.krito.com.rezetopia.models.pojo.Group.Admin;
import io.krito.com.rezetopia.models.pojo.Group.Member;

public class GroupSettings extends AppCompatActivity implements View.OnClickListener, Filterable {

    private static final int PICK_COVER_IMAGE = 100;
    private static final int PICK_PP_IMAGE = 101;

    @BindView(R.id.group_name_tv)
    TextView groupName_tv;
    @BindView(R.id.group_privacy_tv)
    TextView groupPrivacy_tv;
    @BindView(R.id.group_members_tv)
    TextView group_membersNumber_tv;
    @BindView(R.id.edit_cover_btn)
    Button group_edit_cover_btn;
    @BindView(R.id.cover_image_im)
    ImageView cover_image;
    @BindView(R.id.edit_name_tv)
    EditText name_et;
    @BindView(R.id.group_img)
    ImageView pp_image;
    @BindView(R.id.admins_recyclerView)
    RecyclerView AdminsRecyclerView;
    @BindView(R.id.add_admin_et)
    EditText add_Admin;
    @BindView(R.id.addFriendBtn)
    Button addFriendsBtn;
    @BindView(R.id.closed_group)
    RadioButton privacyClosedBtn;
    @BindView(R.id.secret_group)
    RadioButton privacySecretBtn;
    @BindView(R.id.open_group)
    RadioButton privacyOpenBtn;
    @BindView(R.id.admin_add_member)
    RadioButton adminAddMember;
    @BindView(R.id.members_add_member)
    RadioButton membersAddMembers;
    @BindView(R.id.admins_post)
    RadioButton adminAddPost;
    @BindView(R.id.members_post)
    RadioButton membersAddPost;
    @BindView(R.id.everyone_post)
    RadioButton everyoneAddPost;
    @BindView(R.id.admin_event)
    RadioButton adminAddEvent;
    @BindView(R.id.members_event)
    RadioButton membersAddEvent;
    @BindView(R.id.submit_button)
    Button submitUpdates;
    @BindView(R.id.pp_image_edit)
    Button editPP;
    @BindView(R.id.edit_description_tv)
    EditText decription_et;
    @BindView(R.id.scroll)
    ScrollView scrollView;
    @BindView(R.id.admins_listView)
    ListView adminsListView;
    ExpandableRelativeLayout expandableLayout1, expandableLayout2, expandableLayout3, expandableLayout4, expandableLayout5, expandableLayout6, expandableLayout7;


    private String groupName;
    private String groupPrivacy;
    private String groupId;
    private int membersNumber;
    private Uri coverImageUri;
    private Uri ppImageUri;
    private final static String BACK_END_UPDATE_DATA = "https://rezetopia.com/Apis/groups/setting/update";
    private RequestQueue requestQueue;
    private Gson gson;
    private String eventSettings;
    private String postSettings;
    private String MembersSettings;
    private String privacySettings;
    private String groupDescriptionEdit;
    private String userName;
    private Bitmap coverBitmap;
    private Bitmap ppBitmap;
    private String addMember;
    private String addPost;
    private String addEvent;
    private String description;
    private String coverPhoto;
    private String profilePhoto;
    private AddAdminsListViewAdapter addAdminsListViewAdapter;
    private AddAdminRecyclerViewAdapter addAdminRecyclerViewAdapter;
    private List<Member> allMember = new ArrayList<>();
    private List<Member> memberResult = new ArrayList<>();
    private List<Member> selectedMembers = new ArrayList<>();
    private List<Admin>  groupAdmins = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        group_edit_cover_btn.setOnClickListener(this);
        submitUpdates.setOnClickListener(this);
        editPP.setOnClickListener(this);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            groupName = extras.getString("groupName");
            groupPrivacy = extras.getString("groupPrivacy");
            membersNumber = extras.getInt("membersNumber");
            groupId = extras.getString("groupId");
            userName = extras.getString("username");
            addPost = extras.getString("add_post");
            addMember = extras.getString("add_member");
            addEvent = extras.getString("add_event");
            description = extras.getString("description");
            coverPhoto =extras.getString("cover");
            profilePhoto = extras.getString("picture");
            allMember = extras.getParcelableArrayList("members");
            groupAdmins = extras.getParcelableArrayList("admins");
//            filterGroupMembers();
            setView();
        }

        // Volley Request and Gson
        requestQueue = Volley.newRequestQueue(this);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        add_Admin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                   getFilter().filter(String.valueOf(s.toString()));

                  //request admins
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

//    private void filterGroupMembers() {
//        for (int i = 0 ; i < groupAdmins.size(); i++){
//            for (int x=0 ; x< allMember.size(); x++){
//                if (groupAdmins.get(i).getId() == allMember.get(x).getId()){
//                    allMember.remove(x);
//                }
//            }
//        }
//    }

    private void setRecyclerView (){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        AdminsRecyclerView.setLayoutManager(gridLayoutManager);
        AdminsRecyclerView.setHasFixedSize(true);
        AdminsRecyclerView.setAdapter(addAdminRecyclerViewAdapter);
    }

    private void sendData(){

        StringRequest update = new StringRequest(Request.Method.POST, BACK_END_UPDATE_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response",response);
                Toast.makeText(GroupSettings.this, String.valueOf(response), Toast.LENGTH_LONG).show();
                Snackbar.make(scrollView, "updated", Snackbar.LENGTH_SHORT).show();
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(GroupSettings.this, String.valueOf(error), Toast.LENGTH_LONG).show();
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                        Snackbar.make(scrollView, errorMessage, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.retry, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        sendData();
                                    }
                                }).show();
                       // Toast.makeText(GroupSettings.this, errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                        Snackbar.make(scrollView, errorMessage, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.retry, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        sendData();
                                    }
                                }).show();
                      //  Toast.makeText(GroupSettings.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");
                        Log.e("Error Status", status);
                        Log.e("Error Message", message);
                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                            Snackbar.make(scrollView, errorMessage, Snackbar.LENGTH_INDEFINITE)
                                    .setAction(R.string.retry, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sendData();
                                        }
                                    }).show();
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                            Toast.makeText(GroupSettings.this, errorMessage, Toast.LENGTH_LONG).show();
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                            Toast.makeText(GroupSettings.this, errorMessage, Toast.LENGTH_LONG).show();
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                            Snackbar.make(scrollView, errorMessage, Snackbar.LENGTH_INDEFINITE)
                                    .setAction(R.string.retry, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sendData();
                                        }
                                    }).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                //params.put("id", groupId);
                params.put("id", String.valueOf(30));
                params.put("name", groupName);
                params.put("username", userName);
                params.put("privacy", groupPrivacy);
                params.put("add_member", addMember);
                params.put("add_post", addPost);
                params.put("add_event", addEvent);
                params.put("about",description);

                if (coverBitmap != null){
                    String cover = getStringImage(coverBitmap);
                    params.put("cover", cover);
                }else{
                    params.put("cover",coverPhoto);
                }

                if (ppBitmap != null){
                    String pp = getStringImage(ppBitmap);
                    params.put("picture", pp);
                }else{
                    params.put("picture",profilePhoto);
                }

                //converting ArrayList to String
                List<Member> adminsIds = new ArrayList<>();
                for (Member m : selectedMembers){
                    adminsIds.add(m);
                }
                String admins = gson.toJson(adminsIds);
                params.put("admins",admins);

                return params;
            }
        };
        requestQueue.add(update);
    }

    private void setView() {
        groupName_tv.setText(groupName);
        group_membersNumber_tv.setText(String.valueOf(membersNumber +" "+ getString(R.string.member)));
        decription_et.setText(description);
        name_et.setText(groupName);
        if (!coverPhoto.isEmpty()){
            Picasso.with(this).load(coverPhoto).into(cover_image);
        }
        if (!profilePhoto.isEmpty()){
            Picasso.with(this).load(profilePhoto).into(pp_image);
        }
        switch (groupPrivacy){
            case "open":
                privacyOpenBtn.setChecked(true);
                groupPrivacy_tv.setText(groupPrivacy);
                groupPrivacy_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_open_group_icon, 0, 0, 0);
                break;
            case "secret":
                privacySecretBtn.setChecked(true);
                groupPrivacy_tv.setText(groupPrivacy);
                groupPrivacy_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_secret_group_icon, 0, 0, 0);
                break;
            case "closed":
                privacyClosedBtn.setChecked(true);
                groupPrivacy_tv.setText(groupPrivacy);
                groupPrivacy_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_closed_group_icon, 0, 0, 0);
                break;
            default:
                privacyOpenBtn.setChecked(true);
                groupPrivacy_tv.setText(groupPrivacy);
                groupPrivacy_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_open_group_icon, 0, 0, 0);
                break;
        }
        switch (addEvent){
            case "only_admins":
                adminAddEvent.setChecked(true);
                break;
            case "members":
                membersAddEvent.setChecked(true);
                break;
            default:
                adminAddEvent.setChecked(true);
                break;
        }
        switch (addPost){
            case "only_admins":
                adminAddPost.setChecked(true);
                break;
            case "members":
                membersAddPost.setChecked(true);
                break;
            case "everyone":
                everyoneAddPost.setChecked(true);
                break;
            default:
                adminAddPost.setChecked(true);
                break;
        }
        switch (addMember){
            case "only_admins":
                adminAddMember.setChecked(true);
                break;
            case "members":
                membersAddMembers.setChecked(true);
                break;
            default:
                adminAddMember.setChecked(true);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        if (v == group_edit_cover_btn){
            openGalleryForCoverImage();
        }else if (v == submitUpdates){
            groupName = name_et.getText().toString();
            description = decription_et.getText().toString();
            sendData();
        }else if (v == editPP){
            openGalleryForPPImage();
        }
    }


    //Picking Cover Photo From gallery
    private void openGalleryForCoverImage() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery , PICK_COVER_IMAGE);
    }
    private void openGalleryForPPImage() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery , PICK_PP_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_COVER_IMAGE){
            coverImageUri = data.getData();
            cover_image.setImageURI(coverImageUri);
            try {
                coverBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), coverImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (resultCode == RESULT_OK && requestCode == PICK_PP_IMAGE){
            ppImageUri = data.getData();
            pp_image.setImageURI(ppImageUri);
            Toast.makeText(GroupSettings.this, ppImageUri.toString(), Toast.LENGTH_LONG).show();
            try {
                ppBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),ppImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void expandableButton1(View view) {
        expandableLayout1 = findViewById(R.id.expandableLayout1);
        expandableLayout1.toggle(); // toggle expand and collapse
        //view.setBackgroundColor();
    }

    public void expandableButton2(View view) {
        expandableLayout2 = findViewById(R.id.expandableLayout2);
        expandableLayout2.toggle(); // toggle expand and collapse
    }

    public void expandableButton3(View view) {
        expandableLayout3 = findViewById(R.id.expandableLayout3);
        expandableLayout3.toggle(); // toggle expand and collapse
    }

    public void expandableButton5(View view) {
        expandableLayout5 = findViewById(R.id.expandableLayout5);
        expandableLayout5.toggle();
    }

    public void expandableButton4(View view) {
        expandableLayout4 = findViewById(R.id.expandableLayout4);
        expandableLayout4.toggle();
    }

    public void expandableButton6(View view) {
        expandableLayout6 = findViewById(R.id.expandableLayout6);
        expandableLayout6.toggle();
    }

    public void expandableButton7(View view) {
        expandableLayout7 = findViewById(R.id.expandableLayout7);
        expandableLayout7.toggle();
    }



    public void checkEventSettingsRadioButton(View view) {
        if (view == adminAddEvent){
            eventSettings = "only_admins";
        }else if (view == membersAddEvent) {
            eventSettings = "members";
        }
    }

    public void checkPostSettingsRadioButton(View view) {
        if (view == adminAddPost){
            postSettings = "only_admins";
        }else if (view == membersAddPost){
            postSettings = "members";
        }else if (view == everyoneAddPost){
            postSettings = "everyone";
        }
    }

    public void checkAddMembersSettingsRadioButton(View view) {
        if (view == adminAddMember){
            MembersSettings = "only_admins";
        }else if (view == membersAddMembers){
            MembersSettings = "members";
        }
    }

    public void checkGroupPrivacyRadioButton(View view) {
        if (view == privacyClosedBtn){
            privacySettings = "closed";
        }else if (view == privacySecretBtn){
            privacySettings = "secret";
        }else if (view == privacyOpenBtn){
            privacySettings = "open";
        }
    }

    // convert Bitmap image to String for volley request
    public String getStringImage(Bitmap bitmap){
        Log.i("MyBitmap",""+bitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                memberResult = (List<Member>) results.values;
                addAdminsListViewAdapter = new AddAdminsListViewAdapter(GroupSettings.this, memberResult, new AddAdminsListViewAdapter.OnAdminChoosedListener() {
                    @Override
                    public void OnAdminChoosed(Member member) {
                        boolean founded= false;
                        for(Member m : selectedMembers){
                            if(m.getId() == member.getId()){
                                founded = true;
                                Toast.makeText(GroupSettings.this, member.getName() +"is selected", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (!founded){
                            selectedMembers.add(member);
                        }
                     addAdminRecyclerViewAdapter = new AddAdminRecyclerViewAdapter(selectedMembers, GroupSettings.this, new AddAdminRecyclerViewAdapter.OnItemCanceledListener() {
                         @Override
                         public void onItemCanceled(Member member) {
                                   List<Member> toRemove = new ArrayList<Member>();
                                   for(Member m : selectedMembers){
                                       if(m.getId() == member.getId()){
                                           toRemove.add(m);
                                        }
                                   }
                                   selectedMembers.removeAll(toRemove);
                         }
                     });
                     setRecyclerView();
                    }
                });
                adminsListView.setAdapter(addAdminsListViewAdapter);
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();     // Holds the results of a filtering operation in values
                ArrayList<Member> filteredArrayList = new ArrayList<>();

                if (allMember == null){
                    allMember = new ArrayList<Member>(memberResult);  // saves the original data in members
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the members(Original) values
                 *  else does the Filtering and returns filteredArrayList(Filtered)
                 *
                 ********/
               /* if (constraint == null || constraint.length() ==0){
                    filterResults.count = allMember.size();
                    filterResults.values = allMember;
                }else {
                    */
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < allMember.size() ; i++){
                        String data = allMember.get(i).getName();
                        if (data.toLowerCase().startsWith(constraint.toString())){
                            filteredArrayList.add(new Member(allMember.get(i).getName(), allMember.get(i).getId()));
                        }
                    }
                    filterResults.count = filteredArrayList.size();
                    filterResults.values = filteredArrayList;
            //    }
                return filterResults;
            }
        };
        return filter;
    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }
}
