package com.example.hp.babble.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.babble.InternetDetector;
import com.example.hp.babble.R;
import com.example.hp.babble.adapter.SendrequestAdapter;
import com.example.hp.babble.apiConfiguration.ApiConfiguration;
import com.example.hp.babble.httpRequestProcessor.HttpRequestProcessor;
import com.example.hp.babble.httpRequestProcessor.Response;
import com.example.hp.babble.sharedPreferences.LogIn_Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LogIn extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LOGIN";
    private EditText edtUserName;
    private EditText edtPassword;
    private Button btnLogin;
    private HttpRequestProcessor httpRequestProcessor;
    private ApiConfiguration apiConfiguration;
    private Response response;
    private SharedPreferences sharedLoginPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private String baseURL, urlLogin,urlForgetPassword, jsonStringToPost, jsonResponseString;
    private String name, password,message;
    private boolean success, isActive;
    private String errorMessage, emailID, address, userName, applicationUserId;
    private ProgressDialog progressDialog;
    private TextView register,forgotPassword;

    //Internet Connection Checker
    Boolean isConnectionExist = false;
    InternetDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Log.d(TAG, "onCreate: ");
        //FIND VIEW BY ID
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogIn);
        register= (TextView) findViewById(R.id.reg);
        forgotPassword= (TextView) findViewById(R.id.forgot_password);

        cd = new InternetDetector(getApplicationContext());
        isConnectionExist = cd.checkMobileInternetConn();
        if (isConnectionExist) {
            //Toast.makeText(LogIn.this,"Internet is working",Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(LogIn.this,"Internet is not working",Toast.LENGTH_SHORT).show();
        }


        //Initialization
        httpRequestProcessor = new HttpRequestProcessor();
        response = new Response();
        apiConfiguration = new ApiConfiguration();

        //Getting base url
        baseURL = apiConfiguration.getApi();
        //URL for Login
        urlLogin = baseURL + "AccountAPI/GetLoginUser";
        //URL for Forget Password
        urlForgetPassword=baseURL+ "MemberAPI/ForgotPassword";

        //On clicking login button
        btnLogin.setOnClickListener(this);
        register.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        /*
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = edtUserName.getText().toString().trim();
                password = edtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(userName)) {
                    edtUserName.setError("Enter UserName");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    edtPassword.setError("Enter Password");
                    return;
                }
                new LoginTask().execute(userName, password);


            }
        });
        //On clicking Register Button move to registration screen
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn.this, SignUp.class);

                startActivity(intent);
            }
        });
        */
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogIn:
                userName = edtUserName.getText().toString().trim();
                password = edtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(userName)) {
                    edtUserName.setError("Enter UserName");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    edtPassword.setError("Enter Password");
                    return;
                }
                new LoginTask().execute(userName, password);

                break;
            case R.id.reg:
                Intent intent1 = new Intent(LogIn.this, SignUp.class);

                startActivity(intent1);
                break;
            case R.id.forgot_password:
                userName = edtUserName.getText().toString().trim();
                if(TextUtils.isEmpty(userName)){
                    edtUserName.setError("Enter UserName");
                    return;
                }
                new ForgetPasswordTask().execute(userName);
                break;

        }

    }


    public class LoginTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(LogIn.this);
            progressDialog.setMessage("Loging In...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            userName = params[0];
            password = params[1];
            //  applicationUserId=params[2];

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("UserName", userName);
                jsonObject.put("Password", password);
                //    jsonObject.put("ApplicationUserId",applicationUserId);

                jsonStringToPost = jsonObject.toString();
                response = httpRequestProcessor.postRequestProcessor(jsonStringToPost, urlLogin);
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
                Log.d("success", String.valueOf(success));
                errorMessage = jsonObject.getString("ErrorMessage");
                Log.d("ErrorMessage", errorMessage);

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }


                if (success == true) {


                    Toast.makeText(LogIn.this, errorMessage, Toast.LENGTH_SHORT).show();


                    // JSONArray responseData = jsonObject.getJSONArray("response`Data");
                    // for (int i = 0; i < responseData.length(); i++) {
                    JSONObject object = new JSONObject(jsonResponseString); /*= responseData.getJSONObject()*/
                    applicationUserId = object.getString("ApplicationUserId");
                    Log.d("ApplicationUserId", applicationUserId);
                    name = object.getString("Name");
                    Log.d("name", name);
                    address = object.getString("Address");
                    Log.d("address", address);
                    emailID = object.getString("EmailId");
                    Log.d("emailId", emailID);

                    userName = object.getString("UserName");
                    Log.d("userName", userName);
                    password = object.getString("Password");
                    Log.d("password", password);

                    //  }


                    //SharedPreferences
                    sharedLoginPreferences = getSharedPreferences(LogIn_Pref.Pref_Name, MODE_PRIVATE);
                    sharedPreferencesEditor = sharedLoginPreferences.edit();
                    sharedPreferencesEditor.putString(LogIn_Pref.ApplicationUserId, applicationUserId);
                    sharedPreferencesEditor.putString(LogIn_Pref.Name,name);
                    sharedPreferencesEditor.putString(LogIn_Pref.EmailId, emailID);
                    sharedPreferencesEditor.commit();


                    //Explicit Intent Type
                    Intent intent = new Intent(LogIn.this, ChatTabs.class);
                    intent.putExtra("name", name);
                    startActivity(intent);

                } else {


                    Toast.makeText(LogIn.this,errorMessage, Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    //ForgetPassword AsyncTask
    public class ForgetPasswordTask extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(LogIn.this);
            progressDialog.setMessage("Loading");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            userName = params[0];

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("UserName", userName);
                Log.d("UserName ",userName);

                jsonStringToPost = jsonObject.toString();
                response = httpRequestProcessor.postRequestProcessor(jsonStringToPost, urlForgetPassword);
                jsonResponseString = response.getJsonResponseString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonResponseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d( "str ",s);
            try {

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                JSONObject jsonObject = new JSONObject(s);
                success = jsonObject.getBoolean("success");
                Log.d("success", String.valueOf(success));
                message = jsonObject.getString("message");
                Log.d("message", message);
                int responseData;
                responseData=jsonObject.getInt("responseData");
                Log.d("responseData ", String.valueOf(responseData));
                Toast.makeText(LogIn.this,"Check Your Email For Password",Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
