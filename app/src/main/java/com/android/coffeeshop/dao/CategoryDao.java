package com.android.coffeeshop.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.android.coffeeshop.entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT category.* " +
            "FROM category ")
    LiveData<List<Category>> getCategories();

    @Query("SELECT * FROM category WHERE category_id = :id")
    Category getCategoryById(int id);

    @Query("DELETE FROM category WHERE category_id = :categoryId")
    void deleteCategory(int categoryId);

    @Query("SELECT * FROM category")
    LiveData<List<Category>> getAllCategories();

    @Insert
    void insert(Category category);
    @Update
    void update(Category category);
    @Query("SELECT category.* FROM category WHERE category_name = :name")
    LiveData<List<Category>> getCategoriesListByName(String name);
}
