package com.asana.mate;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;

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
    ArrayList<String> countryNames = new ArrayList<>();




    EditText nameSignup, emailSignup, passwordSignup, confirmPasswordSignup ;
    RadioGroup gender;
    Spinner country;
    CheckBox terms;
    Button signup;


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

        nameSignup = findViewById(R.id.signup_name_box);
        emailSignup = findViewById(R.id.signup_email_box);
        passwordSignup = findViewById(R.id.signup_password_box);
        confirmPasswordSignup = findViewById(R.id.signup_confirmpassword_box);
        gender = findViewById(R.id.signup_gender);
        country = findViewById(R.id.signup_country);
        terms = findViewById(R.id.terms_and_conditions);
        signup = findViewById(R.id.signup_button);

        String[] countryCodes = Locale.getISOCountries();
        for (String countryCode : countryCodes) {
            Locale countryLocale = new Locale("", countryCode);
            countryNames.add(countryLocale.getDisplayCountry());
        }

        countryNames.add(0, "Choose your country");

        ArrayAdapter countryList = new ArrayAdapter<>(SignUpActivity.this, android.R.layout.simple_list_item_1, countryNames);
        countryList.setDropDownViewResource(android.R.layout.simple_list_item_multiple_choice);
        country.setAdapter(countryList);

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
                } else if (gender.getCheckedRadioButtonId() == -1) {
                    Snackbar.make(view, "Gender is required", Snackbar.LENGTH_SHORT).show();
                } else if (country.getSelectedItemPosition()<=0) {
                    Snackbar.make(view, "Please select a country", Snackbar.LENGTH_SHORT).show();
                } else if (!terms.isChecked()) {
                    Snackbar.make(view, "Please accept terms & conditions", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(view, "Signup successful", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}