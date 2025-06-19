package com.asana.mate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button login;
    ImageButton google,facebook;
    TextView signup,forgetPassword,google1;
    EditText emailLogin,passwordLogin;
    ImageView showPassword, hidePassword;
    Toolbar toolbar;

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
                        InputMethodManager keyboardHider = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        keyboardHider.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
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
        emailLogin = findViewById(R.id.email_box_login);
        passwordLogin = findViewById(R.id.password_box);
        showPassword = findViewById(R.id.signin_view);
        hidePassword = findViewById(R.id.signin_hide);


        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordLogin.setTransformationMethod(null);
                showPassword.setVisibility(v.INVISIBLE);
                hidePassword.setVisibility(v.VISIBLE);
            }
        });

        hidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordLogin.setTransformationMethod(new PasswordTransformationMethod());
                showPassword.setVisibility(v.VISIBLE);
                hidePassword.setVisibility(v.INVISIBLE);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailLogin.getText().toString().trim().equals("") || !emailLogin.getText().toString().trim().matches(emailPattern)) {
                    emailLogin.setError("Enter valid email");
                } else if (passwordLogin.getText().toString().equals("") || !passwordLogin.getText().toString().matches(passwordPattern)) {
                    passwordLogin.setError(passwordError);
                } else {
                    checkUser();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);

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

    public void checkUser() {
        String userEmail = emailLogin.getText().toString().trim().replace(".","_"),
               userPassword = passwordLogin.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("email").equalTo(userEmail);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    emailLogin.setError(null);
                    String passwordDB = snapshot.child(userEmail).child("password").getValue(String.class);

                    if (!Objects.equals(passwordDB, userPassword)) {
                        passwordLogin.setError(null);
                        Intent homeActivity = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(homeActivity);

                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        passwordLogin.setError("Invalid Password");
                        passwordLogin.requestFocus();
                    }
                } else {
                    emailLogin.setError("Wrong Email");
                    emailLogin.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}