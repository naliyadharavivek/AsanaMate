package com.asana.mate.ui.profile;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.view.View.GONE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.asana.mate.ConstantSP;
import com.asana.mate.HelperClass;
import com.asana.mate.MainActivity;
import com.asana.mate.R;
import com.asana.mate.SignUpActivity;
import com.asana.mate.databinding.FragmentProfileBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    Integer countryPosition, selectedGenderID;
    String savedCountry;

    EditText nameBox, emailBox, passwordBox, confirmPasswordBox;
    Spinner countryBox;
    RadioButton male, female;
    RadioGroup genderProfile;
    TextView passwordText, confirmPasswordText;
    FrameLayout passwordFrame, confirmPasswordFrame;
    ImageView hidePassword, showPassword, showConfirmPassword, hideConfirmPassword;
    Button submitButton, editProfile, logout;
    ArrayList<String> countryNames = new ArrayList<>();

    SharedPreferences sp ;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference reference = firebaseDatabase.getReference("users");

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        root.findViewById(R.id.main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    View currentFocus = getActivity().getCurrentFocus();
                    if (currentFocus instanceof EditText) {
                        InputMethodManager keyboardHider = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        keyboardHider.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                        currentFocus.clearFocus();
                    }
                }
                return false;
            }
        });

        sp = requireActivity().getSharedPreferences(ConstantSP.PREF, Context.MODE_PRIVATE);

        nameBox = binding.profileNameBox;
        emailBox = binding.profileEmailBox;
        passwordText = binding.profilePasswordText;
        passwordFrame = binding.profilePasswordFrame;
        confirmPasswordFrame = binding.profileConfirmPasswordFrame;
        passwordBox = binding.profilePasswordBox;
        confirmPasswordText = binding.profileConfirmPasswordText;
        confirmPasswordBox = binding.profileConfirmPasswordBox;
        male = binding.profileMale;
        female = binding.profileFemale;
        showPassword  = binding.profilePasswordShow;
        hidePassword = binding.profilePasswordHide;
        showConfirmPassword = binding.profileConfirmpasswordShow;
        hideConfirmPassword = binding.profileConfirmpasswordHide;
        countryBox = binding.profileCountry;
        genderProfile = binding.profileGender;
        submitButton = binding.profileSubmitButton;
        editProfile = binding.profileEditButton;
        logout = binding.profileLogoutButton;


        nameBox.setText(sp.getString(ConstantSP.NAME,""));
        emailBox.setText(sp.getString(ConstantSP.EMAIL,"").replace("_", "."));
        passwordBox.setText(sp.getString(ConstantSP.PASSWORD,""));
        confirmPasswordBox.setText(sp.getString(ConstantSP.CONFIRMPASSWORD,""));


        String[] countryCodes = Locale.getISOCountries();
        for (String countryCode : countryCodes) {
            Locale countryLocale = new Locale("", countryCode);
            countryNames.add(countryLocale.getDisplayCountry());
        }

        countryNames.add(0, "Choose your country");

        ArrayAdapter<String> countryList = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, countryNames);
        countryList.setDropDownViewResource(android.R.layout.simple_list_item_multiple_choice);
        countryBox.setAdapter(countryList);


        for (int i = 0; i < countryNames.size(); i++) {

            if (sp.getString(ConstantSP.COUNTRY, "").equals(countryNames.get(i))) {
                countryPosition = i;
                break;
            }
        }

        countryBox.setSelection(countryPosition);

        if (sp.getString(ConstantSP.GENDER, null).equals("Male")) {

            male.setChecked(true);
            female.setChecked(false);

        } else {

            male.setChecked(false);
            female.setChecked(true);

        }

        setData(false);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setData(true);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder logoutAlert = new AlertDialog.Builder(getActivity());
                logoutAlert.setTitle("Logout");
                logoutAlert.setIcon(R.drawable.asanamate_logo1);
                logoutAlert.setMessage("Are you sure want to log out ?");

                logoutAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        sp.edit().clear().apply();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        requireActivity().finish();

                    }
                });

                logoutAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

                logoutAlert.show();

            }
        });

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordBox.setTransformationMethod(null);
                showPassword.setVisibility(INVISIBLE);
                hidePassword.setVisibility(VISIBLE);
            }
        });

        hidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordBox.setTransformationMethod(new PasswordTransformationMethod());
                showPassword.setVisibility(VISIBLE);
                hidePassword.setVisibility(INVISIBLE);
            }
        });

        showConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmPasswordBox.setTransformationMethod(null);
                showConfirmPassword.setVisibility(INVISIBLE);
                hideConfirmPassword.setVisibility(VISIBLE);
            }
        });

        hideConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmPasswordBox.setTransformationMethod(new PasswordTransformationMethod());
                showConfirmPassword.setVisibility(VISIBLE);
                hideConfirmPassword.setVisibility(INVISIBLE);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedGenderID = genderProfile.getCheckedRadioButtonId();

                String name = nameBox.getText().toString();
                String email = emailBox.getText().toString().replace(".", "_");
                String password = passwordBox.getText().toString();
                String confirmPassword = confirmPasswordBox.getText().toString();
                String country = countryBox.getSelectedItem().toString();
                String gender = null;

                if (selectedGenderID != -1) {
                    RadioButton selectedGender = root.findViewById(selectedGenderID);
                    gender = selectedGender.getText().toString();
                }

                String originalEmail = sp.getString(ConstantSP.EMAIL, "");
                String newEmail = emailBox.getText().toString().trim().replace(".", "_");

                if (!originalEmail.equals(newEmail)) {
                    String finalGender = gender;
                    reference.orderByChild("email").equalTo(newEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                                emailBox.setError("This mail is already linked to another account");
                                emailBox.requestFocus();

                            } else {

                                if (password.equals(confirmPassword)) {

                                    sp.edit().putString(ConstantSP.NAME, name).apply();
                                    sp.edit().putString(ConstantSP.EMAIL, email).apply();
                                    sp.edit().putString(ConstantSP.PASSWORD, password).apply();
                                    sp.edit().putString(ConstantSP.CONFIRMPASSWORD, confirmPassword).apply();
                                    sp.edit().putString(ConstantSP.GENDER, finalGender).apply();
                                    sp.edit().putString(ConstantSP.COUNTRY, country).apply();

                                    HelperClass helperClass = new HelperClass(name, email, password, confirmPassword, finalGender, country);
                                    reference.child(name).setValue(helperClass);


                                    Toast.makeText(getActivity(), "Details updated successfully!!", Toast.LENGTH_SHORT).show();
                                    requireActivity().onBackPressed();

                                    setData(false);

                                } else {

                                    confirmPasswordBox.setError("Password and Confirm Password does not match");

                                }


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {

                    if (password.equals(confirmPassword)) {

                        sp.edit().putString(ConstantSP.NAME, name).apply();
                        sp.edit().putString(ConstantSP.EMAIL, email).apply();
                        sp.edit().putString(ConstantSP.PASSWORD, password).apply();
                        sp.edit().putString(ConstantSP.CONFIRMPASSWORD, confirmPassword).apply();
                        sp.edit().putString(ConstantSP.GENDER, gender).apply();
                        sp.edit().putString(ConstantSP.COUNTRY, country).apply();

                        HelperClass helperClass = new HelperClass(name, email, password, confirmPassword, gender, country);
                        reference.child(name).setValue(helperClass);


                        Toast.makeText(getActivity(), "Details updated successfully!!", Toast.LENGTH_SHORT).show();
                        requireActivity().onBackPressed();

                        setData(false);

                    } else {

                        confirmPasswordBox.setError("Password and Confirm Password does not match");

                    }
                }

            }
        });

        return root;
    }

    private void setData(boolean b) {

        nameBox.setEnabled(b);
        emailBox.setEnabled(b);
        passwordBox.setEnabled(b);
        confirmPasswordBox.setEnabled(b);
        male.setEnabled(b);
        female.setEnabled(b);
        countryBox.setEnabled(b);



        if (b) {

            passwordText.setVisibility(VISIBLE);
            passwordFrame.setVisibility(VISIBLE);
            confirmPasswordText.setVisibility(VISIBLE);
            confirmPasswordFrame.setVisibility(VISIBLE);

            submitButton.setVisibility(VISIBLE);
            editProfile.setVisibility(GONE);
            logout.setVisibility(GONE);

        } else {

            submitButton.setVisibility(GONE);
            editProfile.setVisibility(VISIBLE);
            logout.setVisibility(VISIBLE);

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
