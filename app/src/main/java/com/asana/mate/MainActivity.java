package com.asana.mate;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
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
    ImageButton google, facebook;
    TextView signup, forgetPassword, google1, facebook1;
    EditText emailLogin, passwordLogin;
    ImageView showPassword, hidePassword;
    SharedPreferences sp;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&*\\-])(?=\\S+$).{8,20}$";
    String passwordError = """
            Password must contain \s
            → One digit\s
            → One lowercase letter\s
            → One uppercase letter\s
            → Minimum : 8 characters\s
            → Maximum : 20 characters\s
            → No spaces\s
            → A special character from this set :\s
                 ! @ # $ % & * -""";

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

        sp = getSharedPreferences(ConstantSP.PREF, MODE_PRIVATE);

        login = findViewById(R.id.login_button);
        signup = findViewById(R.id.signUpText);
        forgetPassword = findViewById(R.id.forgot_password_text);
        google = findViewById(R.id.btnGoogle);
        google1 = findViewById(R.id.google_logo_text);
        facebook = findViewById(R.id.btnFacebook);
        facebook1 = findViewById(R.id.facebook_logo_text);
        emailLogin = findViewById(R.id.email_box_login);
        passwordLogin = findViewById(R.id.password_box);
        showPassword = findViewById(R.id.signin_view);
        hidePassword = findViewById(R.id.signin_hide);


        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordLogin.setTransformationMethod(null);
                showPassword.setVisibility(INVISIBLE);
                hidePassword.setVisibility(VISIBLE);
            }
        });

        hidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordLogin.setTransformationMethod(new PasswordTransformationMethod());
                showPassword.setVisibility(VISIBLE);
                hidePassword.setVisibility(INVISIBLE);
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

                    Query getUserDetails = reference.orderByChild("email").equalTo(emailLogin.getText().toString().trim());

                    getUserDetails.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                    String sNAME = userSnapshot.child("name").getValue(String.class);
                                    String sEMAIL = userSnapshot.child("email").getValue(String.class);
                                    String sGENDER = userSnapshot.child("gender").getValue(String.class);
                                    String sPASSWORD = userSnapshot.child("password").getValue(String.class);
                                    String sCONFIRMPASSWORD = userSnapshot.child("confirmPassword").getValue(String.class);
                                    String sCOUNTRY = userSnapshot.child("country").getValue(String.class);

                                    sp.edit().putString(ConstantSP.NAME, sNAME).apply();
                                    sp.edit().putString(ConstantSP.EMAIL, sEMAIL).apply();
                                    sp.edit().putString(ConstantSP.PASSWORD, sPASSWORD).apply();
                                    sp.edit().putString(ConstantSP.CONFIRMPASSWORD, sCONFIRMPASSWORD).apply();
                                    sp.edit().putString(ConstantSP.GENDER, sGENDER).apply();
                                    sp.edit().putString(ConstantSP.COUNTRY, sCOUNTRY).apply();

                                    Log.d("SP_VALUES", "Name: " + sNAME + ", Email: " + sEMAIL + "Password: " + sPASSWORD + "Confirm Password :" + sCONFIRMPASSWORD + ", Gender: " + sGENDER + ", Country: " + sCOUNTRY);

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
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
                Snackbar.make(view, "Coming Soon! Stay tuned!!!", Snackbar.LENGTH_SHORT).show();
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Coming Soon! Stay tuned!!!", Snackbar.LENGTH_SHORT).show();
            }
        });

        google1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Coming Soon! Stay tuned!!!", Snackbar.LENGTH_SHORT).show();
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Coming Soon! Stay tuned!!!", Snackbar.LENGTH_SHORT).show();
            }
        });

        facebook1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Coming Soon! Stay tuned!!!", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void checkUser() {
        String userEmail = emailLogin.getText().toString().trim();
        String userPassword = passwordLogin.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("email").equalTo(userEmail);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    emailLogin.setError(null);

                    Query passwordDB = reference.orderByChild("password").equalTo(userPassword);
                    passwordDB.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                passwordLogin.setError(null);
                                Intent homeActivity = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(homeActivity);
                                finish();
                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                passwordLogin.setError("Invalid Password");
                                passwordLogin.requestFocus();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    emailLogin.setError("Account does not exist!! Please create one!!");
                    emailLogin.requestFocus();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}