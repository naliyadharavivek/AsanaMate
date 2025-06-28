package com.asana.mate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageView = findViewById(R.id.splash_screen_image);
        sp = getSharedPreferences(ConstantSP.PREF, MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (sp.getString(ConstantSP.NAME, "").equals("")) {
                    Intent mainActivity = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainActivity);
                    finish();
                } else {
                    Intent homeActivity = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(homeActivity);
                    finish();
                }

            }
        }, 3000);
    }
}