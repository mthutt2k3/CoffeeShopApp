package com.android.coffeeshop.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.coffeeshop.R;
import com.android.coffeeshop.adapter.EmployeeAdapter;
import com.android.coffeeshop.entity.User;
import com.android.coffeeshop.viewmodel.EmployeeViewModel;

import java.util.List;

public class EmployeeListActivity extends BaseActivity {

    private RecyclerView rvEmployeeList;
    private Button btnAddEmployee;
    private EmployeeAdapter employeeAdapter;
    private TextView tvEmployeeCount;
    private EmployeeViewModel employeeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_employee_list);

        rvEmployeeList = findViewById(R.id.rvEmployeeList);
        btnAddEmployee = findViewById(R.id.btnAddEmployee);
        tvEmployeeCount = findViewById(R.id.tvEmployeeCount);

        rvEmployeeList.setLayoutManager(new LinearLayoutManager(this));

        employeeViewModel = new EmployeeViewModel(getApplication());

        employeeViewModel.getEmployees().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                employeeAdapter = new EmployeeAdapter(EmployeeListActivity.this, users,
                        EmployeeListActivity.this::showDeleteConfirmationDialog,
                        EmployeeListActivity.this::navigateToEditEmployeeScreen);
                rvEmployeeList.setAdapter(employeeAdapter);
                updateEmployeeCount(users);
            }
        });

        btnAddEmployee.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeListActivity.this, AddEmployeeActivity.class);
            startActivity(intent);
            Toast.makeText(EmployeeListActivity.this, "Navigating to Add Employee",
                    Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_employee_list;
    }

    @SuppressLint("SetTextI18n")
    private void updateEmployeeCount(List<User> users) {
        if (users.isEmpty()) {
            tvEmployeeCount.setText("No employees found.");
        } else {
            tvEmployeeCount.setText(users.size() + " Employees Found");
        }
    }

    private void showDeleteConfirmationDialog(int employeeId, String fullName) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Employee " + fullName)
                .setMessage("Do you want to delete " + fullName + " ?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    confirmDelete(employeeId);
                })
                .setNegativeButton("No", null)
                .show();
    }
    private void confirmDelete(int employeeId) {
        employeeViewModel.deleteEmployee(employeeId, isDeleted -> {
            runOnUiThread(() -> {
                if (isDeleted) {
                    Toast.makeText(this, "Employee deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Cannot delete employee. It has existing orders or schedules.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
//    private void confirmDelete(int employeeId) {
//        employeeViewModel.deleteEmployee(employeeId);
//        employeeViewModel.getEmployees().observe(this, users -> {
//            employeeAdapter = new EmployeeAdapter(EmployeeListActivity.this, users,
//                    EmployeeListActivity.this::showDeleteConfirmationDialog,
//                    EmployeeListActivity.this::navigateToEditEmployeeScreen);
//            rvEmployeeList.setAdapter(employeeAdapter);
//            updateEmployeeCount(users);
//        });
//        Toast.makeText(this, "Employee deleted successfully", Toast.LENGTH_SHORT).show();
//    }


    private void navigateToEditEmployeeScreen(int employeeId, String fullName, String userName
            , String email, String phoneNumber, String password, String position, boolean isActive) {
        Intent intent = new Intent(EmployeeListActivity.this, EditEmployeeActivity.class);
        intent.putExtra("EMPLOYEE_ID", employeeId);
        intent.putExtra("FULL_NAME", fullName);
        intent.putExtra("USER_NAME", userName);
        intent.putExtra("EMAIL", email);
        intent.putExtra("PHONE_NUMBER", phoneNumber);
        intent.putExtra("PASSWORD", password);
        intent.putExtra("POSITION", position);
        intent.putExtra("ACTIVE", isActive);
        startActivity(intent);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        employeeViewModel.getEmployees().observe(this, users -> {
//            employeeAdapter = new EmployeeAdapter(EmployeeListActivity.this, users,
//                    EmployeeListActivity.this::showDeleteConfirmationDialog,
//                    EmployeeListActivity.this::navigateToEditEmployeeScreen);
//            rvEmployeeList.setAdapter(employeeAdapter);
//            updateEmployeeCount(users);
//        });
//    }
}
