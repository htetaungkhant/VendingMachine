package com.example.user.vendingmachine;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import android.widget.Toast;


import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail;
    EditText edtUserPassword;
    Button btnLogin;
    Button btnSignup;

    RequestQueue queue;
    String loginRequestUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail=findViewById(R.id.editEmail);
        edtUserPassword=findViewById(R.id.editUserPassword);
        btnLogin=findViewById(R.id.btnLogin);
        btnSignup=findViewById(R.id.btnSignup);

        queue=Volley.newRequestQueue(this);
        loginRequestUrl=serverIP.serverIp+"user/login";
        queue.start();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=edtEmail.getText().toString();
                String password=edtUserPassword.getText().toString();

                HashMap<String,String> params=new HashMap<String, String>();
                params.put("email",email);
                params.put("password",password);

                JsonObjectRequest postRequest=new JsonObjectRequest(Request.Method.POST, loginRequestUrl, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Intent myIntent=new Intent(LoginActivity.this,MainActivity.class);
                                myIntent.putExtra("Email",email);
                                startActivity(myIntent);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(LoginActivity.this,"UserName or Password is incorred",Toast.LENGTH_LONG).show();
                            }
                        }
                );
                queue.add(postRequest);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSignup=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(toSignup);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.isLogout=true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(MainActivity.isLogout){
            edtEmail.setText("");
            edtUserPassword.setText("");
            edtEmail.requestFocus();
            MainActivity.isLogout=false;
        }
        else{
            MainActivity.isLogout=false;
            finish();
        }
    }
}
