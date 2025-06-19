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
import com.android.coffeeshop.utils.StaffWithRole;
import com.android.coffeeshop.viewmodel.StaffViewModel;

import java.util.ArrayList;
import java.util.List;

public class EditStaffActivity extends BaseActivity {

    private EditText edtName, edtSalary, edtPassword, edtEmail, edtMobile;
    private Spinner spinnerRole;
    private Button btnSave, btnCancel;
    private ImageButton btnBack;
    private StaffViewModel staffViewModel;
    private RoleSpinnerAdapter roleAdapter;
    private List<String> roleNames = new ArrayList<>();
    private List<Integer> roleIds = new ArrayList<>();
    private int selectedRoleId = 3; // Default to Employee
    private int userId;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        setupViewModel();
        getIntentData();
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
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnBack = findViewById(R.id.btnBack);

        // Set button text to Save instead of Add
        btnSave.setText("Save");
    }

    private void setupViewModel() {
        staffViewModel = new ViewModelProvider(this).get(StaffViewModel.class);
    }

    private void getIntentData() {
        userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(this, "Error: Staff ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Get staff information
        currentUser = staffViewModel.getUserById(userId);
        if (currentUser == null) {
            Toast.makeText(this, "Error: Staff not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Get role information
        StaffWithRole staffWithRole = staffViewModel.getStaffById(userId);
        if (staffWithRole != null) {
            edtName.setText(staffWithRole.getFullName());
            edtSalary.setText(String.valueOf(staffWithRole.getSalary()));
            edtEmail.setText(staffWithRole.getEmail());
            edtMobile.setText(currentUser.getPhoneNumber());
            edtPassword.setText(currentUser.getPassword());

            // Determine the role ID from role name
            String roleName = staffWithRole.getRoleName();
            if ("Manager".equals(roleName)) {
                selectedRoleId = 2;
            } else {
                selectedRoleId = 3; // Default to Employee
            }
        }
    }

    private void loadRoles() {
        // Add roles that are valid for staff (Manager and Employee)
        roleNames.add("Employee");
        roleIds.add(3);
        roleNames.add("Manager");
        roleIds.add(2);

        roleAdapter = new RoleSpinnerAdapter(this, roleNames, roleIds);
        spinnerRole.setAdapter(roleAdapter);

        // Set selected role
        for (int i = 0; i < roleIds.size(); i++) {
            if (roleIds.get(i) == selectedRoleId) {
                spinnerRole.setSelection(i);
                break;
            }
        }
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

        btnSave.setOnClickListener(v -> {
            if (validateInput()) {
                updateStaff();
            }
        });
    }

    private boolean validateInput() {
        String name = edtName.getText().toString().trim();
        String salary = edtSalary.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

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

        return true;
    }

    private void updateStaff() {
        String name = edtName.getText().toString().trim();
        double salary = Double.parseDouble(edtSalary.getText().toString().trim());
        String mobile = edtMobile.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Update user object
        currentUser.setFullName(name);
        currentUser.setSalary(salary);
        currentUser.setPhoneNumber(mobile);
        currentUser.setPassword(password);

        staffViewModel.updateStaff(currentUser, selectedRoleId);
        Toast.makeText(this, "Staff updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_edit_staff;
    }
}