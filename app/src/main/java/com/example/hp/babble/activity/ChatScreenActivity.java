package com.example.hp.babble.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.Timer;
import java.util.TimerTask;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

/**
 * Created by HP on 5/8/2017.
 */

public class ChatScreenActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ChatScreenActivity";

    private ListView listView;
    private ImageButton buttonSend;
    private ImageView emojiButton;
    private EmojiconEditText edtMessage;
    private View rootView;
    private EmojIconActions emojIcon;
    private Button btnRefresh;
    private Toolbar toolbar;
    private TextView txtChatName;
    private ActionBarDrawerToggle actionBarDrawerToggle;
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
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        rootView = findViewById(R.id.root_view);
        buttonSend = (ImageButton) findViewById(R.id.send);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        listView = (ListView) findViewById(R.id.msgview);
        edtMessage = (EmojiconEditText) findViewById(R.id.msg);
        emojiButton = (ImageView) findViewById(R.id.emoji_btn);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        setSupportActionBar(toolbar);


        //Initialization
        httpRequestProcessor = new HttpRequestProcessor();
        response = new Response();
        apiConfiguration = new ApiConfiguration();


        sharedPreferences = ChatScreenActivity.this.getSharedPreferences(LogIn_Pref.Pref_Name, Context.MODE_PRIVATE);
        loggedInUserID = sharedPreferences.getString(LogIn_Pref.ApplicationUserId, null);


        //Getting FriendID from FriendList Screen
        friendName = getIntent().getExtras().getString("friendName");

        //TO Set Tittle of Toolbar
        getSupportActionBar().setTitle(friendName);

        friendID = getIntent().getExtras().getString("friendID");
        Log.d("friendID ", friendID);

        //Getting base url
        baseURL = apiConfiguration.getApi();
        urlLoadMessage = baseURL + "MessageAPI/GetOurOldMessage/" + loggedInUserID + "/" + friendID;
        messageArrayList = new ArrayList<Message>();
        new LoadMessageTask().execute();
        /*
        if (messageArrayList.size() != 0) {
            adapter_message = new Adapter_message(ChatScreenActivity.this, messageArrayList);
            listView.setAdapter(adapter_message);
        }
        */

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        buttonSend.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);

        adapter_message = new Adapter_message(ChatScreenActivity.this, messageArrayList);
        listView.setAdapter(adapter_message);

        emojIcon = new EmojIconActions(this, rootView, edtMessage, emojiButton);
        emojIcon.ShowEmojIcon();
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard", "open");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
            }
        });

/*
        chatText = (EditText) findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        */
/*
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
             sentmessage = edtMessage.getText().toString().trim();
                //Getting BAseURL
                baseURL = apiConfiguration.getApi();
                urlSubmitMessage = baseURL + "MessageAPI/SubmitMessage";
                new SubmitMessagesTask().execute(loggedInUserID, sentmessage, friendID);
                edtMessage.setText(" ");
                Toast.makeText(ChatScreenActivity.this, "Message sent", Toast.LENGTH_LONG).show();

               // sendChatMessage();
            }
        });
*/

        //to scroll the list view to bottom on data change
       /* chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
        */
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                sentmessage = edtMessage.getText().toString().trim();
                if (TextUtils.isEmpty(sentmessage)) {
                    edtMessage.setError("Message area is empty");
                    return;
                }
                //Getting BAseURL
                baseURL = apiConfiguration.getApi();
                urlSubmitMessage = baseURL + "MessageAPI/SubmitMessage";
                new SubmitMessagesTask().execute(loggedInUserID, sentmessage, friendID);
                edtMessage.setText(" ");
                //Toast.makeText(ChatScreenActivity.this, "Message sent", Toast.LENGTH_LONG).show();
                break;
            case R.id.btnRefresh:

                urlLoadMessage = baseURL + "MessageAPI/GetOurOldMessage/" + loggedInUserID + "/" + friendID;
                // messageArrayList = new ArrayList<Message>();
                new LoadMessageTask().execute();
                Toast.makeText(ChatScreenActivity.this, "Refresh", Toast.LENGTH_LONG).show();

                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    private boolean sendChatMessage() {
        chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString()));
        chatText.setText("");
        side = !side;
        return true;
    }
    */
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
                //TO clear the list for Refresh Button
                if (messageArrayList != null)
                    messageArrayList.clear();

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
                            Log.d("SenderId ", senderId);

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
                      //  Toast.makeText(ChatScreenActivity.this, message, Toast.LENGTH_LONG).show();


                    }
                    //if JSONArray is null i.e. users are not invloved in any chat till now
                   /* else if (responseData.length() == 0) {
                        m = new Message();
                        messageArrayList.add(m);
                    }
*/
                } else {
                    Toast.makeText(ChatScreenActivity.this, message, Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        callAsynchronousTask();
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
                    Toast.makeText(ChatScreenActivity.this, message, Toast.LENGTH_LONG).show();
                    //For loading old messages
                   // new LoadMessageTask().execute();
                } else if (responseData == 2) {
                    Toast.makeText(ChatScreenActivity.this, message, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //To Refresh the Activity
    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            LoadMessageTask loadMessageTask = new LoadMessageTask();
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                            loadMessageTask.execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 500); //execute in every 50000 ms
    }


}
