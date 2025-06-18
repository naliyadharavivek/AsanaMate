package com.asana.mate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class SignUpActivity extends AppCompatActivity {

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

    Button signup;
    EditText emailSignup,passwordSignup,confirmPasswordSignup, nameSignup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signup = findViewById(R.id.signup_button);
        emailSignup = findViewById(R.id.email_box_signup);
        passwordSignup = findViewById(R.id.password_box_sign_up);
        confirmPasswordSignup = findViewById(R.id.confirmpassword_box_sign_up);
        nameSignup = findViewById(R.id.name_box);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameSignup.getText().toString().isEmpty()) {
                    nameSignup.setError("Name can not be blank");
                } else if (!emailSignup.getText().toString().trim().matches(emailPattern)) {
                    emailSignup.setError("Enter Valid Email");
                } else if (emailSignup.getText().toString().trim().isEmpty()) {
                    emailSignup.setError("Email can not be blank");
                } else if (!passwordSignup.getText().toString().trim().matches(passwordPattern)) {
                    passwordSignup.setError(passwordError);
                } else if (passwordSignup.getText().toString().trim().isEmpty()) {
                    passwordSignup.setError("Password can not be blank");
                } else if (confirmPasswordSignup.getText().toString().trim().isEmpty()) {
                    confirmPasswordSignup.setError("Confirm Password can not be blank");
                } else if (!confirmPasswordSignup.getText().toString().trim().equals(passwordSignup.getText().toString().trim())) {
                    confirmPasswordSignup.setError("Passwords do not match");
                } else {
                    Snackbar.make(view, "Signup successful", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}