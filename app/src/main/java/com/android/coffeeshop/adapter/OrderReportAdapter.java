package com.android.coffeeshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.coffeeshop.R;
import com.android.coffeeshop.entity.Order;
import com.android.coffeeshop.utils.DateUtils;
import com.android.coffeeshop.utils.FormartCurrency;

import java.util.ArrayList;
import java.util.List;

public class OrderReportAdapter extends RecyclerView.Adapter<OrderReportAdapter.OrderViewHolder> {

    private List<Order> orders;

    public OrderReportAdapter() {
        this.orders = new ArrayList<>();
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_report, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderId, tvOrderDate, tvQuantity, tvStatus, tvTotalPrice;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
        }

        public void bind(Order order) {
            tvOrderId.setText("#" + order.getOrderId());
            tvOrderDate.setText(DateUtils.formatDate(order.getCreateAt()));
            tvQuantity.setText(String.valueOf(order.getTotalQuantity()));
            tvStatus.setText(order.getPaymentStatus());

            // Màu trạng thái thanh toán
            if ("Đã thanh toán".equals(order.getPaymentStatus())) {
                tvStatus.setTextColor(itemView.getResources().getColor(android.R.color.holo_green_dark));
            } else {
                tvStatus.setTextColor(itemView.getResources().getColor(android.R.color.holo_red_dark));
            }

            tvTotalPrice.setText(FormartCurrency.formatVNCurrency(order.getTotalPrice()) + " VNĐ");
        }
    }
}