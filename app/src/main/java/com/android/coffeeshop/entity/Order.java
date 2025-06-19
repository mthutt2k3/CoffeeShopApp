package com.android.coffeeshop.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "order", foreignKeys = @ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "user_id"))
public class Order {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "order_id")
    private  int orderId;

    @ColumnInfo(name = "user_id")
    @NonNull
    private  int userId;

    @ColumnInfo(name = "total_quantity")
    @NonNull
    private   float totalQuantity;

    @ColumnInfo(name = "total_price")
    @NonNull
    private   float totalPrice;

    @ColumnInfo(name = "status")
    private  String status;

    @ColumnInfo(name = "customer")
    private  String customer;

    @ColumnInfo(name = "payment_status")
    private  String paymentStatus;

    @ColumnInfo(name = "create_at")
    @NonNull
    private  Date createAt;
}