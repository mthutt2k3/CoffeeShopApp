package com.android.coffeeshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.coffeeshop.R;
import com.android.coffeeshop.entity.Order;
import com.android.coffeeshop.utils.FormartCurrency;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lombok.NonNull;

public class OrderEmployeeAdapter extends RecyclerView.Adapter<OrderEmployeeAdapter.MyViewHolder> {
    private static List<Order> orders;
    Context context;
    private OnItemClickListener onItemClickListener;

    public OrderEmployeeAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @androidx.annotation.NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from((parent.getContext())).inflate(R.layout.order_card_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull MyViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.tvOrderId.setText(String.valueOf(order.getOrderId()));
        holder.tvCustomer.setText(order.getCustomer());
        holder.tvPrice.setText(FormartCurrency.formatVNCurrency(order.getTotalPrice()));
        holder.tvQuantity.setText(String.valueOf(order.getTotalQuantity()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(order.getCreateAt());
        holder.tvDate.setText(formattedDate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(order);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Order order);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvOrderId, tvPrice, tvQuantity, tvDate, tvCustomer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.textViewOrderId);
            tvPrice = itemView.findViewById(R.id.textViewPrice);
            tvQuantity = itemView.findViewById(R.id.textViewQuantity);
            tvDate = itemView.findViewById(R.id.textViewDate);
            tvCustomer = itemView.findViewById(R.id.textViewCustomer);
        }
    }

    public static List<Order> getListOrder() {
        return orders;
    }

    public static void setOrderList(List<Order> orderList) {
        OrderEmployeeAdapter.orders = orderList;
    }

    public static void filter(String text) {
        List<Order> filteredList = new ArrayList<>();
        for (Order order : orders) {
            if (order.getCustomer().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(order);
            }
        }
        filterList(filteredList);
    }

    public static void filterList(List<Order> filteredList) {
        orders = filteredList;
    }
}
