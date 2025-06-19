package com.android.coffeeshop.activity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.coffeeshop.R;
import com.android.coffeeshop.adapter.CategoryEmployeeAdapter;
import com.android.coffeeshop.entity.Category;
import com.android.coffeeshop.entity.User;
import com.android.coffeeshop.viewmodel.CategoryViewModel;
import com.android.coffeeshop.viewmodel.UserViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class CategoryListEmployeeActivity extends BaseActivity {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_category_list_employee;
    }
    RecyclerView recyclerView;
    List<Category> categories = new ArrayList<>();
    private CategoryViewModel categoryViewModel;
    private UserViewModel userViewModel;
    CategoryEmployeeAdapter categoryAdapter;
    TextInputEditText searchBox;
    ShimmerFrameLayout skeletonLayout;
    private ShimmerFrameLayout skele;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initData();
        addEvents();
        getCategoryRequest();
        skeletonLayout.startShimmer();
    }

    private void initData() {
        skeletonLayout = findViewById(R.id.skeletonLayout);

        recyclerView = findViewById(R.id.categoryRecyclerView);
        recyclerView.hasFixedSize();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        searchBox = findViewById(R.id.searchEditText);

        categoryViewModel = new CategoryViewModel(getApplication());
        userViewModel = new UserViewModel(getApplication());
    }

    private void addEvents() {
        searchCategory();
    }

    private void getCategoryRequest() {
        categoryViewModel.getCategorieList().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categoriess) {
                categories = new ArrayList<>(categoriess);
                renderCategories();
            }
        });
    }

    private void renderCategories() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userName = sharedPreferences.getString("username", null);
        User user = userViewModel.getUserByUserName(userName);

        categoryAdapter = new CategoryEmployeeAdapter(categories, user.getUserId());
        recyclerView.setAdapter(categoryAdapter);

        skeletonLayout.stopShimmer();
        skeletonLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void searchCategory() {
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                categoryAdapter.setCategoryList(categories);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = editable.toString().trim();

                categoryAdapter.filter(searchText);
                categoryAdapter.notifyDataSetChanged();
            }
        });
    }

    public void showDeleteDialog(Category category) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Category")
                .setMessage("Are you sure you want to delete this category?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deleteCategory(category);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteCategory(Category category) {
        categoryViewModel.deleteCategory(category.getCategoryId(), isDeleted -> {
            runOnUiThread(() -> {
                if (isDeleted) {
                    Toast.makeText(this, "Category deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Cannot delete category. It has existing products.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}