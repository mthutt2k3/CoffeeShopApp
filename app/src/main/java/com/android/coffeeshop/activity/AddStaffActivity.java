package com.android.coffeeshop.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.android.coffeeshop.R;
import com.android.coffeeshop.adapter.RoleSpinnerAdapter;
import com.android.coffeeshop.entity.User;
import com.android.coffeeshop.viewmodel.StaffViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddStaffActivity extends BaseActivity {

    private EditText edtName, edtSalary, edtPassword, edtEmail, edtMobile;
    private Spinner spinnerRole;
    private Button btnAdd, btnCancel;
    private ImageButton btnBack;
    private StaffViewModel staffViewModel;
    private RoleSpinnerAdapter roleAdapter;
    private List<String> roleNames = new ArrayList<>();
    private List<Integer> roleIds = new ArrayList<>();
    private int selectedRoleId = 3; // Default to Employee

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        setupViewModel();
        loadRoles();
        setupListeners();
    }

    private void initViews() {
        edtName = findViewById(R.id.edtName);
        edtSalary = findViewById(R.id.edtSalary);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        edtMobile = findViewById(R.id.edtMobile);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupViewModel() {
        staffViewModel = new ViewModelProvider(this).get(StaffViewModel.class);
    }

    private void loadRoles() {
        // Add roles that are valid for staff (Manager and Employee)
        roleNames.add("Employee");
        roleIds.add(3);
        roleNames.add("Manager");
        roleIds.add(2);

        roleAdapter = new RoleSpinnerAdapter(this, roleNames, roleIds);
        spinnerRole.setAdapter(roleAdapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnCancel.setOnClickListener(v -> finish());

        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRoleId = roleAdapter.getRoleId(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Default to Employee
                selectedRoleId = 3;
            }
        });

        btnAdd.setOnClickListener(v -> {
            if (validateInput()) {
                saveStaff();
            }
        });
    }

    private boolean validateInput() {
        String name = edtName.getText().toString().trim();
        String salary = edtSalary.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String mobile = edtMobile.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            edtName.setError("Name is required");
            return false;
        }

        if (TextUtils.isEmpty(salary)) {
            edtSalary.setError("Salary is required");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Password is required");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Email is required");
            return false;
        }

        if (TextUtils.isEmpty(mobile)) {
            edtMobile.setError("Mobile number is required");
            return false;
        }
        if (mobile.length() != 10 || !mobile.matches("\\d+")) {
            edtMobile.setError("Mobile number must be 10 digits");
            return false;
        }

        // Check if email already exists
        User existingUser = staffViewModel.getUserByEmail(email);
        if (existingUser != null) {
            edtEmail.setError("Email already in use");
            return false;
        }

        // Username will be generated from email (part before @)
        String username = email.split("@")[0];
        User existingUsername = staffViewModel.getUserByUsername(username);
        if (existingUsername != null) {
            edtEmail.setError("Username would conflict. Try another email");
            return false;
        }

        return true;
    }

    private void saveStaff() {
        String name = edtName.getText().toString().trim();
        double salary = Double.parseDouble(edtSalary.getText().toString().trim());
        String password = edtPassword.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String mobile = edtMobile.getText().toString().trim();
        String username = email.split("@")[0]; // Generate username from email

        User newStaff = new User();
        newStaff.setFullName(name);
        newStaff.setSalary(salary);
        newStaff.setPassword(password);
        newStaff.setEmail(email);
        newStaff.setPhoneNumber(mobile);
        newStaff.setUserName(username);
        newStaff.setActive(true);

        staffViewModel.insertStaff(newStaff, selectedRoleId);
        Toast.makeText(this, "Staff added successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_staff;
    }
}