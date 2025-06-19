package com.android.coffeeshop.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

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
import com.android.coffeeshop.adapter.ProductEmployeeAdapter;
import com.android.coffeeshop.entity.Category;
import com.android.coffeeshop.entity.Product;
import com.android.coffeeshop.fragment.CategoryBottomSheetFragment;
import com.android.coffeeshop.viewmodel.CategoryViewModel;
import com.android.coffeeshop.viewmodel.ProductViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class ProductListEmployeeActivity extends BaseActivity implements CategoryBottomSheetFragment.OnCategoryItemClickListener {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_product_list_employee;
    }

    private TextView showCategories;
    RecyclerView recyclerView;
    List<Product> products = new ArrayList<>();
    private ProductViewModel productViewModel;
    private CategoryViewModel categoryViewModel;
    ProductEmployeeAdapter productAdapter;
    View rootView;
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

        getProductRequest();
        showCurrentCategoryLabel(0);

        skeletonLayout.startShimmer();
    }

    private void initData() {
        showCategories = findViewById(R.id.showCategories);
        showCategories.setVisibility(View.GONE);
        skeletonLayout = findViewById(R.id.skeletonLayout);

        recyclerView = findViewById(R.id.productRecyclerView);
        recyclerView.hasFixedSize();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        searchBox = findViewById(R.id.searchEditText);

        productViewModel = new ProductViewModel(getApplication());
        categoryViewModel = new CategoryViewModel(getApplication());
    }

    private void addEvents() {
        showCategoryBottomSheet();
        searchProduct();
    }

    private void showCategoryBottomSheet() {
        ArrayList<Category> categories = new ArrayList<>();
        categoryViewModel.getCategorieList().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categoryList) {
                categories.addAll(categoryList);
            }
        });
        showCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryBottomSheetFragment bottomSheetFragment = new CategoryBottomSheetFragment(categories);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });
    }

    private void getProductRequest() {
        productViewModel.getProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> productList) {
                products = new ArrayList<>(productList);
                renderProducts();
            }
        });
    }

    private void renderProducts() {
        productAdapter = new ProductEmployeeAdapter(products, this);
        recyclerView.setAdapter(productAdapter);

        skeletonLayout.stopShimmer();
        skeletonLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void searchProduct() {
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                productAdapter.setProductList(products);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = editable.toString().trim();

                productAdapter.filter(searchText);
                productAdapter.notifyDataSetChanged();
            }
        });
    }

    private void showCurrentCategoryLabel(int categoryId) {
        if (categoryId == 0) {
            showCategories.setText("Tất cả");
        } else {
            // find category name by categoryId in categories
            Category category = categoryViewModel.getCategoryById(categoryId);
            showCategories.setText(category.getCategoryName());
        }
    }

    @Override
    public void onCategoryItemClick(int categoryId) {
        if (categoryId == 0)
            getProductRequest();
        else
            productViewModel.getProductsByCategoryId(categoryId, products).observe(this, new Observer<List<Product>>() {
                @Override
                public void onChanged(List<Product> productList) {
                    products = new ArrayList<>(productList);
                    renderProducts();
                }
            });

        showCurrentCategoryLabel(categoryId);
    }
}