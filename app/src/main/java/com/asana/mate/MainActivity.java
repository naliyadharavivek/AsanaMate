package com.asana.mate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends AppCompatActivity {

    Button login;
    ImageButton google,facebook;
    TextView signup,forgetPassword,google1;
    EditText email,password;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&*\\-])(?=\\S+$).{8,20}$";
    String passwordError = "Password must contain " +
            "\n→ One digit" +
            "\n→ One lowercase letter" +
            "\n→ One uppercase letter" +
            "\n→ Minimum : 8 characters" +
            "\n→ Maximum : 20 characters" +
            "\n→ No spaces" +
            "\n→ A special character from this set : \n     ! @ # $ % & * -";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Add this to hide keyboard on touch outside of EditText
        findViewById(R.id.main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    View currentFocus = getCurrentFocus();
                    if (currentFocus instanceof EditText) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                        currentFocus.clearFocus();
                    }
                }
                return false;
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        login = findViewById(R.id.login_button);
        signup = findViewById(R.id.signUpText);
        forgetPassword = findViewById(R.id.forgot_password_text);
        google = findViewById(R.id.btnGoogle);
        google1 = findViewById(R.id.google_logo_text);
        facebook = findViewById(R.id.btnFacebook);
        email = findViewById(R.id.email_box);
        password = findViewById(R.id.password_box);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().matches(emailPattern) && password.getText().toString().matches(passwordPattern)) {
                    Snackbar.make(view, "login successful", Snackbar.LENGTH_SHORT).show();
                } else if (email.getText().toString().equals("") || !email.getText().toString().matches(emailPattern)) {
                    email.setError("Enter valid email");
                } /*else if (!email.getText().toString().matches(emailPattern)) {
                    email.setError("Enter valid email");
                }*/
                else if (password.getText().toString().equals("")) {
                    password.setError(passwordError);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "signup", Snackbar.LENGTH_SHORT).show();
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "forgetPassword", Snackbar.LENGTH_SHORT).show();
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "google", Snackbar.LENGTH_SHORT).show();
            }
        });

        google1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "googletext", Snackbar.LENGTH_SHORT).show();
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "facebook", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}