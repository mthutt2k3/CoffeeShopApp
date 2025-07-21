package com.android.coffeeshop.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.android.coffeeshop.dao.AppDatabase;
import com.android.coffeeshop.dao.OrderDetailDao;
import com.android.coffeeshop.dao.ProductDao;
import com.android.coffeeshop.entity.Product;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ProductRepository {
    private ProductDao productDao;
    private OrderDetailDao orderDetailDao;
    private ExecutorService executorService;

    public ProductRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        productDao = db.productDao();
        orderDetailDao = db.orderDetailDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Product>> getProducts() {
        return productDao.getProducts();
    }

    public List<Product> getProductsByCategoryId(int categoryId, List<Product> products) {
        return (List<Product>) products.stream().filter(product -> product.getCategoryId() == categoryId);
    }

    public void addProduct(Product newProduct) {
        executorService.execute(() -> productDao.insertProduct(newProduct));
    }

    public LiveData<List<Product>> getProductsByCategoryId(int categoryId) {
        return productDao.getProductsByCategoryId(categoryId);
    }

    public void deleteProduct(int productId, Consumer<Boolean> callback) {
        executorService.execute(() -> {
            int orderDetailCount = orderDetailDao.countOrderDetailsByProductId(productId);
            boolean isDeleted = false;
            if (orderDetailCount == 0) {
                productDao.deleteProduct(productId);
                isDeleted = true;
            }
            callback.accept(isDeleted);
        });
    }

    public void updateProduct(Product updatedProduct) {
        executorService.execute(() -> productDao.updateProduct(updatedProduct));
    }

    public Product getProductById(int productId) {
        return productDao.getProductsById(productId);
    }
}
