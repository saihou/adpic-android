package com.adpic.adpic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ChallengeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_details);

        if (Utils.mostRecentChallengeClicked != null) {
            ChallengeCardData data = Utils.lookupChallenge();
            Toast.makeText(this, data.getMerchantName(), Toast.LENGTH_LONG).show();
        } else {
            // Get lost, what are you doing here??
        }
    }
}
