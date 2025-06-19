package com.android.coffeeshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.android.coffeeshop.R;

public class HomeManagerActivity extends BaseActivity {
    CardView productCard, categoryCard, employeeCard, workScheduleCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_home_manager);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        initDatas();

        productCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomeManagerActivity.this, ProductListManagerActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Navigating to Product Management", Toast.LENGTH_SHORT).show();
        });

        categoryCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomeManagerActivity.this, CategoryListManagerActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Navigating to Category Management", Toast.LENGTH_SHORT).show();
        });

        employeeCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomeManagerActivity.this, EmployeeListActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Navigating to Employee Management", Toast.LENGTH_SHORT).show();
        });

        workScheduleCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomeManagerActivity.this, ViewScheduleListActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Navigating to Work Schedule Card Management", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home_manager;
    }

    void initDatas() {
        productCard = findViewById(R.id.productCard);
        categoryCard = findViewById(R.id.categoryCard);
        employeeCard = findViewById(R.id.employeeCard);
        workScheduleCard = findViewById(R.id.workScheduleCard);
    }
}
