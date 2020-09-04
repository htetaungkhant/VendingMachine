package com.example.user.vendingmachine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RedeemActivity extends AppCompatActivity {

    TextView tvRedeemCodeError;
    EditText etRedeem;
    Button btnRedeem;

    RequestQueue queue;
    String redeemRequestUrl;

    String Email;

    SweetAlertDialog pd;

    public static boolean isRedeemSuccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);

        tvRedeemCodeError=findViewById(R.id.tvRedeemCodeError);
        etRedeem=findViewById(R.id.etRedeem);
        btnRedeem=findViewById(R.id.btnRedeem);

        Intent dataFromMain=getIntent();
        Email=dataFromMain.getStringExtra("emailFromMain");

        queue= Volley.newRequestQueue(this);
        redeemRequestUrl=serverIP.serverIp+"wallet/redeemWallet";
        queue.start();

        etRedeem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String walletCode=etRedeem.getText().toString().trim();
                if(walletCode.length()== 15){
                    tvRedeemCodeError.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String walletCode=etRedeem.getText().toString().trim();
                if(walletCode.length()==15){
                    HashMap<String,String> params=new HashMap<String, String>();
                    params.put("email",Email);
                    params.put("walletCode",walletCode);

                    JsonObjectRequest redeemRequest=new JsonObjectRequest(Request.Method.POST,redeemRequestUrl,new JSONObject(params),
                            new Response.Listener<JSONObject>(){
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String result=response.getString("message");
                                        pd.dismiss();
                                        if(result.equals("Redeem success")){
                                            finish();
                                            isRedeemSuccess=true;
                                            onBackPressed();
                                        }
                                        else{
                                            SweetAlertDialog pd1=new SweetAlertDialog(RedeemActivity.this,SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Fail!")
                                                    .setContentText("Sorry,your Code is invalid!");
                                                    pd1.show();
                                                    Button btnOk=pd1.findViewById(R.id.confirm_button);
                                                    btnOk.setBackgroundResource(R.drawable.sweet_alert_confirm_button);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener(){
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                    pd=new SweetAlertDialog(RedeemActivity.this,SweetAlertDialog.PROGRESS_TYPE);
                    pd.setTitleText("USSD Code Running...");
                    pd.setCancelable(false);
                    pd.show();
                    queue.add(redeemRequest);
                }
                else{
                    tvRedeemCodeError.setText("Invalid Redeem Code");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
