package com.android.coffeeshop.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.android.coffeeshop.dao.AppDatabase;
import com.android.coffeeshop.dao.OrderDao;
import com.android.coffeeshop.dao.OrderDetailDao;
import com.android.coffeeshop.entity.Order;
import com.android.coffeeshop.utils.DailyOrderStats;
import com.android.coffeeshop.utils.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderRepository {
    private OrderDao orderDao;
    private OrderDetailDao orderDetailDao;
    private ExecutorService executorService;

    public OrderRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        orderDao = db.orderDao();
        orderDetailDao = db.orderDetailDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Order>> getOrdersCreateByEmployee(int employeeId) {
        return orderDao.getOrdersCreateByEmployee(employeeId);
    }

    public Order getOrderById(int orderId) {
        return orderDao.getOrderById(orderId);
    }

    public LiveData<List<DailyOrderStats>> getDailyStatsForCurrentWeek() {
        Date startDate = DateUtils.getStartOfWeek();
        Date endDate = DateUtils.getEndOfWeek();
        return orderDao.getDailyOrderStatsByDateRange(startDate, endDate);
    }
}
