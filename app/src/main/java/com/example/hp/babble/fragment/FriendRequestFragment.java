package com.example.hp.babble.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.babble.R;
import com.example.hp.babble.apiConfiguration.ApiConfiguration;
import com.example.hp.babble.bean.RequestRow;
import com.example.hp.babble.adapter.RequestAdapter;
import com.example.hp.babble.httpRequestProcessor.HttpRequestProcessor;
import com.example.hp.babble.httpRequestProcessor.Response;
import com.example.hp.babble.sharedPreferences.ActionOnRequest_Pref;
import com.example.hp.babble.sharedPreferences.LogIn_Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FriendRequestFragment extends Fragment {


    private SharedPreferences sharedPreferences;
    private static final String TAG="FriendRequestFragment";
    private ListView listView;
    private RequestRow requestRow;
    private ArrayList<RequestRow> requestRowArrayList;
    private HttpRequestProcessor httpRequestProcessor;
    private Response response;
    private ApiConfiguration apiConfiguration;
    private String baseURL, urlUser, jsonStringToPost, jsonResponseString;
    private boolean success;
    private String message;
    private String memberId,friendId,memberName,friendName;
    private RequestAdapter requestAdapter;
    private int image;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private String applicationFriendAssociationId,emailId;
    private SwipeRefreshLayout refreshLayout;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_friend_request, container, false);
        listView= (ListView) view.findViewById(R.id.friendRequestList);
        refreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshRequest);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Execute AsyncTask
                new MyFriendRequestTask().execute();
            }
        });
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,android.R.color.holo_green_light,android.R.color.holo_orange_light,android.R.color.holo_red_light);



        httpRequestProcessor = new HttpRequestProcessor();
        response = new Response();
        apiConfiguration = new ApiConfiguration();

        sharedPreferences = getActivity().getSharedPreferences(LogIn_Pref.Pref_Name, MODE_PRIVATE);
        memberId = sharedPreferences.getString(LogIn_Pref.ApplicationUserId, null);

        baseURL = apiConfiguration.getApi();
        urlUser = baseURL + "ApplicationFriendAPI/MyFriendRequest/"+memberId;


       //image=R.drawable.image;
        requestRowArrayList=new ArrayList<RequestRow>();
        new MyFriendRequestTask().execute();
        requestAdapter=new RequestAdapter(getActivity(),requestRowArrayList);
        listView.setAdapter(requestAdapter);

        return view;
    }
    public class MyFriendRequestTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            jsonResponseString = httpRequestProcessor.gETRequestProcessor(urlUser);
            return jsonResponseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("str", s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                success=jsonObject.getBoolean("success");

                if (success){
                    if (requestRowArrayList != null)
                        requestRowArrayList.clear();


                    refreshLayout.setRefreshing(false);
                    message=jsonObject.getString("message");

                    JSONArray responseData = jsonObject.getJSONArray("responseData");
                    for (int i = 0; i < responseData.length(); i++) {
                        JSONObject object = (JSONObject) responseData.get(i);
                        applicationFriendAssociationId=object.getString("ApplicationFriendAssociationId");
  //                      Log.d("ApplicationFriendAssociationId ", applicationFriendAssociationId);

                        memberId=object.getString("FriendId");
                        Log.d("FriendId ",memberId);

                        friendId=object.getString("MemberId");
                        Log.d("MemberId ", friendId);

                        memberName=object.getString("MemberName");
                        Log.d("MemberName ",memberName);

                        friendName=object.getString("FriendName");
                        Log.d("FriendName ",friendName);

                        //SharedPreferences
                        sharedPreferences = getActivity().getSharedPreferences(ActionOnRequest_Pref.Pref_Name, MODE_PRIVATE);
                        sharedPreferencesEditor = sharedPreferences.edit();
                        sharedPreferencesEditor.putString(ActionOnRequest_Pref.ApplicationFriendAssociationId, applicationFriendAssociationId);
                        sharedPreferencesEditor.putString(ActionOnRequest_Pref.EmailId, emailId);
                        sharedPreferencesEditor.commit();

                        requestRow=new RequestRow(friendName);
                        requestRowArrayList.add(requestRow);
                        requestAdapter.notifyDataSetChanged();


                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }
}
