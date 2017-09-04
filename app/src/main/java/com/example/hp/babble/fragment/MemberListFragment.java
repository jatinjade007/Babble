package com.example.hp.babble.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.hp.babble.R;
import com.example.hp.babble.activity.FriendSuggestionActivity;
import com.example.hp.babble.adapter.SendrequestAdapter;
import com.example.hp.babble.apiConfiguration.ApiConfiguration;
import com.example.hp.babble.bean.UserBean;
import com.example.hp.babble.httpRequestProcessor.HttpRequestProcessor;
import com.example.hp.babble.httpRequestProcessor.Response;
import com.example.hp.babble.sharedPreferences.LogIn_Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemberListFragment extends Fragment {
    private HttpRequestProcessor httpRequestProcessor;
    private Response response;
    private ApiConfiguration apiConfiguration;
    private ListView suggestionList;
    private String baseURL, urlUser, jsonStringToPost, jsonResponseString;
    private UserBean suggestionRow;
    private ArrayList<UserBean> suggestionRowArrayList;
    private boolean success;
    private String message, name, emailId, memberId;
    private int image;
    private Toolbar suggestionToolbar;
    private SendrequestAdapter sendrequestAdapter;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private SearchView searchView;
    private SwipeRefreshLayout refreshLayout;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user_list, container, false);
        suggestionList= (ListView) view.findViewById(R.id.allmemberlist);

        //initialization

        httpRequestProcessor = new HttpRequestProcessor();
        response = new Response();
        apiConfiguration = new ApiConfiguration();
        suggestionRowArrayList = new ArrayList<UserBean>();

        //Getting BAseURL
        baseURL = apiConfiguration.getApi();
        urlUser = baseURL + "ApplicationFriendAPI/GetApplicationMemberList";
        new UserListTask().execute();

        //SearchBox
        searchView= (SearchView) view.findViewById(R.id.searchUser1);
        //Swipe Refresh Layout
        refreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.refreshUserList);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Execute AsyncTask
                new UserListTask().execute();
            }
        });
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,android.R.color.holo_green_light,android.R.color.holo_orange_light,android.R.color.holo_red_light);


        //Search Query
        searchView.setActivated(true);
        searchView.setQueryHint("Enter Search Text");
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sendrequestAdapter.getFilter().filter(newText);
                /*
                if(newText.length()>3){
                    suggestionList.setVisibility(View.VISIBLE);
                    new UserTask().execute(newText);

                } else {
                    suggestionList.setVisibility(View.INVISIBLE);
                }
                */
                return false;
            }
        });


        sendrequestAdapter = new SendrequestAdapter(getActivity(), suggestionRowArrayList);
        suggestionList.setAdapter(sendrequestAdapter);


        return view;
    }


    //Getting users list
    public class UserListTask extends AsyncTask<String, String, String> {

        /*
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
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
            sharedPreferences = getActivity().getSharedPreferences(LogIn_Pref.Pref_Name, Context.MODE_PRIVATE);
            memberId = sharedPreferences.getString(LogIn_Pref.ApplicationUserId, null);

            try {
                JSONObject object = new JSONObject(s);
                success = object.getBoolean("success");
                // int id = object.getInt("ApplicationUserId");
                // String id1 = String.valueOf(id);

                if (success) {
                    if (suggestionRowArrayList != null)
                        suggestionRowArrayList.clear();


                    refreshLayout.setRefreshing(false);
                    message = object.getString("message");
                    JSONArray responseData = object.getJSONArray("responseData");
                    for (int i = 0; i < responseData.length(); i++) {
                        JSONObject object1 = (JSONObject) responseData.get(i);
                        name = object1.getString("Name");
                        Log.d("Name", name);
                        // String mID = object1.getString("MemberId");
                        // Log.d("MemberId", mID);
                        emailId = object1.getString("EmailId");
                        Log.d("EmailId", emailId);
                        int id = object1.getInt("MemberId");
                        int mID = Integer.parseInt(memberId);
                        String id1 = String.valueOf(id);
                        if (id != mID) {
                            suggestionRow = new UserBean(name,String.valueOf(id) , emailId);
                            suggestionRowArrayList.add(suggestionRow);
                        }
                        /*
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        */
                    }
                    sendrequestAdapter.notifyDataSetChanged();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


}
