package com.android.coffeeshop.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.coffeeshop.R;
import com.android.coffeeshop.adapter.OrderEmployeeAdapter;
import com.android.coffeeshop.entity.Order;
import com.android.coffeeshop.viewmodel.OrderViewModel;
import com.android.coffeeshop.viewmodel.UserViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class OrderListEmployeeActivity extends BaseActivity {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_order_list_employee;
    }

    RecyclerView recyclerView;
    List<Order> orders = new ArrayList<>();
    private OrderViewModel orderViewModel;
    private UserViewModel userViewModel;
    OrderEmployeeAdapter orderAdapter;
    View rootView;
    TextInputEditText searchBox;
    ShimmerFrameLayout skeletonLayout;
    private ShimmerFrameLayout skele;
    private int employeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initData();
        addEvents();

        getOrderRequest();

        skeletonLayout.startShimmer();
    }

    private void initData() {
        skeletonLayout = findViewById(R.id.skeletonLayout);

        recyclerView = findViewById(R.id.orderRecyclerView);
        recyclerView.hasFixedSize();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        searchBox = findViewById(R.id.searchEditText);

        orderViewModel = new OrderViewModel(getApplication());
        userViewModel = new UserViewModel(getApplication());

        // Lấy username từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        employeeId = userViewModel.getUserByUserName(username).getUserId();
    }

    private void addEvents() {
        searchOrder();
    }

    private void getOrderRequest() {
        orderViewModel.getOrdersCreateByEmployee(employeeId).observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orderList) {
                orders = new ArrayList<>(orderList);
                renderOrders();
            }
        });
    }

    private void renderOrders() {
        orderAdapter = new OrderEmployeeAdapter(orders, this);

        // Set click listener
        orderAdapter.setOnItemClickListener(order -> {
            Intent intent = new Intent(OrderListEmployeeActivity.this, OrderDetailsActivity.class);
            intent.putExtra("ORDER_ID", order.getOrderId()); // Pass orderId
            startActivity(intent);
        });

        recyclerView.setAdapter(orderAdapter);

        skeletonLayout.stopShimmer();
        skeletonLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void searchOrder() {
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                orderAdapter.setOrderList(orders);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = editable.toString().trim();

                orderAdapter.filter(searchText);
                orderAdapter.notifyDataSetChanged();
            }
        });
    }
}