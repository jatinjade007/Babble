package com.example.hp.babble.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.babble.R;
import com.example.hp.babble.adapter.Adapter_message;
import com.example.hp.babble.apiConfiguration.ApiConfiguration;
import com.example.hp.babble.bean.ChatRow;
import com.example.hp.babble.bean.Message;
import com.example.hp.babble.httpRequestProcessor.HttpRequestProcessor;
import com.example.hp.babble.httpRequestProcessor.Response;
import com.example.hp.babble.sharedPreferences.LogIn_Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by admin on 4/21/2017.
 */

public class ChatActivity extends AppCompatActivity {

    private TextView txtName, txtBack, txtStatus;
    private ImageView imageView3;
    private Button btnRefresh, btnSend;
    private ListView lv;
    private EditText edtMessage;
    private ArrayList<ChatRow> chatRowArrayList;
    private ChatRow chatRow;
    private String senderId, receiverId, senderName, messages;
    private HttpRequestProcessor httpRequestProcessor;
    private Response response;
    private ApiConfiguration apiConfiguration;
    private String baseURL, urlLoadMessage, urlSubmitMessage, jsonStringToPost, jsonResponseString, jsonResponse, loggedInUserID, message, sentmessage;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int responseData;
    private Boolean isLoggedInUserId, success;
    private String friendID, friendName;
    private Message m;
    private ArrayList<Message> messageArrayList;
    private Context context;
    private Adapter_message adapter_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        txtBack = (TextView) findViewById(R.id.txtBack);
        txtName = (TextView) findViewById(R.id.txtName);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        btnSend = (Button) findViewById(R.id.btnSend);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        lv = (ListView) findViewById(R.id.lvMessage);
        //Initialization
        httpRequestProcessor = new HttpRequestProcessor();
        response = new Response();
        apiConfiguration = new ApiConfiguration();


        sharedPreferences = ChatActivity.this.getSharedPreferences(LogIn_Pref.Pref_Name, Context.MODE_PRIVATE);
        loggedInUserID = sharedPreferences.getString(LogIn_Pref.ApplicationUserId, null);


        //Getting FriendID from FriendList Screen
        friendName = getIntent().getExtras().getString("friendName");
        txtName.setText(friendName);

        friendID = getIntent().getExtras().getString("friendID");
        Log.d("friendID ", friendID);

        //Getting base url
        baseURL = apiConfiguration.getApi();
        urlLoadMessage = baseURL + "MessageAPI/GetOurOldMessage/" + loggedInUserID + "/" + friendID;
        messageArrayList = new ArrayList<Message>();
        new LoadMessageTask().execute();
       /* if (messageArrayList.size() != 0) {
            adapter_message = new Adapter_message(ChatActivity.this, messageArrayList);
            messageList.setAdapter(adapter_message);
        }*/
        adapter_message = new Adapter_message(ChatActivity.this, messageArrayList);
        lv.setAdapter(adapter_message);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentmessage = edtMessage.getText().toString().trim();
                //Getting BAseURL
                baseURL = apiConfiguration.getApi();
                urlSubmitMessage = baseURL + "MessageAPI/SubmitMessage";
                new SubmitMessagesTask().execute(loggedInUserID, sentmessage, friendID);
                edtMessage.setText(" ");
                Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_LONG).show();


            }
        });

    }

    public class LoadMessageTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

//                senderId = params[0];
//                Log.e("SenderId", loggedInUserID);
//                receiverId = params[1];
//
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("SenderId",loggedInUserID);
//                    jsonObject.put("ReceiverId",receiverId);
//
//
//                    jsonStringToPost = jsonObject.toString();
//                    response = httpRequestProcessor.pOSTRequestProcessor(jsonStringToPost, urlLoadMessage);
//                    jsonResponseString = response.getJsonResponseString();
//                }
//                catch (JSONException e)
//                {
//                    e.printStackTrace();
//                }
//                return jsonResponseString;
            jsonResponse = httpRequestProcessor.gETRequestProcessor(urlLoadMessage);
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                success = jsonObject.getBoolean("success");
                Log.d("Success", String.valueOf(success));
                message = jsonObject.getString("message");
                Log.d("message", message);

                if (success) {
                    JSONArray responseData = jsonObject.getJSONArray("responseData");

                    //If resonseData is not null
                    if (responseData != null && responseData.length() != 0) {
                        for (int i = 0; i < responseData.length(); i++) {
                            JSONObject object = responseData.getJSONObject(i);

                            senderName = object.getString("SenderName");
                            Log.d("SenderName", senderName);
                            receiverId = object.getString("RecipientId");
                            messages = object.getString("MessageBody");
                            // memberID = object.getInt("RecipientId");
                            senderId = object.getString("SenderId");
                            Log.d( "SenderId ",senderId);

                            //If sender of message is same as the loggedIn User,set the boolean field isLoggedInUserID to true
                            if (senderId.equals(loggedInUserID)) {
                                isLoggedInUserId = true;
                                m = new Message(senderName, messages, isLoggedInUserId);
                                messageArrayList.add(m);

                            } else {
                                isLoggedInUserId = false;
                                m = new Message(senderName, messages, isLoggedInUserId);
                                messageArrayList.add(m);
                            }
                        }
                        adapter_message.notifyDataSetChanged();
                        Toast.makeText(ChatActivity.this, message, Toast.LENGTH_LONG).show();


                    }
                    //if JSONArray is null i.e. users are not invloved in any chat till now
                   /* else if (responseData.length() == 0) {
                        m = new Message();
                        messageArrayList.add(m);
                    }*/

                } else {
                    Toast.makeText(ChatActivity.this, message, Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

    public class SubmitMessagesTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            loggedInUserID = params[0];
            Log.d("SenderId", loggedInUserID);
            sentmessage = params[1];
            Log.d("MessageBody ", sentmessage);
            friendID = params[2];
            Log.d("ReceiverId ", friendID);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("SenderId", loggedInUserID);
                Log.d("SenderId", loggedInUserID);
                jsonObject.put("MessageBody", sentmessage);
                Log.d("MessageBody", sentmessage);
                jsonObject.put("ReceiverId", friendID);
                Log.d("ReceiverId", friendID);

                jsonStringToPost = jsonObject.toString();
                response = httpRequestProcessor.postRequestProcessor(jsonStringToPost, urlSubmitMessage);
                jsonResponseString = response.getJsonResponseString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonResponseString;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                success = jsonObject.getBoolean("success");
                Log.d("Success", String.valueOf(success));
                message = jsonObject.getString("message");
                Log.d("message", message);
                responseData = jsonObject.getInt("responseData");
                if (responseData == 1) {
                    Toast.makeText(ChatActivity.this, message, Toast.LENGTH_LONG).show();

                } else if (responseData == 2) {
                    Toast.makeText(ChatActivity.this, message, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}





