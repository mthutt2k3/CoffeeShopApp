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
@Entity(tableName = "product", foreignKeys = @ForeignKey(entity = Category.class, parentColumns = "category_id", childColumns = "category_id"))
public class Product {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    int productId;

    @ColumnInfo(name = "category_id")
    @NonNull
    int categoryId;

    @ColumnInfo(name = "product_name")
    @NonNull
    String productName;

    @ColumnInfo(name = "product_recipes")
    String productRecipes;

    @ColumnInfo(name = "product_price")
    @NonNull
    double productPrice;

    @ColumnInfo(name = "stock_quantity")
    @NonNull
    int stockQuantity;

    @ColumnInfo(name = "product_image")
    String productImage;

    @ColumnInfo(name = "create_at")
    @NonNull
    Date createdAt;

    @ColumnInfo(name = "status")
    @NonNull
    boolean status = true;
}
