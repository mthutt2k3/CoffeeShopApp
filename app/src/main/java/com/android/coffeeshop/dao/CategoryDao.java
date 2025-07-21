package com.android.coffeeshop.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.android.coffeeshop.entity.Category;

import java.util.List;

public interface CategoryDao {
    LiveData<List<Category>> getCategories();

    Category getCategoryById(int id);

    void deleteCategory(int categoryId);

    LiveData<List<Category>> getAllCategories();

    void insert(Category category);
    void update(Category category);
    LiveData<List<Category>> getCategoriesListByName(String name);
}
