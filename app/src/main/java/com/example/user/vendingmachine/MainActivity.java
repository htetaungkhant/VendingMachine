package com.example.user.vendingmachine;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    Button btnLogout;
    TextView UserName;
    TextView UserPhone;
    TextView UserBalance;
    CardView PayCardView;
    CardView RedeemCardView;

    String username;
    String userphone;
    Integer userbalance;

    public static boolean isLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainFunction();
    }

    @Override
    public void onBackPressed() {
        if(isLogout){
            SweetAlertDialog pd=new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure to Logout?")
                    .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            finish();
                        }
                    })
                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            isLogout=false;
                        }
                    });
                    pd.show();
                    Button btnOk=pd.findViewById(R.id.confirm_button);
                    btnOk.setBackgroundResource(R.drawable.sweet_alert_confirm_button);
        }
        else{
            SweetAlertDialog pd=new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure to exit?")
                    .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            finish();
                        }
                    })
                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });
                    pd.show();
                    Button btnOk=pd.findViewById(R.id.confirm_button);
                    btnOk.setBackgroundResource(R.drawable.sweet_alert_confirm_button);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mainFunction();
    }

    private void mainFunction(){
        btnLogout=findViewById(R.id.btnLogout);
        UserName=findViewById(R.id.userName);
        UserPhone=findViewById(R.id.userPhone);
        UserBalance=findViewById(R.id.userBalance);
        PayCardView=findViewById(R.id.payCardView);
        RedeemCardView=findViewById(R.id.redeemCardView);

        isLogout=false;

        if(RedeemActivity.isRedeemSuccess){
            SweetAlertDialog pd=new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE);
            pd.setTitleText("Redeem Success!");
            pd.setCancelable(false);
            pd.show();
            Button btnOk=pd.findViewById(R.id.confirm_button);
            btnOk.setBackgroundResource(R.drawable.sweet_alert_confirm_button);
            RedeemActivity.isRedeemSuccess=false;
        }

        if(ConfirmCode.isSignupSuccess){
            SweetAlertDialog pd=new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE);
            pd.setTitleText("Successfully Created!");
            pd.setCancelable(false);
            pd.show();
            Button btnOk=pd.findViewById(R.id.confirm_button);
            btnOk.setBackgroundResource(R.drawable.sweet_alert_confirm_button);
            ConfirmCode.isSignupSuccess=false;
        }

        Intent dataFromLogin=getIntent();
        final String Email=dataFromLogin.getStringExtra("Email");

        final RequestQueue queue= Volley.newRequestQueue(this);
        final String userInfoRequestUrl=serverIP.serverIp+"user/"+Email;
        queue.start();

        JsonObjectRequest userInfoRequest=new JsonObjectRequest(Request.Method.GET, userInfoRequestUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array=response.getJSONArray("user");

                            for(int i=0;i<array.length();i++){
                                JSONObject user=array.getJSONObject(i);

                                username=user.getString("name");
                                userphone=user.getString("phoneNumber");
                                userbalance=user.getInt("balance");
                            }
                            UserName.setText(username);
                            UserPhone.setText(userphone);
                            UserBalance.setText("Current Balance =\t"+ NumberFormat.getNumberInstance(Locale.US).format(userbalance)+" MMK");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        queue.add(userInfoRequest);

        PayCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),barcodeScannerActivity.class));
            }
        });

        RedeemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRedeemActivity=new Intent(MainActivity.this,RedeemActivity.class);
                toRedeemActivity.putExtra("emailFromMain",Email);
                startActivity(toRedeemActivity);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                isLogout=true;
                onBackPressed();
            }
        });
    }
}
