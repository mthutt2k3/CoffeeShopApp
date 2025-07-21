package com.android.coffeeshop.utils;

import androidx.room.ColumnInfo;

public class DailyOrderStats {
    @ColumnInfo(name = "orderDate")
    private String orderDate;

    @ColumnInfo(name = "totalSales")
    private float totalSales;

    @ColumnInfo(name = "orderCount")
    private int orderCount;

    public DailyOrderStats(String orderDate, float totalSales, int orderCount) {
        this.orderDate = orderDate;
        this.totalSales = totalSales;
        this.orderCount = orderCount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public float getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(float totalSales) {
        this.totalSales = totalSales;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }
}