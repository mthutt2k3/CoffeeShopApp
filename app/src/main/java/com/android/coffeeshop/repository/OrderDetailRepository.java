package com.android.coffeeshop.repository;

import android.content.Context;

import com.android.coffeeshop.dao.AppDatabase;
import com.android.coffeeshop.dao.OrderDetailDao;
import com.android.coffeeshop.entity.OrderDetail;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderDetailRepository {
    private OrderDetailDao orderDetailDao;
    private ExecutorService executorService;

    public OrderDetailRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        orderDetailDao = db.orderDetailDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public List<OrderDetail> getOrderDetailByOrderId(int orderId) {
        return orderDetailDao.getOrderDetailsByOrderId(orderId);
    }
}
