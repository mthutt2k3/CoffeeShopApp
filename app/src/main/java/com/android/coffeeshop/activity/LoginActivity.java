package com.android.coffeeshop.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.android.coffeeshop.R;
import com.android.coffeeshop.utils.UserWithRole;
import com.android.coffeeshop.viewmodel.UserViewModel;

public class LoginActivity extends AppCompatActivity {
    private UserViewModel userViewModel;

    private EditText edLoginUserName;
    private EditText edLoginPassword;
    private CheckBox rememberMeCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        edLoginUserName = findViewById(R.id.edLoginUserName);
        edLoginPassword = findViewById(R.id.edLoginPassword);
        rememberMeCheckBox = findViewById(R.id.chkRememberMe);

        Button btnLogin = findViewById(R.id.btnLogin);
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");
        if (!savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            edLoginUserName.setText(savedUsername);
            edLoginPassword.setText(savedPassword);
            rememberMeCheckBox.setChecked(true);
        }

        btnLogin.setOnClickListener(v -> {
            String username = edLoginUserName.getText().toString();
            String password = edLoginPassword.getText().toString();
            if (username.isEmpty() || password.isEmpty()) {
                showErrorMessage("Please enter username and password");
                return;
            }

            UserWithRole user = userViewModel.login(username, password);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", username);
            if (rememberMeCheckBox.isChecked()) {

                editor.putString("username", username);
                editor.putString("password", password);
            } else {

                editor.remove("username");
                editor.remove("password");
            }
            editor.apply();

            if (user != null) {
                if (user.getActive() != 1) {
                    showErrorMessage("Your account is not active. Please contact the admin.");
                } else {
                    navigateToHomeScreen(user.getRoleName());
                }
            } else {
                showErrorMessage("Invalid username or password");
            }
        });
    }

    private void navigateToHomeScreen(String roleName) {
        if (roleName.equals("Employee")) {
            startActivity(new Intent(this, HomeEmployeeActivity.class));
        } else if (roleName.equals("Manager")) {
            startActivity(new Intent(this, HomeManagerActivity.class));
        } else {
            startActivity(new Intent(this, HomeStoreKeeperActivity.class));
        }
    }

    private void showErrorMessage(String invalidUsernameOrPassword) {
        Toast.makeText(this, invalidUsernameOrPassword, Toast.LENGTH_SHORT).show();
    }


}