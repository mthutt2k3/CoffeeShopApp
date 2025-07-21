package com.android.coffeeshop.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.android.coffeeshop.entity.Category;
import com.android.coffeeshop.repository.CategoryRepository;
import com.android.coffeeshop.repository.ProductRepository;

import java.util.List;
import java.util.function.Consumer;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    private LiveData<List<Category>> categories;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
        productRepository = new ProductRepository(application);
    }

    public LiveData<List<Category>> getCategories() {
        categories = categoryRepository.getCategories();
        return categories;
    }

    public LiveData<List<Category>> getCategorieList() {
        return categoryRepository.getCategorieList();
    }

    public Category getCategoryById(int id) {
        return categoryRepository.getCategoryById(id);
    }

    public void deleteCategory(int categoryId, Consumer<Boolean> callback) {
        categoryRepository.deleteCategory(categoryId, callback);
    }
    public void addCategory(Category category) {
        categoryRepository.addCategory(category);
    }
    public void updateCategory(Category category) {
        categoryRepository.updateCategory(category);
    }
    public LiveData<List<Category>> getCategoryListByName(String name) {
        return categoryRepository.getCategoriesListByName(name);
    }
}
