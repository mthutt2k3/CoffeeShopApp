package com.android.coffeeshop.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.android.coffeeshop.entity.Product;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirestoreProductDao implements ProductDao, BaseDao {
    private final FirebaseFirestore db;

    public FirestoreProductDao(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public LiveData<List<Product>> getProducts() {
        MutableLiveData<List<Product>> liveData = new MutableLiveData<>();
        db.collection("products").orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Product> products = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Product product = doc.toObject(Product.class);
                        if (product != null) {
                            product.setProductId(Integer.parseInt(doc.getId()));
                        }
                        products.add(product);
                    }
                    liveData.setValue(products);
                });
        return liveData;
    }

    @Override
    public void insertProduct(Product product) {
        executeFirestoreTaskVoid(db.collection("products").document(String.valueOf(product.getProductId())).set(product));
    }

    @Override
    public void updateProduct(Product updatedProduct) {
        executeFirestoreTaskVoid(db.collection("products").document(String.valueOf(updatedProduct.getProductId())).set(updatedProduct));
    }

    @Override
    public LiveData<List<Product>> getProductsByCategoryId(int categoryId) {
        MutableLiveData<List<Product>> liveData = new MutableLiveData<>();
        db.collection("products").whereEqualTo("categoryId", categoryId)
                .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Product> products = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Product product = doc.toObject(Product.class);
                        if (product != null) {
                            product.setProductId(Integer.parseInt(doc.getId()));
                        }
                        products.add(product);
                    }
                    liveData.setValue(products);
                });
        return liveData;
    }

    @Override
    public Product getProductsById(int productId) {
        DocumentSnapshot doc = executeFirestoreTask(db.collection("products").document(String.valueOf(productId)).get());
        if (doc != null) {
            Product product = doc.toObject(Product.class);
            if (product != null) {
                product.setProductId(Integer.parseInt(doc.getId()));
            }
            return product;
        }
        return null;
    }

    @Override
    public int countProductsByCategoryId(int categoryId) {
        QuerySnapshot snapshot = executeFirestoreTask(db.collection("products").whereEqualTo("categoryId", categoryId).get());
        return snapshot != null ? snapshot.size() : 0;
    }

    @Override
    public void deleteProduct(int productId) {
        executeFirestoreTaskVoid(db.collection("products").document(String.valueOf(productId)).delete());
    }
}