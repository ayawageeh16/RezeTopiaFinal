package io.krito.com.rezetopia.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnimBarBuilder;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import io.krito.com.rezetopia.R;
import io.krito.com.rezetopia.application.AppConfig;
import io.krito.com.rezetopia.fragments.AlertFragment;
import io.krito.com.rezetopia.helper.DateDialog;
import io.krito.com.rezetopia.models.operations.UserOperations;
import io.krito.com.rezetopia.models.pojo.User;
import io.krito.com.rezetopia.views.CustomButton;
import io.krito.com.rezetopia.views.CustomEditText;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.rmiri.buttonloading.ButtonLoading;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    CustomEditText edtName, edtName2, edtPhone, edtEmail, edtDate, edtPassword;
    CustomButton btnLogin;
    ButtonLoading btnSignUp;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        edtName = findViewById(R.id.edName);
        edtPhone = findViewById(R.id.edPhone);
        edtEmail = findViewById(R.id.edEmail);
        edtDate = findViewById(R.id.edDate);
        edtPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnRegister);
        checkBox = findViewById(R.id.checkbox);
        edtName2 = findViewById(R.id.edName2);
        //btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        networkListener();

        edtDate.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    DateDialog dialog = new DateDialog(view);
                    FragmentTransaction ft =getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }

        });
        btnSignUp.setOnButtonLoadingListener(new ButtonLoading.OnButtonLoadingListener() {
            @Override
            public void onClick() {
                if (checkData()) {

                    final User user = new User();
                    user.setName(edtName.getText().toString().trim() + " " + edtName2.getText().toString().trim());
                    user.setFisrtName(edtName.getText().toString().trim());
                    user.setLastName(edtName2.getText().toString().trim());
                    user.setEmail(edtEmail.getText().toString().trim());
                    user.setPassword(edtPassword.getText().toString().trim());
                    user.setBirthday(edtDate.getText().toString());
                    user.setMobile(edtPassword.getText().toString().trim());
                    UserOperations.Register(user);
                    UserOperations.setRegisterCallback(new UserOperations.RegisterCallback() {
                        @Override
                        public void onSuccess(String id) {
                            getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                                    .edit().putString(AppConfig.LOGGED_IN_USER_ID_SHARED, id).apply();
                            startActivity(new Intent(Registration.this, WelcomeSlider.class));
                            finish();
                        }

                        @Override
                        public void onError(int error) {
                            String s;
                            if (error == R.string.unknown_error){
                                s = getResources().getString(R.string.invalid_email);
                            } else {
                                s = getResources().getString(error);
                            }

                            AlertFragment.createFragment(s).show(getFragmentManager(), null);
                            btnSignUp.setProgress(false);
                        }
                    });
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                btnSignUp.setProgress(false);
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnLogin:
                Intent loginIntent = new Intent(this, Login.class);
                startActivity(loginIntent);

                break;
            case R.id.btnRegister:
                break;
        }

    }


    private Boolean checkData() {
        String name,fName, lName, password, email, phone, dateOfBirth;
        Boolean valid = true;

        //name = edtName.getText().toString();
        password = edtPassword.getText().toString();
        email = edtEmail.getText().toString();
        dateOfBirth = edtDate.getText().toString();
        phone = edtPhone.getText().toString();
        fName = edtName.getText().toString();
        lName = edtName2.getText().toString();


        if (fName.isEmpty() || fName.length() < 3) {
            edtName.setError("Must be at least 3 characters ");
            valid = false;
        } else
            edtName.setError(null);

        if (lName.isEmpty() || lName.length() < 3) {
            edtName2.setError("Must be at least 3 characters ");
            valid = false;
        } else
            edtName2.setError(null);

        if (phone.isEmpty() || phone.length() < 11) {
            edtPhone.setError("Enter a Valid Mobile Number ");
            valid = false;
        } else
            edtPhone.setError(null);

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Enter a Valid Email");
            valid = false;
        } else
            edtEmail.setError(null);
        if (password.isEmpty() || password.length() < 4) {
            edtPassword.setError("Must be between 4 and 10 alphanumeric characters ");
            valid = false;
        } else
            edtPassword.setError(null);
        if (dateOfBirth.isEmpty()) {
            edtDate.setError(" Enter your birthday date");
            valid = false;
        } else
            edtDate.setError(null);
        if (!checkBox.isChecked()) {
            checkBox.setError("Confirm terms please ");
            valid = false;
        } else
            checkBox.setError(null);

        return valid;
    }

    private void networkListener(){
        ReactiveNetwork.observeNetworkConnectivity(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(connectivity -> {
                    if (connectivity.getState() == NetworkInfo.State.CONNECTED){
                        Log.i("internetC", "onNext: " + "Connected");
                    } else if (connectivity.getState() == NetworkInfo.State.SUSPENDED){
                        Log.i("internetC", "onNext: " + "LowNetwork");
                    } else {
                        Log.i("internetC", "onNext: " + "NoInternet");
                        Flashbar.Builder builder = new Flashbar.Builder(this);
                        builder.gravity(Flashbar.Gravity.BOTTOM)
                                .backgroundColor(R.color.red2)
                                .enableSwipeToDismiss()
                                .message(R.string.checkingNetwork)
                                .enterAnimation(new FlashAnimBarBuilder(Registration.this).slideFromRight().duration(200))
                                .build().show();
                    }
                });
    }
}
