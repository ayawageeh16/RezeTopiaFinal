package io.krito.com.reze.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import io.krito.com.reze.R;
import io.krito.com.reze.application.AppConfig;
import io.krito.com.reze.fragments.AlertFragment;
import io.krito.com.reze.models.operations.UserOperations;
import io.krito.com.reze.models.pojo.User;
import io.krito.com.reze.views.CustomButton;
import io.krito.com.reze.views.CustomEditText;

public class Registration extends AppCompatActivity implements View.OnClickListener {
    CustomEditText edtName, edtPhone, edtEmail, edtDate, edtPassword;
    CustomButton btnSignUp, btnLogin;
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
        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnLogin:
                Intent loginIntent = new Intent(this, Login.class);
                startActivity(loginIntent);

                break;
            case R.id.btnRegister:
                if (checkData()) {

                    final User user = new User();
                    user.setName(edtName.getText().toString());
                    user.setEmail(edtEmail.getText().toString());
                    user.setPassword(edtPassword.getText().toString());
                    user.setBirthday(edtDate.getText().toString());
                    user.setMobile(edtPassword.getText().toString());
                    UserOperations.Register(user);
                    UserOperations.setRegisterCallback(new UserOperations.RegisterCallback() {
                        @Override
                        public void onSuccess(String id) {
                            getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                                    .edit().putString(AppConfig.LOGGED_IN_USER_ID_SHARED, id).apply();
                            startActivity(new Intent(Registration.this, Main.class));
                            finish();
                        }

                        @Override
                        public void onError(int error) {
                            String s = getResources().getString(error);
                            AlertFragment.createFragment(s).show(getFragmentManager(), null);
                        }
                    });
                }
                break;
        }

    }


    private Boolean checkData() {
        String name, password, email, phone, dateOfBirth;
        Boolean valid = true;

        name = edtName.getText().toString();
        password = edtPassword.getText().toString();
        email = edtEmail.getText().toString();
        dateOfBirth = edtDate.getText().toString();
        phone = edtPhone.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            edtName.setError("Must be at least 3 characters ");
            valid = false;
        } else
            edtName.setError(null);

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
}
