package com.android.coffeeshop.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.android.coffeeshop.entity.Product;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.List;

public class FirestoreProductDao implements ProductDao {
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
        db.collection("products").document(String.valueOf(product.getProductId())).set(product);
    }

    @Override
    public void updateProduct(Product updatedProduct) {
        db.collection("products").document(String.valueOf(updatedProduct.getProductId())).set(updatedProduct);
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
        try {
            DocumentSnapshot doc = Tasks.await(db.collection("products").document(String.valueOf(productId)).get());
            Product product = doc.toObject(Product.class);
            if (product != null) {
                product.setProductId(Integer.parseInt(doc.getId()));
            }
            return product;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int countProductsByCategoryId(int categoryId) {
        try {
            QuerySnapshot snapshot = Tasks.await(db.collection("products").whereEqualTo("categoryId", categoryId).get());
            return snapshot.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void deleteProduct(int productId) {
        db.collection("products").document(String.valueOf(productId)).delete();
    }
}