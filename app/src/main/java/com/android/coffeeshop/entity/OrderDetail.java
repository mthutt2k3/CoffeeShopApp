package com.android.coffeeshop.entity;

import com.google.firebase.firestore.PropertyName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    @PropertyName("orderDetailId")
    private int orderDetailId;

    @PropertyName("orderId")
    @NonNull
    private int orderId;

    @PropertyName("productId")
    @NonNull
    private int productId;

    @PropertyName("price")
    @NonNull
    private double price;

    @PropertyName("quantity")
    @NonNull
    private int quantity;
}