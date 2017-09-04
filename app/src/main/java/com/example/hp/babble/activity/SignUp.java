package com.example.hp.babble.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hp.babble.InternetDetector;
import com.example.hp.babble.R;
import com.example.hp.babble.apiConfiguration.ApiConfiguration;
import com.example.hp.babble.httpRequestProcessor.HttpRequestProcessor;
import com.example.hp.babble.httpRequestProcessor.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    private static final String TAG="SIGNUP";
    private EditText edtName, edtUserName, edtEmail, edtPassword, edtConfirmPassword;
    private Button btnRegister, btnCancel;
    private HttpRequestProcessor httpRequestProcessor;
    private Response response;
    private ApiConfiguration apiConfiguration;
    private String baseURL, urlRegister;
    private String name, userName, email, password,confirmPassword;
    private String jsonPostString,jsonResponseString;
    private int responseData;
    private ProgressDialog progressDialog;

    //Internet Connection Checker
    Boolean isConnectionExist = false;
    InternetDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //findViewById
        edtName = (EditText) findViewById(R.id.edtName);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtConfirmPassword = (EditText) findViewById(R.id.edtConfirmPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        cd = new InternetDetector(getApplicationContext());
        isConnectionExist = cd.checkMobileInternetConn();
        if (isConnectionExist) {
            //Toast.makeText(LogIn.this,"Internet is working",Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(SignUp.this,"Internet is not working",Toast.LENGTH_SHORT).show();
        }

        //Initialization
        httpRequestProcessor = new HttpRequestProcessor();
        response = new Response();
        apiConfiguration = new ApiConfiguration();

        //Getting BaseURL
        baseURL = apiConfiguration.getApi();
        urlRegister = baseURL + "AccountAPI/SaveApplicationUser";

        //On clicking register Button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edtName.getText().toString();
                userName = edtUserName.getText().toString();
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();
                confirmPassword=edtConfirmPassword.getText().toString();

                if(TextUtils.isEmpty(name)){
                    edtName.setError("Enter Name");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    edtEmail.setError("Enter EmailId");
                    return;
                }else if (!isValidEmail(email)){
                    edtEmail.setError("Invalid Email Id");
                    return;
                }
                if(TextUtils.isEmpty(userName)){
                    edtUserName.setError("Enter UserName");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    edtPassword.setError("Enter Password");
                    return;
                }
                if(TextUtils.isEmpty(confirmPassword)){
                    edtConfirmPassword.setError("Re-Type Password");
                    return;
                }else if(password.equals(confirmPassword)){

                }else {
                    edtConfirmPassword.setError("Password Not Matched");
                    return;
                }





                new RegistrationTask().execute(name,email,userName,password);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUp.this,LogIn.class);
                startActivity(intent);
            }
        });

    }
    //Validating Email Id
    public boolean isValidEmail(String email){
        String Email_Pattern="^[_A-Za-z0-9-\\+]+(\\/[_A-Za-z0-9-]+)*@"+"[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern=Pattern.compile(Email_Pattern);
        Matcher matcher=pattern.matcher(email);
        return matcher.matches();
    }

    private class RegistrationTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(SignUp.this);
            progressDialog.setMessage("Signing Up...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "doInBackground: ");
            name = params[0];
            Log.e("Name", name);
            email = params[1];
            userName = params[2];
            password = params[3];

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Name", name);
                jsonObject.put("EmailId", email);
                jsonObject.put("UserName", userName);
                jsonObject.put("Password", password);


                jsonPostString = jsonObject.toString();
                response = httpRequestProcessor.postRequestProcessor(jsonPostString, urlRegister);
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
            try {
                JSONObject jsonObject = new JSONObject(s);
                responseData=jsonObject.getInt("responseData");
                Log.d("responseData", String.valueOf(responseData));

                if(responseData==1)
                {

                    Toast.makeText(SignUp.this,"User Register Successfully",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignUp.this,LogIn.class));
                }
                else {
                    Toast.makeText(SignUp.this,"User Already Registered",Toast.LENGTH_LONG).show();

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
}
