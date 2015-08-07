package org.feedhenry.pushstarter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.feedhenry.sdk.FH;
import com.feedhenry.sdk.FHActCallback;
import com.feedhenry.sdk.FHResponse;

import org.feedhenry.pushstarter.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        register();
    }

    private void register() {

        FH.pushRegister(new FHActCallback() {
            @Override
            public void success(FHResponse fhResponse) {
                startActivity(new Intent(RegisterActivity.this, MessagesActivity.class));
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
