package com.example.hp.babble.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.babble.R;
import com.example.hp.babble.activity.ChatTabs;
import com.example.hp.babble.activity.LogIn;
import com.example.hp.babble.apiConfiguration.ApiConfiguration;
import com.example.hp.babble.bean.RequestRow;
import com.example.hp.babble.bean.UserBean;
import com.example.hp.babble.httpRequestProcessor.HttpRequestProcessor;
import com.example.hp.babble.httpRequestProcessor.Response;
import com.example.hp.babble.sharedPreferences.LogIn_Pref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by HP on 4/26/2017.
 */

public class SendrequestAdapter extends BaseAdapter implements View.OnClickListener,Filterable {
    private static final String TAG = "SendRequest";
    private Context context;
    private SharedPreferences sharedPreferences;
    private ArrayList<UserBean> userBeanArrayList,originalList;
    private UserBean userBean;
    private LayoutInflater layoutInflater;
    private HttpRequestProcessor httpRequestProcessor;
    private Response response;
    private ApiConfiguration apiConfiguration;
    private String suggestName, message;
    private boolean success;
    private int responseData;
    private String memberId, friendId, requestBy, modifiedBy;
    private String baseURL, urlUser, jsonStringToPost, jsonResponseString;
    private Button btnAddAsFriend;
    private ValueFilter valueFilter;

    public SendrequestAdapter(Context context, ArrayList<UserBean> userBeanArrayList) {
        this.context = context;
        this.userBeanArrayList = userBeanArrayList;
        this.originalList=userBeanArrayList;
    }

    @Override
    public int getCount() {
        return userBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return userBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Log.d(TAG, "getView: ");
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.send_request_row, viewGroup, false);
        //Getting All The View Inside XML
        TextView txtSuggestName = (TextView) view.findViewById(R.id.txtSuggestName);
        btnAddAsFriend = (Button) view.findViewById(R.id.btnAddAsFriend);
        ImageView imageView = (ImageView) view.findViewById(R.id.images);
        //Getting Data
        userBean = userBeanArrayList.get(position);
        suggestName = userBean.getName();
        Log.d(suggestName, "Name ");
        friendId = userBean.getUserId();
        Log.d(friendId, "FriendId ");


        httpRequestProcessor = new HttpRequestProcessor();
        response = new Response();
        apiConfiguration = new ApiConfiguration();

        //int images=userBean.getImages();
        //Loading Views with Corresponding data
        txtSuggestName.setText(suggestName);
        //imageView.setImageResource(images);
        btnAddAsFriend.setText("Add As Friend");
        //SetTag of Button
        btnAddAsFriend.setTag(friendId);

        btnAddAsFriend.setOnClickListener(this);
    /*    btnAddAsFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");


                //Retrieve Data from SharedPreference
                sharedPreferences = context.getSharedPreferences("LogIn_Pref", Context.MODE_PRIVATE);
                memberId = sharedPreferences.getString("MemberId", null);
                requestBy = sharedPreferences.getString("MemberId", null);
                modifiedBy = sharedPreferences.getString("MemberId", null);
                friendId = String.valueOf(userBean.getUserId());
                Log.d(friendId, "FriendId ");

                baseURL = apiConfiguration.getApi();
                urlUser = baseURL + "ApplicationFriendAPI/AddFriendRequest/";


                new AddFriendTask(v).execute(memberId, friendId, requestBy, modifiedBy);
                if (btnAddAsFriend.getText().toString().equals("Add As Friend")){
                    btnAddAsFriend.setText("RequestSent");
                }
            }
        });

*/
/*
        btnAddAsFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_LONG).show();

                //Retrieve Data from SharedPreference
                sharedPreferences = context.getSharedPreferences(LogIn_Pref.Pref_Name, Context.MODE_PRIVATE);
                memberId = sharedPreferences.getString(LogIn_Pref.ApplicationUserId, null);
                Log.d("memberID ", memberId);
                requestBy = sharedPreferences.getString(LogIn_Pref.ApplicationUserId, null);
                modifiedBy = sharedPreferences.getString(LogIn_Pref.ApplicationUserId, null);
                // friendId = userBean.getUserId();
                friendId = (String) v.getTag();
                Log.d("FriendId1", friendId);

                baseURL = apiConfiguration.getApi();
                urlUser = baseURL + "ApplicationFriendAPI/AddFriendRequest";
                // btnAddAsFriend.setText("Request Send");
                //btnAddAsFriend.setVisibility(v.INVISIBLE);
                new AddFriendTask(v).execute(memberId, friendId, requestBy, modifiedBy);
            }
        });
*/
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddAsFriend:
                Toast.makeText(context, "Clicked", Toast.LENGTH_LONG).show();

                //Retrieve Data from SharedPreference
                sharedPreferences = context.getSharedPreferences(LogIn_Pref.Pref_Name, Context.MODE_PRIVATE);
                memberId = sharedPreferences.getString(LogIn_Pref.ApplicationUserId, null);
                Log.d("memberID ", memberId);
                requestBy = sharedPreferences.getString(LogIn_Pref.ApplicationUserId, null);
                modifiedBy = sharedPreferences.getString(LogIn_Pref.ApplicationUserId, null);
                // friendId = userBean.getUserId();
                friendId = (String) v.getTag();

                Log.d(friendId, "FriendId ");

                baseURL = apiConfiguration.getApi();
                urlUser = baseURL + "ApplicationFriendAPI/AddFriendRequest";
                // btnAddAsFriend.setText("Request Send");
                //btnAddAsFriend.setVisibility(v.INVISIBLE);
                new AddFriendTask(v).execute(memberId, friendId, requestBy, modifiedBy);
                break;
        }

    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }
    private class ValueFilter extends android.widget.Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0){
                ArrayList<UserBean> tempArrayList=new ArrayList<UserBean>();
                for (int i = 0; i < originalList.size(); i++) {
                    if ((originalList.get(i).getName().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        tempArrayList.add(originalList.get(i));
                    }
                }
                results.count = tempArrayList.size();
                results.values = tempArrayList;
            }
            else{
                results.count = originalList.size();
                results.values =originalList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            userBeanArrayList = (ArrayList<UserBean>) results.values;
            notifyDataSetChanged();
        }
    }


    public class AddFriendTask extends AsyncTask<String, String, String> {
        private View v;

        public AddFriendTask(View v) {
            this.v = v;
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "doInBackground: ");
            memberId = params[0];
            Log.d("memberId ", memberId);
            friendId = params[1];
            Log.d("friendID", friendId);
            requestBy = params[2];
            modifiedBy = params[3];
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("MemberId", memberId);
                jsonObject.put("FriendId", friendId);
                jsonObject.put("RequestBy", requestBy);
                jsonObject.put("ModifiedBy", modifiedBy);

                jsonStringToPost = jsonObject.toString();
                response = httpRequestProcessor.postRequestProcessor(jsonStringToPost, urlUser);
                jsonResponseString = response.getJsonResponseString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonResponseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: ");
            Log.d("str ", s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                success = jsonObject.getBoolean("success");
                Log.d("success", String.valueOf(success));
                message = jsonObject.getString("message");
                Log.d("message", message);


                if (success) {

                    responseData = jsonObject.getInt("responseData");
                    Log.d("responseData", String.valueOf(responseData));


                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                    ((Button) v).setText("Request Sent");


                } else {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            notifyDataSetChanged();


        }


    }


}
