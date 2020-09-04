package com.example.user.vendingmachine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Session;

public class SignupActivity extends AppCompatActivity {
    Button btnContinue;
    EditText signupUsername;
    EditText signupEmail;
    EditText signupPassword;
    EditText signupConfirmPassword;
    EditText signupPhoneNumber;

    TextView emailError;
    TextView passwordError;
    TextView confirmPasswordError;

    ProgressDialog pdialog = null;
    Context context=null;

    boolean mailExist;

    RequestQueue queue;
    String checkMailRequestUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        context = this;

        queue=Volley.newRequestQueue(this);

        btnContinue=findViewById(R.id.btnContinue);
        signupUsername=findViewById(R.id.signupUsername);
        signupEmail=findViewById(R.id.signupEmail);
        signupPassword=findViewById(R.id.signupPassword);
        signupConfirmPassword=findViewById(R.id.signupConfirmPassword);
        signupPhoneNumber=findViewById(R.id.signupPhoneNumber);
        emailError=findViewById(R.id.emailError);
        passwordError=findViewById(R.id.passwordError);
        confirmPasswordError=findViewById(R.id.confirmPasswordError);

        signupUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String username=signupUsername.getText().toString();
                if (username.equals("")){
                    signupUsername.setBackgroundResource(R.drawable.error_message);
                }
                else {
                    signupUsername.setBackgroundResource(R.drawable.edit_text_field_design);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signupEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email=signupEmail.getText().toString().trim();
                if(!email.equals(""))
                {
                    signupEmail.setBackgroundResource(R.drawable.edit_text_field_design);
                }
                if(validEmail(email)){
                    checkMailExist(email);
                }
                if(email.equals(""))
                {
                    signupEmail.setBackgroundResource(R.drawable.error_message);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signupPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password=signupPassword.getText().toString();
                String confirmPassword=signupConfirmPassword.getText().toString();
                if(password.equals("")){
                    signupPassword.setBackgroundResource(R.drawable.error_message);
                }
                if(password.equals(confirmPassword)){
                    confirmPasswordError.setText("");
                }
                if(!password.equals("")) {
                    signupPassword.setBackgroundResource(R.drawable.edit_text_field_design);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signupConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password=signupPassword.getText().toString();
                String confirmPassword=signupConfirmPassword.getText().toString();
                if(confirmPassword.equals("")){
                    signupConfirmPassword.setBackgroundResource(R.drawable.error_message);
                }
                if(password.equals(confirmPassword)){
                    confirmPasswordError.setText("");
                }
                if(!confirmPassword.equals("")) {
                    signupConfirmPassword.setBackgroundResource(R.drawable.edit_text_field_design);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signupPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phoneNumber=signupPhoneNumber.getText().toString();
                if(phoneNumber.equals("")){
                    signupPhoneNumber.setBackgroundResource(R.drawable.error_message);
                }
                else {
                    signupPhoneNumber.setBackgroundResource(R.drawable.edit_text_field_design);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username=signupUsername.getText().toString().trim();
                String email=signupEmail.getText().toString().trim();
                String password=signupPassword.getText().toString().trim();
                String confirmPassword=signupConfirmPassword.getText().toString().trim();
                String phoneNumber=signupPhoneNumber.getText().toString().trim();

                if(username.equals("")){
                    signupUsername.setBackgroundResource(R.drawable.error_message);
                }
                if(email.equals("")){
                    signupEmail.setBackgroundResource(R.drawable.error_message);
                }
                if(!validEmail(email) && !email.equals("")){
                    emailError.setText("Invalid email");
                }
                if(password.equals("")){
                    signupPassword.setBackgroundResource(R.drawable.error_message);
                }
                if(confirmPassword.equals("")){
                    signupConfirmPassword.setBackgroundResource(R.drawable.error_message);
                }
                if(!password.equals(confirmPassword)){
                    confirmPasswordError.setText("Password does not match");
                }
                if(phoneNumber.equals("")){
                    signupPhoneNumber.setBackgroundResource(R.drawable.error_message);
                }
                if(!username.equals("") && !email.equals("") && validEmail(email) && !mailExist && !password.equals("") && !confirmPassword.equals("") && password.equals(confirmPassword) && !phoneNumber.equals("")){
                    Random r = new Random();
                    int min=1000;
                    int max=9999;
                    int num=r.nextInt((max - min) + 1) + min;

                    String result="Hello "+username+",<br><center>Your confirmation code is <b>"+num+"</b>";

                    //Creating SendMail object
                    SendMail sm = new SendMail(SignupActivity.this, email, "Confirmation for YCC wallet",result);
                    sm.execute();

                    Intent toConfirmCode=new Intent(SignupActivity.this,ConfirmCode.class);
                    toConfirmCode.putExtra("confirmCode",num);
                    toConfirmCode.putExtra("username",username);
                    toConfirmCode.putExtra("email",email);
                    toConfirmCode.putExtra("password",password);
                    toConfirmCode.putExtra("phoneNumber",phoneNumber);
                    startActivity(toConfirmCode);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.isLogout=true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(ConfirmCode.isSignupSuccess1){
            ConfirmCode.isSignupSuccess1=false;
            finish();
        }
    }

    private static boolean validEmail(String emailAddr){
        Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(emailAddr);
        if(matcher.find()){
            return true;
        }
        else{
            return false;
        }
    }
    private void checkMailExist(String emailAddr){
        checkMailRequestUrl=serverIP.serverIp+"user/checkMailExist";
        queue.start();
        HashMap<String,String> params=new HashMap<String, String>();
        params.put("email",emailAddr);

        JsonObjectRequest checkMailRequest=new JsonObjectRequest(Request.Method.POST, checkMailRequestUrl, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            emailError.setText(response.getString("message"));
                            mailExist=true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        emailError.setText("");
                        mailExist=false;
                    }
                });
        checkMailRequest.setTag("TAG");
        queue.add(checkMailRequest);
    }
}
