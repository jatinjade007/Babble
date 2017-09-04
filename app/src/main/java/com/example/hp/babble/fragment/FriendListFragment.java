package com.example.hp.babble.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hp.babble.activity.ChatActivity;
import com.example.hp.babble.activity.ChatScreenActivity;
import com.example.hp.babble.activity.FriendSuggestionActivity;
import com.example.hp.babble.adapter.FriendListAdapter;
import com.example.hp.babble.apiConfiguration.ApiConfiguration;
import com.example.hp.babble.bean.ChatRow;
import com.example.hp.babble.R;
import com.example.hp.babble.httpRequestProcessor.HttpRequestProcessor;
import com.example.hp.babble.httpRequestProcessor.Response;
import com.example.hp.babble.sharedPreferences.LogIn_Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FriendListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG="FriendListFragment";
    private  FriendListAdapter friendListAdapter;
    private ListView listView;
    private ChatRow chatRow;
    private ArrayList<ChatRow> chatRowArrayList;
    private HttpRequestProcessor httpRequestProcessor;
    private Response response;
    private ApiConfiguration apiConfiguration;
    private String baseURL, urlUser, jsonStringToPost, jsonResponseString;
    private boolean success;
    private String message,memberId;
    private SharedPreferences sharedPreferences;
    private String applicationFriendAssociationId,friendName,friendId;
    private Bundle bundle;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout refreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_friend_list, container, false);
        listView= (ListView) view.findViewById(R.id.friendList);
        refreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshFriendList);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Execute AsyncTask
                new FriendListTask().execute();
            }
        });
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,android.R.color.holo_green_light,android.R.color.holo_orange_light,android.R.color.holo_red_light);


        chatRowArrayList=new ArrayList<ChatRow>();
        //initialization

        httpRequestProcessor = new HttpRequestProcessor();
        response = new Response();
        apiConfiguration = new ApiConfiguration();

        sharedPreferences = getActivity().getSharedPreferences(LogIn_Pref.Pref_Name, MODE_PRIVATE);
        memberId = sharedPreferences.getString(LogIn_Pref.ApplicationUserId, null);

        //Getting BAseURL
        baseURL = apiConfiguration.getApi();
        urlUser = baseURL + "ApplicationFriendAPI/MyFriendList/"+memberId;


        new FriendListTask().execute();
        friendListAdapter=new FriendListAdapter(getActivity(),chatRowArrayList);
        listView.setOnItemClickListener(this);

        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),ChatActivity.class);

                //Create the bundle
                bundle = new Bundle();
                //Add your data to bundle
                bundle.putString("friendID",friendId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        */
        listView.setAdapter(friendListAdapter);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        chatRow=chatRowArrayList.get(position);
        friendName=chatRow.getFriendName();
        friendId=chatRow.getFriendId();
        Toast.makeText(getActivity(), friendName+"selected", Toast.LENGTH_SHORT).show();
       Intent intent=new Intent(getActivity().getApplicationContext(),ChatScreenActivity.class);
        //Intent intent=new Intent(getActivity().getApplicationContext(),ChatActivity.class);
        intent.putExtra("friendID",friendId);
        intent.putExtra("friendName",friendName);
        startActivity(intent);


    }


    //Getting Friend List
    public class FriendListTask extends AsyncTask<String,String,String >{


        /*
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("Loging In...");
            progressDialog.show();
        }
        */



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
                JSONObject object = new JSONObject(s);
                success = object.getBoolean("success");
                if (success) {
                    if (chatRowArrayList != null)
                        chatRowArrayList.clear();


                    refreshLayout.setRefreshing(false);

                    message = object.getString("message");
                    JSONArray responseData = object.getJSONArray("responseData");
                    for (int i = 0; i < responseData.length(); i++) {
                        JSONObject object1 = (JSONObject) responseData.get(i);
                        applicationFriendAssociationId=object1.getString("ApplicationFriendAssociationId");
                        friendName = object1.getString("FriendName");
                        Log.d("Name", friendName);
                        friendId=object1.getString("FriendId");
                        Log.d("friendId ",friendId);


                        chatRow = new ChatRow(friendName,friendId);
                        chatRowArrayList.add(chatRow);
                    }
                    friendListAdapter.notifyDataSetChanged();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
