package com.android.coffeeshop.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import com.android.coffeeshop.R;
import com.android.coffeeshop.entity.Category;
import com.android.coffeeshop.viewmodel.CategoryViewModel;

import java.util.List;

public class AddCategoryActivity extends BaseActivity {
    private EditText edtCategoryName;
    private Button btnSaveCategory;
    private CategoryViewModel categoryViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        edtCategoryName = findViewById(R.id.categoryInput);
        btnSaveCategory = findViewById(R.id.saveButton);

        categoryViewModel = new CategoryViewModel(getApplication());
        btnSaveCategory.setOnClickListener(v -> onSaveCategoryClicked());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_category;
    }
    private void onSaveCategoryClicked() {
        String inputName = edtCategoryName.getText().toString().trim();
        if (inputName.isEmpty()) {
            edtCategoryName.setError("Category name is required");
            return;
        }
        categoryViewModel.getCategoryListByName(inputName).observe(this, categories -> {
            if (categories != null && !categories.isEmpty()) {
                edtCategoryName.setError("Category name already exists");
            } else {
                try {
                    Category newCategory = new Category();
                    newCategory.setCategoryName(inputName);
                    categoryViewModel.addCategory(newCategory);
                    Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid category", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}