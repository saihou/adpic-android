package com.adpic.adpic;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SignupActivity extends AppCompatActivity {

    private View formView;
    private View progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        formView = findViewById(R.id.create_form_elements);
        progressView = findViewById(R.id.create_progress);

        ImageView back = (ImageView) findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button createButton = (Button) findViewById(R.id.create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(v, "Thanks for your interest!! We will be in touch shortly.", Snackbar.LENGTH_LONG);
                ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.CYAN);
                snackbar.show();

                //FIXME: do a real network connection to create account instead of simulating
                showProgress(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showProgress(false);
                    }
                }, 3000);
            }
        });
    }

    private void showProgress(boolean show) {
        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        formView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    public Activity getActivity() {
        return this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
