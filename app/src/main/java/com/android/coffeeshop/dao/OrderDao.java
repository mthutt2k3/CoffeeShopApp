package com.android.coffeeshop.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.android.coffeeshop.entity.Order;
import com.android.coffeeshop.utils.DailyOrderStats;

import java.util.Date;
import java.util.List;

@Dao
public interface OrderDao {
        @Query("SELECT * FROM `order`" +
                        "WHERE user_id = :employeeId")
        LiveData<List<Order>> getOrdersCreateByEmployee(int employeeId);

        @Query("SELECT * FROM `order`" +
                        "WHERE order_id = :orderId")
        Order getOrderById(int orderId);

        @Query("SELECT * FROM `order` WHERE create_at BETWEEN :startDate AND :endDate ORDER BY create_at ASC")
        LiveData<List<Order>> getOrdersByDateRange(Date startDate, Date endDate);

        @Query("SELECT strftime('%Y-%m-%d', create_at) as orderDate, SUM(total_price) as totalSales, COUNT(*) as orderCount "
                        +
                        "FROM `order` " +
                        "WHERE create_at BETWEEN :startDate AND :endDate " +
                        "GROUP BY strftime('%Y-%m-%d', create_at) " +
                        "ORDER BY orderDate ASC")
        LiveData<List<DailyOrderStats>> getDailyOrderStatsByDateRange(Date startDate, Date endDate);

        @Query("SELECT SUM(total_price) FROM `order` WHERE create_at BETWEEN :startDate AND :endDate")
        LiveData<Float> getTotalSalesByDateRange(Date startDate, Date endDate);

        @Query("SELECT COUNT(*) FROM `order` WHERE user_id = :userId")
        int countOrdersByUserId(int userId);
}
