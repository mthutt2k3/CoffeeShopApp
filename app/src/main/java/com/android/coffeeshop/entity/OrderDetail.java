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
    private int orderId;

    @PropertyName("productId")
    private int productId;

    @PropertyName("price")
    private double price;

    @PropertyName("quantity")
    private int quantity;
}