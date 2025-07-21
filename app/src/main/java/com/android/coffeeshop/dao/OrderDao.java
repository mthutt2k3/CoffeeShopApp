package com.android.coffeeshop.dao;

import androidx.lifecycle.LiveData;
import com.android.coffeeshop.entity.Order;
import com.android.coffeeshop.utils.DailyOrderStats;

import java.util.Date;
import java.util.List;

public interface OrderDao {
        LiveData<List<Order>> getOrdersCreateByEmployee(int employeeId);

        Order getOrderById(int orderId);

        LiveData<List<Order>> getOrdersByDateRange(Date startDate, Date endDate);

        LiveData<List<DailyOrderStats>> getDailyOrderStatsByDateRange(Date startDate, Date endDate);

        LiveData<Float> getTotalSalesByDateRange(Date startDate, Date endDate);

        int countOrdersByUserId(int userId);
}