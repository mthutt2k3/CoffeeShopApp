package com.android.coffeeshop.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.coffeeshop.R;

public class HomeEmployeeActivity extends BaseActivity {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home_employee;
    }
    CardView productCard, categoryCard, profileCard, workScheduleCard, orderCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initDatas();

        productCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomeEmployeeActivity.this, ProductListEmployeeActivity.class);
            startActivity(intent);
        });

        categoryCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomeEmployeeActivity.this, CategoryListEmployeeActivity.class);
            startActivity(intent);
        });

        profileCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomeEmployeeActivity.this, EditUserProfileActivity.class);
            startActivity(intent);
        });

        workScheduleCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomeEmployeeActivity.this, ViewEmployeeScheduleActivity.class);
            startActivity(intent);
        });

        orderCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomeEmployeeActivity.this, OrderListEmployeeActivity.class);
            startActivity(intent);
        });
    }

    void initDatas() {
        productCard = findViewById(R.id.productCard);
        categoryCard = findViewById(R.id.categoryCard);
        profileCard = findViewById(R.id.profileCard);
        workScheduleCard = findViewById(R.id.workScheduleCard);
        orderCard = findViewById(R.id.orderCard);
    }
}