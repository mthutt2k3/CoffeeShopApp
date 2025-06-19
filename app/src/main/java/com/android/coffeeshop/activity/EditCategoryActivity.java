package com.android.coffeeshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.coffeeshop.R;
import com.android.coffeeshop.entity.Category;
import com.android.coffeeshop.viewmodel.CategoryViewModel;

import java.util.List;

public class EditCategoryActivity extends BaseActivity {
    private int CategoryId;
    private EditText edtCategoryName;
    private Button btnSaveCategory;
    private CategoryViewModel categoryViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        edtCategoryName = findViewById(R.id.edtCategoryName);
        btnSaveCategory = findViewById(R.id.btnSaveCategory);

        categoryViewModel = new CategoryViewModel(getApplication());
        btnSaveCategory.setOnClickListener(v -> onSaveCategoryClicked());
        Intent intent = getIntent();
        CategoryId = intent.getIntExtra("CATEGORY_ID", -1);
        String categoryName = intent.getStringExtra("CATEGORY_NAME");
        edtCategoryName.setText(categoryName);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_edit_category;
    }
    private void onSaveCategoryClicked() {
        if (!validateRequiredFields()) {
            return;
        }

        try {
            String name = edtCategoryName.getText().toString().trim();
            Category newCategory = new Category();
            newCategory.setCategoryName(name);
            newCategory.setCategoryId(CategoryId);
            categoryViewModel.updateCategory(newCategory);

            Toast.makeText(this, "Category updated successfully", Toast.LENGTH_SHORT).show();
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid category", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean validateRequiredFields() {
        boolean isValid = true;
        if (edtCategoryName.getText().toString().trim().isEmpty()) {
            edtCategoryName.setError("Category name is required");
            return false;
        }
        List<Category> categories = categoryViewModel.getCategoryListByName(edtCategoryName.getText().toString().trim()).getValue();
        if (categories != null && !categories.isEmpty()) {
            edtCategoryName.setError("Category name already exists");
            return false;
        }
        return isValid;
    }
}