package com.android.coffeeshop.entity;

import com.google.firebase.firestore.PropertyName;
import com.google.firebase.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @PropertyName("orderId")
    private int orderId;

    @PropertyName("userId")
    @NonNull
    private int userId;

    @PropertyName("totalQuantity")
    @NonNull
    private float totalQuantity;

    @PropertyName("totalPrice")
    @NonNull
    private float totalPrice;

    @PropertyName("status")
    private String status;

    @PropertyName("customer")
    private String customer;

    @PropertyName("paymentStatus")
    private String paymentStatus;

    @PropertyName("createAt")
    @NonNull
    private Timestamp createAt;
}