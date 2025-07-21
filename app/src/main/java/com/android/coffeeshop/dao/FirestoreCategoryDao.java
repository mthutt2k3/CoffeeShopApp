package com.android.coffeeshop.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.android.coffeeshop.entity.Category;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.List;

public class FirestoreCategoryDao implements CategoryDao {
    private final FirebaseFirestore db;

    public FirestoreCategoryDao(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public LiveData<List<Category>> getCategories() {
        MutableLiveData<List<Category>> liveData = new MutableLiveData<>();
        db.collection("categories").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Category> categories = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Category category = doc.toObject(Category.class);
                        if (category != null) {
                            category.setCategoryId(Integer.parseInt(doc.getId()));
                        }
                        categories.add(category);
                    }
                    liveData.setValue(categories);
                });
        return liveData;
    }

    @Override
    public Category getCategoryById(int id) {
        try {
            // Sử dụng Tasks.await() để chờ kết quả đồng bộ
            DocumentSnapshot doc = Tasks.await(db.collection("categories").document(String.valueOf(id)).get());
            Category category = doc.toObject(Category.class);
            if (category != null) {
                category.setCategoryId(Integer.parseInt(doc.getId()));
            }
            return category;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteCategory(int categoryId) {
        db.collection("categories").document(String.valueOf(categoryId)).delete();
    }

    @Override
    public LiveData<List<Category>> getAllCategories() {
        return getCategories(); // Tương tự getCategories
    }

    @Override
    public void insert(Category category) {
        db.collection("categories").document(String.valueOf(category.getCategoryId())).set(category);
    }

    @Override
    public void update(Category category) {
        db.collection("categories").document(String.valueOf(category.getCategoryId())).set(category);
    }

    @Override
    public LiveData<List<Category>> getCategoriesListByName(String name) {
        MutableLiveData<List<Category>> liveData = new MutableLiveData<>();
        db.collection("categories").whereEqualTo("categoryName", name).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Category> categories = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Category category = doc.toObject(Category.class);
                        if (category != null) {
                            category.setCategoryId(Integer.parseInt(doc.getId()));
                        }
                        categories.add(category);
                    }
                    liveData.setValue(categories);
                });
        return liveData;
    }
}