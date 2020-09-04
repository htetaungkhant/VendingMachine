package com.example.user.vendingmachine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ConfirmCode extends AppCompatActivity {

    EditText confirmCode;
    Button btnConfirm;

    public static boolean isSignupSuccess;
    public static boolean isSignupSuccess1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_code);

        confirmCode=findViewById(R.id.etConfirmCode);
        btnConfirm=findViewById(R.id.btnConfirm);

        Intent dataFromSignup=getIntent();
        final int code=dataFromSignup.getIntExtra("confirmCode",0);
        final String username=dataFromSignup.getStringExtra("username");
        final String email=dataFromSignup.getStringExtra("email");
        final String password=dataFromSignup.getStringExtra("password");
        final String phoneNumber=dataFromSignup.getStringExtra("phoneNumber");

        final RequestQueue queue= Volley.newRequestQueue(this);
        final String signupRequestUrl=serverIP.serverIp+"user/signup";
        queue.start();
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int codeNow=Integer.parseInt(confirmCode.getText().toString().trim());
                    if(code==codeNow){
                        HashMap<String,String> params=new HashMap<String, String>();
                        params.put("name",username);
                        params.put("email",email);
                        params.put("password",password);
                        params.put("phoneNumber",phoneNumber);

                        JsonObjectRequest postRequest=new JsonObjectRequest(Request.Method.POST, signupRequestUrl, new JSONObject(params),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        isSignupSuccess=true;
                                        isSignupSuccess1=true;

                                        Intent myIntent=new Intent(ConfirmCode.this,MainActivity.class);
                                        myIntent.putExtra("Email",email);
                                        startActivity(myIntent);
                                        finish();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(ConfirmCode.this,"code is incorred",Toast.LENGTH_LONG).show();
                                    }
                                }
                        );
                        queue.add(postRequest);
                    }
                    else {
                        new SweetAlertDialog(ConfirmCode.this,SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Invalid Code!")
                                .setContentText("Sorry,your Code is invalid!")
                                .show();
                    }
                }catch (NumberFormatException e){
                    confirmCode.setBackgroundResource(R.drawable.error_message);
                }
            }
        });
    }
}
