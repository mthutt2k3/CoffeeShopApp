package com.android.coffeeshop.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.android.coffeeshop.entity.OrderDetail;

import java.util.List;

public interface OrderDetailDao {
    int countOrderDetailsByProductId(int productId);

    List<OrderDetail> getOrderDetailsByOrderId(int orderId);
}