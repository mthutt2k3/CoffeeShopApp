package com.android.coffeeshop.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.android.coffeeshop.entity.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT product.* " +
            "FROM product " +
            "INNER JOIN category ON product.category_id = category.category_id " +
            "ORDER BY create_at DESC")
    LiveData<List<Product>> getProducts();

    @Insert
    void insertProduct(Product product);

    @Update
    void updateProduct(Product updatedProduct);

    @Query("SELECT product.* " +
            "FROM product " +
            "INNER JOIN category ON product.category_id = category.category_id " +
            "WHERE category.category_id = :categoryId " +
            "ORDER BY create_at ASC")
    LiveData<List<Product>> getProductsByCategoryId(int categoryId);

    @Query("SELECT product.* " +
            "FROM product " +
            "WHERE product.product_id = :productId " +
            "ORDER BY create_at ASC")
    Product getProductsById(int productId);

    @Query("SELECT COUNT(*) FROM product WHERE category_id = :categoryId")
    int countProductsByCategoryId(int categoryId);

    @Query("DELETE FROM product WHERE product_id = :productId")
    void deleteProduct(int productId);
}
