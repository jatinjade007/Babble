package com.example.hp.babble.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hp.babble.R;
import com.example.hp.babble.adapter.SendrequestAdapter;
import com.example.hp.babble.apiConfiguration.ApiConfiguration;
import com.example.hp.babble.bean.RequestRow;
import com.example.hp.babble.bean.UserBean;
import com.example.hp.babble.httpRequestProcessor.HttpRequestProcessor;
import com.example.hp.babble.httpRequestProcessor.Response;
import com.example.hp.babble.sharedPreferences.LogIn_Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendSuggestionActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_suggestion);

        //findViewById
        suggestionList = (ListView) findViewById(R.id.suggestionList);
        suggestionToolbar = (Toolbar) findViewById(R.id.suggestiontoolbar);
        setSupportActionBar(suggestionToolbar);
        //suggestionToolbar.setTitle("Make New Friends");
        //TO Set Tittle of Toolbar
        getSupportActionBar().setTitle("Make New Friends");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


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
        searchView= (SearchView) findViewById(R.id.searchUser);

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

        sendrequestAdapter = new SendrequestAdapter(FriendSuggestionActivity.this, suggestionRowArrayList);
        suggestionList.setAdapter(sendrequestAdapter);

        /*suggestionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(FriendSuggestionActivity.this,"Button clicked for position "+ position,Toast.LENGTH_LONG).show();
            }
        });*/


    }
    //To go back on Previous Screen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Getting users list
    public class UserListTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(FriendSuggestionActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            jsonResponseString = httpRequestProcessor.gETRequestProcessor(urlUser);
            return jsonResponseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("str", s);
            sharedPreferences = getSharedPreferences(LogIn_Pref.Pref_Name, Context.MODE_PRIVATE);
            memberId = sharedPreferences.getString(LogIn_Pref.ApplicationUserId, null);

            try {
                JSONObject object = new JSONObject(s);
                success = object.getBoolean("success");
                // int id = object.getInt("ApplicationUserId");
                // String id1 = String.valueOf(id);

                if (success) {
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
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                    sendrequestAdapter.notifyDataSetChanged();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


}
