/**
 * Copyright (c) 2015 FeedHenry Ltd, All Rights Reserved.
 *
 * Please refer to your contract with FeedHenry for the software license agreement.
 * If you do not have a contract, you do not have a license to use this software.
 */
package org.feedhenry.pushstarter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.feedhenry.sdk.FH;
import com.feedhenry.sdk.FHActCallback;
import com.feedhenry.sdk.FHResponse;

import org.feedhenry.pushstarter.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        init();
    }

    private void init() {
        FH.init(getApplicationContext(), new FHActCallback() {
            @Override
            public void success(FHResponse fhResponse) {
                startActivity(new Intent(SplashScreenActivity.this, RegisterActivity.class));
            }

            @Override
            public void fail(FHResponse fhResponse) {
                Toast.makeText(getApplicationContext(),
                        fhResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}
