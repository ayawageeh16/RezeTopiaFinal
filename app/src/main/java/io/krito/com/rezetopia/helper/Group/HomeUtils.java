package io.krito.com.rezetopia.helper.Group;

import android.os.AsyncTask;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import io.krito.com.rezetopia.models.pojo.Group.GroupPost;

public class HomeUtils {

    private static NewsFeedCallback feedCallback;

    public static void setFeedCallback(NewsFeedCallback call) {
        feedCallback = call;
    }

    public static class fetchHomeData extends AsyncTask<Void,Void,Void>{

        private String BACK_END = "https://rezetopia.com/Apis/groups/get";
        private RequestQueue requestQueue;
        public StringRequest stringRequest;
        private Gson gson;
        private String cursor = "0";
        private GroupPost groupPost = new GroupPost();
        private String id= "30";
        private static NewsFeedCallback feedCallback;

        public static void setFeedCallback(NewsFeedCallback call) {
            feedCallback = call;
        }

        @Override
        protected Void doInBackground(Void... strings) {

            stringRequest = new StringRequest(Request.Method.POST, BACK_END, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    groupPost = gson.fromJson(response, GroupPost.class);
                    if (groupPost !=null){
                    }else {
                        //Toast.makeText(Group.this, "null object", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                  //  Toast.makeText(Group.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("id", id);
                    params.put("cursor", cursor);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
            return null;
        }
    }

    public interface NewsFeedCallback{
        void onSuccess(GroupPost groupPost);
        void onError(int error);
    }
}
