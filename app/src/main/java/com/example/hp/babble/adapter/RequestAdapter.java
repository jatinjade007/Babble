package com.example.hp.babble.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.babble.R;
import com.example.hp.babble.apiConfiguration.ApiConfiguration;
import com.example.hp.babble.bean.RequestRow;
import com.example.hp.babble.httpRequestProcessor.HttpRequestProcessor;
import com.example.hp.babble.httpRequestProcessor.Response;
import com.example.hp.babble.sharedPreferences.ActionOnRequest_Pref;
import com.example.hp.babble.sharedPreferences.LogIn_Pref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by HP on 4/10/2017.
 */

public class RequestAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private ArrayList<RequestRow> requestRowArrayList;
    private LayoutInflater layoutInflater;
    private HttpRequestProcessor httpRequestProcessor;
    private Response response;
    private ApiConfiguration apiConfiguration;
    private String suggestName, message;
    private String applicationFriendAssociationId,status;
    private String baseURL, urlUser, jsonStringToPost, jsonResponseString;
    private boolean success;
    private int responseData;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    public RequestAdapter(Context context, ArrayList<RequestRow> requestRowArrayList) {
        this.context = context;
        this.requestRowArrayList = requestRowArrayList;
    }

    @Override
    public int getCount() {
        return requestRowArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return requestRowArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view=layoutInflater.inflate(R.layout.request_row,viewGroup,false);
        //Getting All The View Inside XML
        TextView txtRequestname= (TextView) view.findViewById(R.id.txtRequestName);
        Button btnAccept= (Button) view.findViewById(R.id.btnAccept);
        Button btnReject= (Button) view.findViewById(R.id.btnReject);
        ImageView imageView= (ImageView) view.findViewById(R.id.images);

        httpRequestProcessor = new HttpRequestProcessor();
        response = new Response();
        apiConfiguration = new ApiConfiguration();

        baseURL = apiConfiguration.getApi();
        urlUser = baseURL + "ApplicationFriendAPI/ActionOnFriendRequest";

        //Getting Data
        RequestRow requestRow=requestRowArrayList.get(i);
        String requestName=requestRow.getFriendName();
      //  int images=requestRow.getImages();
        //Loading Views with Corresponding data
        txtRequestname.setText(requestName);
     //   imageView.setImageResource(images);

        btnAccept.setOnClickListener(this);
        btnReject.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAccept:
                //Retrieve Data from SharedPreference
                sharedPreferences = context.getSharedPreferences(ActionOnRequest_Pref.Pref_Name, Context.MODE_PRIVATE);
                applicationFriendAssociationId = sharedPreferences.getString(ActionOnRequest_Pref.ApplicationFriendAssociationId, null);


                status="Accept";
                new ActionOnFriendRequestTask().execute(applicationFriendAssociationId,status);
                break;
            case R.id.btnReject:
                //Retrieve Data from SharedPreference
                sharedPreferences = context.getSharedPreferences(ActionOnRequest_Pref.Pref_Name, Context.MODE_PRIVATE);
                applicationFriendAssociationId = sharedPreferences.getString(ActionOnRequest_Pref.ApplicationFriendAssociationId, null);

                status="Reject";
                new ActionOnFriendRequestTask().execute(applicationFriendAssociationId,status);
                break;
        }
    }
    public class ActionOnFriendRequestTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            applicationFriendAssociationId=params[0];
         //   Log.d("ApplicationFriendAssociationId ",applicationFriendAssociationId);
            status=params[1];
            Log.d("Status ",status);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ApplicationFriendAssociationId",applicationFriendAssociationId);
                jsonObject.put("Status",status);

                jsonStringToPost = jsonObject.toString();
                response = httpRequestProcessor.postRequestProcessor(jsonStringToPost, urlUser);
                jsonResponseString = response.getJsonResponseString();

//                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonResponseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject=new JSONObject(s);
                success = jsonObject.getBoolean("success");
                Log.d("success", String.valueOf(success));
                message = jsonObject.getString("message");
                Log.d("message", message);
                if (success) {

                    responseData = jsonObject.getInt("responseData");
                    Log.d("responseData", String.valueOf(responseData));


                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();



                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
