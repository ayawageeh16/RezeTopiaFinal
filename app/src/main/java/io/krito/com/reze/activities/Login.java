package io.krito.com.reze.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import io.krito.com.reze.R;
import io.krito.com.reze.application.AppConfig;
import io.krito.com.reze.application.RezetopiaApp;
import io.krito.com.reze.fragments.AlertFragment;
import io.krito.com.reze.models.operations.UserOperations;
import io.krito.com.reze.receivers.ConnectivityReceiver;
import io.krito.com.reze.views.CustomButton;
import io.krito.com.reze.views.CustomEditText;
import io.krito.com.reze.views.CustomTextView;
import io.rmiri.buttonloading.ButtonLoading;

public class Login extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener{

    RelativeLayout loginLayout;
    //CustomButton sign_in;
    CustomButton sign_up;
    CustomEditText emailView;
    CustomEditText passwordView;
    CustomTextView forgetPassword;
    ButtonLoading buttonLoading;

    boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginLayout = findViewById(R.id.loginLayout);
        //sign_in = findViewById(R.id.btnSignIn);
        sign_up = findViewById(R.id.btnRegister);
        emailView = findViewById(R.id.emailView);
        passwordView = findViewById(R.id.passwordView);
        forgetPassword = findViewById(R.id.forgetPassword);
        buttonLoading = findViewById(R.id.btnSignIn);

        //buttonLoading.setOnClickListener(this);
        sign_up.setOnClickListener(this);

        buttonLoading.setOnButtonLoadingListener(new ButtonLoading.OnButtonLoadingListener() {
            @Override
            public void onClick() {
                if (ConnectivityReceiver.isConnected(Login.this)){
                    if (validCredentials()) {
                        login(emailView.getText().toString(), passwordView.getText().toString());
                    } else {
                        String alert = getResources().getString(R.string.wrong_login);
                        AlertFragment.createFragment(alert).show(getFragmentManager(), null);
                        buttonLoading.setProgress(false);
                    }
                }  else {
                    Snackbar.make(loginLayout, R.string.checkingNetwork, Snackbar.LENGTH_INDEFINITE).show();
                    buttonLoading.setProgress(false);
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        RezetopiaApp.getInstance().setConnectivityListener(this);
    }

    private void login(String email, String password){
        UserOperations.login(email, password);
        UserOperations.setLoginCallback(new UserOperations.LoginCallback() {
            @Override
            public void onSuccess(String id) {
                getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                        .edit().putString(AppConfig.LOGGED_IN_USER_ID_SHARED, id).apply();
                startActivity(new Intent(Login.this, Main.class));
                finish();
            }

            @Override
            public void onError(int error) {
                String s = getResources().getString(error);
                Snackbar.make(loginLayout, s, Snackbar.LENGTH_INDEFINITE).show();
                buttonLoading.setProgress(false);
            }
        });
    }

    private void startRegister(){
        startActivity(new Intent(this, Registration.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignIn:
                break;
            case R.id.btnRegister:
                startRegister();
                break;
            case R.id.forgetPassword:
                break;
           default:
               break;
        }
    }

    private boolean validCredentials(){
        if (!emailView.getText().toString().isEmpty() && !passwordView.getText().toString().isEmpty()){
            return true;
        }

        return false;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        connected = isConnected;
    }
}
