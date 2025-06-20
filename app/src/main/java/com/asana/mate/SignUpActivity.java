package com.asana.mate;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    RadioGroup genderSignup;
    Spinner countrySignup;
    CheckBox terms;
    Button signup;
    ImageView showPassword, hidePassword, showConfirmPassword, hideConfirmPassword;
    FirebaseDatabase signupDatabase;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        findViewById(R.id.main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    View currentFocus = getCurrentFocus();
                    if (currentFocus instanceof EditText) {
                        InputMethodManager keyBoardHider = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        keyBoardHider.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
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

        nameSignup = findViewById(R.id.signup_name_box);
        emailSignup = findViewById(R.id.signup_email_box);
        passwordSignup = findViewById(R.id.signup_password_box);
        confirmPasswordSignup = findViewById(R.id.signup_confirmpassword_box);
        genderSignup = findViewById(R.id.signup_gender);
        countrySignup = findViewById(R.id.signup_country);
        terms = findViewById(R.id.terms_and_conditions);
        signup = findViewById(R.id.signup_button);
        showPassword = findViewById(R.id.signup_view);
        hidePassword = findViewById(R.id.signup_hide);
        showConfirmPassword = findViewById(R.id.signup_confirm_view);
        hideConfirmPassword = findViewById(R.id.signup_confirm_hide);


        String[] countryCodes = Locale.getISOCountries();
        for (String countryCode : countryCodes) {
            Locale countryLocale = new Locale("", countryCode);
            countryNames.add(countryLocale.getDisplayCountry());
        }

        countryNames.add(0, "Choose your country");

        ArrayAdapter countryList = new ArrayAdapter<>(SignUpActivity.this, android.R.layout.simple_list_item_1, countryNames);
        countryList.setDropDownViewResource(android.R.layout.simple_list_item_multiple_choice);
        countrySignup.setAdapter(countryList);

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordSignup.setTransformationMethod(null);
                showPassword.setVisibility(v.INVISIBLE);
                hidePassword.setVisibility(v.VISIBLE);
            }
        });

        hidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordSignup.setTransformationMethod(new PasswordTransformationMethod());
                showPassword.setVisibility(v.VISIBLE);
                hidePassword.setVisibility(v.INVISIBLE);
            }
        });

        showConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPasswordSignup.setTransformationMethod(null);
                showConfirmPassword.setVisibility(v.INVISIBLE);
                hideConfirmPassword.setVisibility(v.VISIBLE);
            }
        });

        hideConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPasswordSignup.setTransformationMethod(new PasswordTransformationMethod());
                showConfirmPassword.setVisibility(v.VISIBLE);
                hideConfirmPassword.setVisibility(v.INVISIBLE);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!nameSignup.getText().toString().isEmpty() && emailSignup.getText().toString().trim().matches(emailPattern) && !emailSignup.getText().toString().trim().isEmpty() && passwordSignup.getText().toString().trim().matches(passwordPattern) && !passwordSignup.getText().toString().trim().isEmpty() && !confirmPasswordSignup.getText().toString().trim().isEmpty() && confirmPasswordSignup.getText().toString().trim().equals(passwordSignup.getText().toString().trim()) && !(genderSignup.getCheckedRadioButtonId() == -1) && !(countrySignup.getSelectedItemPosition()<=0) && terms.isChecked()) {

                    signupDatabase = FirebaseDatabase.getInstance();
                    reference = signupDatabase.getReference("users");

                    reference.orderByChild("email").equalTo(emailSignup.getText().toString().replace(".", "_")).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                emailSignup.setError("Email already exists!!");
                            } else {
                                Integer selectedGenderID = genderSignup.getCheckedRadioButtonId();

                                String name = nameSignup.getText().toString();
                                String email = emailSignup.getText().toString().replace(".", "_");
                                String password = passwordSignup.getText().toString();
                                String confirmPassword = confirmPasswordSignup.getText().toString();
                                String country = countrySignup.getSelectedItem().toString();
                                String gender = null;

                                if (selectedGenderID != -1) {
                                    RadioButton selectedGender = findViewById(selectedGenderID);
                                    gender = selectedGender.getText().toString();
                                }

                                HelperClass helperClass = new HelperClass(name, email, password, confirmPassword, country, gender);
                                reference.child(name).setValue(helperClass);


                                Toast.makeText(SignUpActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else if (nameSignup.getText().toString().isEmpty()) {
                    nameSignup.setError("Name can not be blank");
                } else if (!emailSignup.getText().toString().trim().matches(emailPattern)) {
                    emailSignup.setError("Enter Valid Email");
                }
                else if (emailSignup.getText().toString().trim().isEmpty()) {
                    emailSignup.setError("Email can not be blank");
                } else if (!passwordSignup.getText().toString().trim().matches(passwordPattern)) {
                    passwordSignup.setError(passwordError);
                } else if (passwordSignup.getText().toString().trim().isEmpty()) {
                    passwordSignup.setError("Password can not be blank");
                } else if (confirmPasswordSignup.getText().toString().trim().isEmpty()) {
                    confirmPasswordSignup.setError("Confirm Password can not be blank");
                } else if (!confirmPasswordSignup.getText().toString().trim().equals(passwordSignup.getText().toString().trim())) {
                    confirmPasswordSignup.setError("Passwords do not match");
                } else if (genderSignup.getCheckedRadioButtonId() == -1) {
                    Snackbar.make(view, "Gender is required", Snackbar.LENGTH_SHORT).show();
                } else if (countrySignup.getSelectedItemPosition()<=0) {
                    Snackbar.make(view, "Please select a country", Snackbar.LENGTH_SHORT).show();
                } else if (!terms.isChecked()) {
                    Snackbar.make(view, "Please accept terms & conditions", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}