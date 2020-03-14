package com.orgwork.renewed;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

class ProgressButton {

    private CardView cardView;
    private ConstraintLayout layout;
    private ProgressBar progressBar;
    private TextView textView;



    ProgressButton(Context ct, View view){

        cardView = view.findViewById(R.id.cardView);
        layout = view.findViewById(R.id.constraint_layout);
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.textLoginProgress);

    }

    void buttonRegister(){
        textView.setText("Register");
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.colorPrimary));
    }
    void buttonLoggin(){
        textView.setText("Login");
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.colorPrimary));
    }
    void buttonActivatedRegister(){
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("Signing up...");
    }
    void buttonActivatedLoggin(){
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("Logging in...");
    }
    void buttonFinished(){
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.green));
        progressBar.setVisibility(View.GONE);
        textView.setText("Success");
    }
    void buttonError(){
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.Red));
        progressBar.setVisibility(View.GONE);
        textView.setText("Invalid email");
    }

    void buttonErrorLoggin(){
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.Red));
        progressBar.setVisibility(View.GONE);
        textView.setText("Invalid credentials");
    }
    void buttonRecover(){
        textView.setText("Recover");
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.colorPrimary));
    }

}
