package com.android.coffeeshop.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.android.coffeeshop.entity.Product;

import java.util.List;

public interface ProductDao {
    LiveData<List<Product>> getProducts();

    void insertProduct(Product product);

    void updateProduct(Product updatedProduct);

    LiveData<List<Product>> getProductsByCategoryId(int categoryId);

    Product getProductsById(int productId);

    int countProductsByCategoryId(int categoryId);

    void deleteProduct(int productId);
}
