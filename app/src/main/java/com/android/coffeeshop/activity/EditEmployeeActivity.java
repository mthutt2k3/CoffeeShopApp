package com.android.coffeeshop.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.coffeeshop.R;
import com.android.coffeeshop.entity.User;
import com.android.coffeeshop.viewmodel.EmployeeViewModel;

public class EditEmployeeActivity extends BaseActivity {

    private EditText edtFullName, edtUserName, edtEmail, edtPhoneNumber, edtPassword, edtConfirmPassword;
    private Spinner spPosition;
    private Switch swActive;
    private Button btnSaveEmployee;
    private EmployeeViewModel employeeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_edit_employee);

        edtFullName = findViewById(R.id.edtFullName);
        edtUserName = findViewById(R.id.edtUserName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        swActive = findViewById(R.id.swActive);
        spPosition = findViewById(R.id.spPosition);
        btnSaveEmployee = findViewById(R.id.btnSaveEmployee);

        employeeViewModel = new EmployeeViewModel(getApplication());

        int employeeId = getIntent().getIntExtra("EMPLOYEE_ID", -1);
        String fullName = getIntent().getStringExtra("FULL_NAME");
        String userName = getIntent().getStringExtra("USER_NAME");
        String email = getIntent().getStringExtra("EMAIL");
        String phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");
        String password = getIntent().getStringExtra("PASSWORD");
        String position = getIntent().getStringExtra("POSITION");
        boolean isActive = getIntent().getBooleanExtra("ACTIVE", true);

        edtFullName.setText(fullName);
        edtUserName.setText(userName);
        edtEmail.setText(email);
        edtPhoneNumber.setText(phoneNumber);
        edtPassword.setText(password);
        edtConfirmPassword.setText(password);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.role_array_employee, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPosition.setAdapter(adapter);
        int positionIndex = adapter.getPosition(position);
        spPosition.setSelection(positionIndex);

        swActive.setChecked(isActive);
        btnSaveEmployee.setOnClickListener(v -> onSaveEmployeeClicked(employeeId, userName, email, phoneNumber));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_edit_employee;
    }

    private void onSaveEmployeeClicked(int employeeId, String oldUserName, String oldEmail, String oldPhone) {
        if (!validateRequiredFields() || !validatePasswordMatch() || !validateEmailFormat()
                || !validatePhoneFormat()) {
            return;
        }

        String fullName = edtFullName.getText().toString().trim();
        String userName = edtUserName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String position = spPosition.getSelectedItem().toString();
        boolean isActive = swActive.isChecked();

        if (!userName.equals(oldUserName) && employeeViewModel.isUsernameExists(userName)) {
            edtUserName.setError(getString(R.string.employees_validation_username_exists));
            return;
        }

        if (!email.equals(oldEmail) && employeeViewModel.isEmailExists(email)) {
            edtEmail.setError(getString(R.string.employees_validation_email_exists));
            return;
        }

        if (!phoneNumber.equals(oldPhone) && employeeViewModel.isPhoneExists(phoneNumber)) {
            edtPhoneNumber.setError(getString(R.string.employees_validation_phone_exists));
            return;
        }

        User updatedEmployee = new User();
        updatedEmployee.setUserId(employeeId);
        updatedEmployee.setFullName(fullName);
        updatedEmployee.setUserName(userName);
        updatedEmployee.setEmail(email);
        updatedEmployee.setPhoneNumber(phoneNumber);
        updatedEmployee.setPassword(password);
        updatedEmployee.setPosition(position);
        updatedEmployee.setActive(isActive);

        employeeViewModel.updateEmployee(updatedEmployee);
        Toast.makeText(this, "Employee updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean validateRequiredFields() {
        if (TextUtils.isEmpty(edtFullName.getText())) {
            edtFullName.setError(getString(R.string.employees_validation_empty_fields));
            return false;
        }
        if (TextUtils.isEmpty(edtUserName.getText())) {
            edtUserName.setError(getString(R.string.employees_validation_empty_fields));
            return false;
        }
        if (TextUtils.isEmpty(edtEmail.getText())) {
            edtEmail.setError(getString(R.string.employees_validation_empty_fields));
            return false;
        }
        if (TextUtils.isEmpty(edtPhoneNumber.getText())) {
            edtPhoneNumber.setError(getString(R.string.employees_validation_empty_fields));
            return false;
        }
        if (TextUtils.isEmpty(edtPassword.getText())) {
            edtPassword.setError(getString(R.string.employees_validation_empty_fields));
            return false;
        }
        if (TextUtils.isEmpty(edtConfirmPassword.getText())) {
            edtConfirmPassword.setError(getString(R.string.employees_validation_empty_fields));
            return false;
        }
        return true;
    }

    private boolean validatePasswordMatch() {
        if (!edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
            edtConfirmPassword.setError(getString(R.string.employees_validation_password_mismatch));
            return false;
        }
        return true;
    }

    private boolean validateEmailFormat() {
        String email = edtEmail.getText().toString().trim();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError(getString(R.string.employees_validation_invalid_email));
            return false;
        }
        return true;
    }

    private boolean validatePhoneFormat() {
        String phone = edtPhoneNumber.getText().toString().trim();
        if (!phone.matches("\\d{10}")) {
            edtPhoneNumber.setError(getString(R.string.employees_validation_invalid_phone));
            return false;
        }
        return true;
    }
}
