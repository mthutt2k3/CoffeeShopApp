package com.android.coffeeshop.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.coffeeshop.R;
import com.android.coffeeshop.adapter.ProductAdapter;
import com.android.coffeeshop.entity.Product;
import com.android.coffeeshop.viewmodel.ProductViewModel;

import java.util.List;

public class ProductListManagerActivity extends BaseActivity {

    private RecyclerView rvProductList;
    private Button btnAddProduct;
    private TextView tvProductCount;
    private ProductAdapter productAdapter;
    private ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_product_list_manager);

        rvProductList = findViewById(R.id.rvProductList);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        tvProductCount = findViewById(R.id.tvProductCount);

        rvProductList.setLayoutManager(new LinearLayoutManager(this));

        productViewModel = new ProductViewModel(getApplication());

        productViewModel.getProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                productAdapter = new ProductAdapter(ProductListManagerActivity.this, products,
                        ProductListManagerActivity.this::showDeleteConfirmationDialog,
                        ProductListManagerActivity.this::navigateToEditProductScreen);
                rvProductList.setAdapter(productAdapter);
                updateProductCount(products);
            }
        });

        btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListManagerActivity.this, AddProductActivity.class);
            startActivity(intent);
            Toast.makeText(ProductListManagerActivity.this, "Navigating to Add Product", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_product_list_manager;
    }

    @SuppressLint("SetTextI18n")
    private void updateProductCount(List<Product> products) {
        if (products.isEmpty()) {
            tvProductCount.setText("No products found.");
        } else {
            tvProductCount.setText(products.size() + " Products Found");
        }
    }

    private void showDeleteConfirmationDialog(int productId, String productName) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Product " + productName)
                .setMessage("Do you want to delete " + productName + "?")
                .setPositiveButton("Yes", (dialog, which) -> confirmDelete(productId))
                .setNegativeButton("No", null)
                .show();
    }

    private void confirmDelete(int productId) {
        productViewModel.deleteProduct(productId, isDeleted -> {
            runOnUiThread(() -> {
                if (isDeleted) {
                    Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Cannot delete product. It has existing orders.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }


    private void navigateToEditProductScreen(int productId, String productName, double productPrice, String productImage,
                                             int categoryId, int stockQuantity, String productRecipes, boolean status) {
        Intent intent = new Intent(ProductListManagerActivity.this, EditProductActivity.class);
        intent.putExtra("PRODUCT_ID", productId);
        intent.putExtra("PRODUCT_NAME", productName);
        intent.putExtra("PRODUCT_PRICE", productPrice);
        intent.putExtra("PRODUCT_IMAGE", productImage);
        intent.putExtra("CATEGORY_ID", categoryId);
        intent.putExtra("STOCK_QUANTITY", stockQuantity);
        intent.putExtra("PRODUCT_RECIPES", productRecipes);
        intent.putExtra("STATUS", status);
        startActivity(intent);
        Toast.makeText(this, "Navigating to Edit Product", Toast.LENGTH_SHORT).show();
    }

}
