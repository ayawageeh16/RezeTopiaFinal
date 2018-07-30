package io.krito.com.rezetopia.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.application.AppConfig;
import io.krito.com.rezetopia.helper.Group.AddAdminRecyclerViewAdapter;
import io.krito.com.rezetopia.helper.Group.AddAdminsListViewAdapter;
import io.krito.com.rezetopia.helper.Group.AddMemberListViewAdapter;
import io.krito.com.rezetopia.helper.Group.AddMemberRecyclerViewAdapter;
import io.krito.com.rezetopia.models.pojo.Group.Member;

public class GroupAddMember extends AppCompatActivity implements View.OnClickListener, Filterable {

    @BindView(R.id.home_btn)
    Button home;
    @BindView(R.id.description_btn)
    Button description;
    @BindView(R.id.recyclerview_members)
    RecyclerView recyclerView;
    @BindView(R.id.addFriendBtn)
    Button add;
    @BindView(R.id.group_name_tv_member)
    TextView groupName_tv;
    @BindView(R.id.group_privacy_tv_member)
    TextView groupPrivacy_tv;
    @BindView(R.id.group_members_number_tv)
    TextView groupMembers_tv;
    @BindView(R.id.settings_icon_member)
    ImageView settings;
    @BindView(R.id.add_member_listView)
    ListView addMemberListView;
    @BindView(R.id.add_friend_et)
    EditText member_name_et;


