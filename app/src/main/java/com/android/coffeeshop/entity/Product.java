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
public class Product {
    @PropertyName("productId")
    private int productId;

    @PropertyName("categoryId")
    private int categoryId;

    @PropertyName("productName")
    private String productName;

    @PropertyName("productRecipes")
    private String productRecipes;

    @PropertyName("productPrice")
    private double productPrice;

    @PropertyName("stockQuantity")
    private int stockQuantity;

    @PropertyName("productImage")
    private String productImage;

    @PropertyName("createdAt")
    @NonNull
    private Timestamp createdAt;

    @PropertyName("status")
    private boolean status = true;
}