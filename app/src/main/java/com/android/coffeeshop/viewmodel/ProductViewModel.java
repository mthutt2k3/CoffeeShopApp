package com.android.coffeeshop.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.android.coffeeshop.entity.Product;
import com.android.coffeeshop.repository.ProductRepository;

import java.util.List;
import java.util.function.Consumer;

public class ProductViewModel extends AndroidViewModel {
    private ProductRepository productRepository;

    private LiveData<List<Product>> products;

    public ProductViewModel(Application application) {
        super(application);
        productRepository = new ProductRepository(application);
    }

    public LiveData<List<Product>> getProducts() {
        products = productRepository.getProducts();
        return products;
    }

    public LiveData<List<Product>> getProductsByCategoryId(int categoryId, List<Product> productss) {
        products = (LiveData<List<Product>>) productRepository.getProductsByCategoryId(categoryId, productss);
//        products = productRepository.getProductsByCategoryId(categoryId);
        return (LiveData<List<Product>>) products;
    }

    public void deleteProduct(int productId, Consumer<Boolean> callback) {
        productRepository.deleteProduct(productId, callback);
    }

    public void addProduct(Product product) {
        productRepository.addProduct(product);
    }

    public void updateProduct(Product updatedProduct) {
        productRepository.updateProduct(updatedProduct);
    }

    public Product getProductById(int productId) {
        return productRepository.getProductById(productId);
    }
}