    int membersNumber ;
    private String groupId;
    private String groupPrivacy;
    private String groupName;
    private String groupDescription;
    private String BACK_END = "https://rezetopia.com/Apis/groups/members/add";
    private AddAdminsListViewAdapter addAdminsListViewAdapter;
    private AddAdminRecyclerViewAdapter addAdminRecyclerViewAdapter;
    private List<Member> allUsers = new ArrayList<>();
    private List<Member> UsersResult = new ArrayList<>();
    private List<Member> selectedUsers = new ArrayList<>();
    private List<Member> groupMembers = new ArrayList<>();
    private RequestQueue requestQueue;
    private Gson gson;
    private String userId;
    private String searchName;
    private AddMemberListViewAdapter addMemberListViewAdapter;
    private AddMemberRecyclerViewAdapter addMemberRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add_member);
        ButterKnife.bind(this);

        home.setOnClickListener(this);
        description.setOnClickListener(this);
        add.setOnClickListener(this);
        settings.setOnClickListener(this);

        userId = getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, null);

        Toast.makeText(GroupAddMember.this, userId, Toast.LENGTH_LONG).show();

        // Volley Request and Gson
        requestQueue = Volley.newRequestQueue(this);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            membersNumber = extras.getInt("membersNumber");
            groupId = extras.getString("groupId");
            groupPrivacy = extras.getString("groupPrivacy");
            groupName = extras.getString("groupName");
            groupDescription = extras.getString("description");
            groupMembers = extras.getParcelableArrayList("members");
            filterGroupMembers();
        }
        
        member_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchName = charSequence.toString();
                getUsers();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        setView();
    }
    private void getUsers(){

        StringRequest update = new StringRequest(Request.Method.POST, BACK_END, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response",response);
                Toast.makeText(GroupAddMember.this, String.valueOf(response), Toast.LENGTH_LONG).show();
                //Snackbar.make(scrollView, "updated", Snackbar.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(GroupAddMember.this, String.valueOf(error), Toast.LENGTH_LONG).show();
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
//                        Snackbar.make(scrollView, errorMessage, Snackbar.LENGTH_INDEFINITE)
//                                .setAction(R.string.retry, new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        sendData();
//                                    }
//                                }).show();
                        // Toast.makeText(GroupSettings.this, errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
//                        Snackbar.make(scrollView, errorMessage, Snackbar.LENGTH_INDEFINITE)
//                                .setAction(R.string.retry, new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        sendData();
//                                    }
//                                }).show();
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
//                            Snackbar.make(scrollView, errorMessage, Snackbar.LENGTH_INDEFINITE)
//                                    .setAction(R.string.retry, new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            sendData();
//                                        }
//                                    }).show();
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                            Toast.makeText(GroupAddMember.this, errorMessage, Toast.LENGTH_LONG).show();
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                            Toast.makeText(GroupAddMember.this, errorMessage, Toast.LENGTH_LONG).show();
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
//                            Snackbar.make(scrollView, errorMessage, Snackbar.LENGTH_INDEFINITE)
//                                    .setAction(R.string.retry, new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            sendData();
//                                        }
//                                    }).show();
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
                params.put("group_id", String.valueOf(30));
                params.put("userId", userId);
                params.put("searchname", searchName);

                return params;
            }
        };
        requestQueue.add(update);
    }

    private void filterGroupMembers() {
        for (int i = 0 ; i < groupMembers.size(); i++){
            for (int x=0 ; x< allUsers.size(); x++){
                if (groupMembers.get(i).getId() == allUsers.get(x).getId()){
                    allUsers.remove(x);
                }
            }
        }
    }

    private void setView() {
        groupName_tv.setText(this.groupName);
        groupPrivacy_tv.setText(this.groupPrivacy);
        groupMembers_tv.setText(String.valueOf(this.membersNumber)+ " " +getString(R.string.member));
    }

    @Override
    public void onClick(View v) {
        if (v == home){
            Intent intent = new Intent(GroupAddMember.this, Group.class);
            startActivity(intent);
        }else if (v == description){
            Intent intent = new Intent(GroupAddMember.this, GroupDescription.class);
            intent.putExtra("description",groupDescription);
            intent.putExtra("groupName",groupName);
            intent.putExtra("membersNumber", membersNumber);
            intent.putExtra("groupPrivacy", groupPrivacy);
            intent.putExtra("groupId",groupId);
            startActivity(intent);
        }else if (v == add){

        }else if (v == settings){
            Intent intent = new Intent(GroupAddMember.this, GroupSettings.class);
            intent.putExtra("groupName",groupName);
            intent.putExtra("membersNumber", membersNumber);
            intent.putExtra("groupPrivacy", groupPrivacy);
            intent.putExtra("groupId",groupId);
            startActivity(intent);
        }
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                UsersResult = (List<Member>) results.values;
                addAdminsListViewAdapter = new AddAdminsListViewAdapter(GroupAddMember.this, UsersResult, new AddAdminsListViewAdapter.OnAdminChoosedListener() {
                    @Override
                    public void OnAdminChoosed(Member member) {

                        selectedUsers.add(member);
                        addAdminRecyclerViewAdapter = new AddAdminRecyclerViewAdapter(selectedUsers, GroupAddMember.this, new AddAdminRecyclerViewAdapter.OnItemCanceledListener() {
                            @Override
                            public void onItemCanceled(Member member) {
                                List<Member> toRemove = new ArrayList<Member>();
                                for(Member m : selectedUsers){
                                    if(m.getId() == member.getId()){
                                        toRemove.add(m);
                                    }
                                }
                                selectedUsers.removeAll(toRemove);
                            }
                        });
                        setRecyclerView();
                    }
                });
                addMemberListView.setAdapter(addAdminsListViewAdapter);
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();     // Holds the results of a filtering operation in values
                ArrayList<Member> filteredArrayList = new ArrayList<>();

                if (allUsers == null){
                    allUsers = new ArrayList<Member>(UsersResult);  // saves the original data in members
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the members(Original) values
                 *  else does the Filtering and returns filteredArrayList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() ==0){
                    filterResults.count = allUsers.size();
                    filterResults.values = allUsers;
                }else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < allUsers.size() ; i++){
                        String data = allUsers.get(i).getName();
                        if (data.toLowerCase().startsWith(constraint.toString())){
                            filteredArrayList.add(new Member(allUsers.get(i).getName(), allUsers.get(i).getId()));
                        }
                    }
                    filterResults.count = filteredArrayList.size();
                    filterResults.values = filteredArrayList;
                }
                return filterResults;
            }
        };
        return filter;    
    }
    private void setRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(addMemberRecyclerViewAdapter);
    }
}
