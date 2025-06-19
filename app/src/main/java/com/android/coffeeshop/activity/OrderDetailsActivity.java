package com.android.coffeeshop.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.coffeeshop.R;
import com.android.coffeeshop.adapter.OrderAdapter;
import com.android.coffeeshop.entity.Order;
import com.android.coffeeshop.entity.OrderDetail;
import com.android.coffeeshop.utils.FormartCurrency;
import com.android.coffeeshop.viewmodel.OrderDetailViewModel;
import com.android.coffeeshop.viewmodel.OrderViewModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class OrderDetailsActivity extends BaseActivity {
    RecyclerView recyclerView;
    ImageView imageAvatar;
    TextView textViewOrderId, textViewOrderDate, textViewPaymentMethod, textViewUserName, textViewOrderStatus;
    TextView textViewEmail, textViewAddress, textViewPhoneNumber, textViewTotalPrice, textViewTotalPayment;
    OrderViewModel orderViewModel;
    OrderDetailViewModel orderDetailViewModel;
    OrderAdapter orderAdapter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_order_details;
    }

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
        renderOrder();
    }

    private void initData() {
        recyclerView = findViewById(R.id.recyclerViewOrderDetail);
        recyclerView.hasFixedSize();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        imageAvatar = findViewById(R.id.imageAvatar);
        textViewOrderId = findViewById(R.id.textViewOrderId);
        textViewOrderDate = findViewById(R.id.textViewOrderDate);
        textViewPaymentMethod = findViewById(R.id.textViewPaymentMethod);
        textViewUserName = findViewById(R.id.textViewUserName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewAddress = findViewById(R.id.textViewAddress);
        textViewPhoneNumber = findViewById(R.id.textViewPhoneNumber);
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        textViewTotalPayment = findViewById(R.id.textViewTotalPayment);
        textViewOrderStatus = findViewById(R.id.textViewOrderStatus);

        orderViewModel = new OrderViewModel(getApplication());
        orderDetailViewModel = new OrderDetailViewModel(getApplication());
    }

    private void renderOrder() {
        int orderId = getIntent().getIntExtra("ORDER_ID", -1);
        if (orderId == -1) {
            Toast.makeText(this, "Order not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Order order = orderViewModel.getOrderById(orderId);
        if (order == null) return;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(order.getCreateAt());

        // Set data for order
        textViewOrderId.setText(String.valueOf(order.getOrderId()));
        textViewOrderDate.setText(formattedDate);
        textViewPaymentMethod.setText(order.getPaymentStatus());
        Picasso.get()
                .load("https://fap.fpt.edu.vn/temp/ImageRollNumber/APHL/22758bf5-7d15-4cc8-84af-082e46fe42fd.jpg")
                .into(imageAvatar);
        textViewUserName.setText(order.getCustomer());
        textViewEmail.setText("khangBeoU@gmail.com");
        textViewAddress.setText("Ở đâu còn lâu mới nói");
        textViewPhoneNumber.setText("0123456789");
        textViewTotalPrice.setText(FormartCurrency.formatVNCurrency(order.getTotalPrice()));
        textViewTotalPayment.setText(FormartCurrency.formatVNCurrency(order.getTotalPrice()));
        textViewOrderStatus.setText(order.getStatus());

        List<OrderDetail> orderDetails = orderDetailViewModel
                .getOrderDetailsByOrderId(order.getOrderId());
        // Render recycler view
        orderAdapter = new OrderAdapter(orderDetails);
        recyclerView.setAdapter(orderAdapter);
    }

}