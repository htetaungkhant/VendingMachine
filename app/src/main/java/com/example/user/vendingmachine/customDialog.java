package com.example.user.vendingmachine;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.text.Layout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class customDialog{

    private TextView dialogText;
    private ImageView dialogImage;
    private LinearLayout dialogButtonPanel;
    private Dialog dialog;
    private AppCompatButton btnOk;
    private AppCompatButton btnCancel;

    private boolean hasPositiveButton=false;
    private boolean hasNegativeButton=false;

    public customDialog(Activity activity){
        dialog=new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);
    }

    public void setDialogType(String type){
        dialogImage=dialog.findViewById(R.id.a);

        if(type.equals("success")){
            dialogImage.setBackgroundColor(Color.parseColor("#1FA321"));
            dialogImage.setImageResource(R.drawable.success);
        }
        //fail
        else{
            dialogImage.setBackgroundColor(Color.parseColor("#FF4081"));
            dialogImage.setImageResource(R.drawable.dialog_cross);
        }
    }

    public void setMessage(String message) {
        dialogText=dialog.findViewById(R.id.text_dialog);
        dialogText.setText(message);
        dialogText.setTextSize(14);
        dialogText.setTypeface(Typeface.SANS_SERIF,Typeface.BOLD);
    }

    public void setPositiveButton(String label,String color,final View.OnClickListener listener){
        if(hasPositiveButton){
            dialogButtonPanel.removeView(btnOk);
        }
            dialogButtonPanel=dialog.findViewById(R.id.dialogButtonPanel);

            btnOk=new AppCompatButton(dialog.getContext());
            btnOk.setText(label);
            btnOk.setTextSize(12);
            btnOk.setBackgroundColor(Color.parseColor(color));
            //btnOk.setBackground(ContextCompat.getDrawable(dialog.getContext(),R.drawable.rounded_corner));
            btnOk.setPadding(0,0,0,0);
            //LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(200,95);
            params.setMargins(0,0,20,20);
            btnOk.setLayoutParams(params);
            btnOk.setOnClickListener(listener);

            dialogButtonPanel.addView(btnOk);
            hasPositiveButton=true;
    }

    public void setNegativeButton(String label,String color,final View.OnClickListener listener){
        if(hasNegativeButton){
            dialogButtonPanel.removeView(btnCancel);
        }
        btnCancel=new AppCompatButton(dialog.getContext());
        btnCancel.setText(label);
        btnCancel.setTextSize(12);
        btnCancel.setBackgroundColor(Color.parseColor(color));
        //btnCancel.setBackground(ContextCompat.getDrawable(dialog.getContext(),R.drawable.rounded_corner));
        btnCancel.setPadding(0,0,0,0);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(200,95);
        params.setMargins(0,0,20,20);
        btnCancel.setLayoutParams(params);
        btnCancel.setOnClickListener(listener);

        dialogButtonPanel=dialog.findViewById(R.id.dialogButtonPanel);
        dialogButtonPanel.addView(btnCancel);
        hasNegativeButton=true;
    }

    public void show(){
        dialog.show();
    }
    public void cancel(){dialog.cancel();}
    public void dismiss(){dialog.dismiss();};
}
