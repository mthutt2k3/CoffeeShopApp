package com.android.coffeeshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.android.coffeeshop.R;

public class HomeStoreKeeperActivity extends BaseActivity {

    Button staffManagementButton, financialReportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDatas();

        staffManagementButton.setOnClickListener(v -> {
            navigateToStaffManagement();
        });

        financialReportButton.setOnClickListener(v -> {
            navigateToFinancialReport();
        });
    }

    private void navigateToStaffManagement() {
        Intent intent = new Intent(this, StaffListStorekeeperActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Navigating to Staff Management", Toast.LENGTH_SHORT).show();
    }

    private void navigateToFinancialReport() {
        Intent intent = new Intent(HomeStoreKeeperActivity.this, FinancialReportActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Navigating to Financial Report", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home_store_keeper;
    }

    private void initDatas() {
        staffManagementButton = findViewById(R.id.btn_manage_staff);
        financialReportButton = findViewById(R.id.btn_financial_report);
    }
}