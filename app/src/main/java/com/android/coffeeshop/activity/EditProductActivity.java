package com.android.coffeeshop.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import com.android.coffeeshop.R;
import com.android.coffeeshop.entity.Category;
import com.android.coffeeshop.entity.Product;
import com.android.coffeeshop.viewmodel.CategoryViewModel;
import com.android.coffeeshop.viewmodel.ProductViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditProductActivity extends BaseActivity {

    private EditText etProductName, etProductPrice, etProductRecipes, etStockQuantity;
    private Button btnSaveProduct;
    private Spinner spCategory;
    private ImageView ivProductImage;
    private Switch swActive;
    private TextView txtFileName;
    private int productId, categoryId, stockQuantity;
    private String productName, productImage, productRecipes;
    private double productPrice;
    private boolean status;
    private String selectedImagePath = "";
    private ProductViewModel productViewModel;
    private CategoryViewModel categoryViewModel;

    @SuppressLint("SetTextI18n")
    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    saveImageToExternalStorage(imageUri);
                    loadProductImageWithFileName(selectedImagePath);
                } else {
                    Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                }
            });
    private final ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            result -> {
                if (result.get(Manifest.permission.READ_EXTERNAL_STORAGE) == Boolean.TRUE &&
                        result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == Boolean.TRUE) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_edit_product);

        etProductName = findViewById(R.id.edtProductName);
        etProductPrice = findViewById(R.id.edtProductPrice);
        etProductRecipes = findViewById(R.id.edtProductRecipe);
        etStockQuantity = findViewById(R.id.edtStockQuantity);
        spCategory = findViewById(R.id.spCategory);
        ivProductImage = findViewById(R.id.ivProductImage);
        swActive = findViewById(R.id.swActive);
        btnSaveProduct = findViewById(R.id.btnSaveProduct);
        txtFileName = findViewById(R.id.txtFileName);

        productViewModel = new ProductViewModel(getApplication());
        categoryViewModel = new CategoryViewModel(getApplication());

        Intent intent = getIntent();
        productId = intent.getIntExtra("PRODUCT_ID", -1);
        productName = intent.getStringExtra("PRODUCT_NAME");
        productPrice = intent.getDoubleExtra("PRODUCT_PRICE", 0.0);
        productImage = intent.getStringExtra("PRODUCT_IMAGE");
        categoryId = intent.getIntExtra("CATEGORY_ID", -1);
        stockQuantity = intent.getIntExtra("STOCK_QUANTITY", 0);
        productRecipes = intent.getStringExtra("PRODUCT_RECIPES");
        status = intent.getBooleanExtra("STATUS", true);

        etProductName.setText(productName);
        etProductPrice.setText(String.valueOf(productPrice));
        etProductRecipes.setText(productRecipes);
        etStockQuantity.setText(String.valueOf(stockQuantity));
        swActive.setChecked(status);
        loadProductImageWithFileName(productImage);
        checkAndRequestPermissions();
        loadCategories();
        ivProductImage.setOnClickListener(this::openFileChooserEdit);
        btnSaveProduct.setOnClickListener(v -> onSaveProductClicked());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_edit_product;
    }

    @SuppressLint("SetTextI18n")
    private void loadProductImageWithFileName(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                Picasso.get()
                        .load(imgFile)
                        .placeholder(R.drawable.img_avatar)
                        .error(R.drawable.img_error)
                        .into(ivProductImage);
                String fileName = imgFile.getName();
                txtFileName.setText("Current file: " + fileName);
            } else {
                ivProductImage.setImageResource(R.drawable.img_error);
                txtFileName.setText("No file chosen");
            }
        } else {
            ivProductImage.setImageResource(R.drawable.img_error);
            txtFileName.setText("No file chosen");
        }
    }
    private void loadCategories() {
        categoryViewModel.getCategories().observe(this, categories -> {
            List<String> categoryNames = new ArrayList<>();
            for (Category category : categories) {
                categoryNames.add(category.getCategoryName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, categoryNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCategory.setAdapter(adapter);
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getCategoryId() == categoryId) {
                    spCategory.setSelection(i);
                    break;
                }
            }
        });
    }

    public void openFileChooserEdit(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }

    private void saveImageToExternalStorage(Uri imageUri) {
        try (InputStream inputStream = getContentResolver().openInputStream(imageUri)) {
            if (inputStream == null) {
                Toast.makeText(this, "Cannot open image stream", Toast.LENGTH_SHORT).show();
                return;
            }

            File dir = new File(getExternalFilesDir(null), "product_images_upload");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, getFileName(imageUri));
            if (file.exists()) {
                file.delete();
            }
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            selectedImagePath = file.getAbsolutePath();
            Toast.makeText(this, "Image saved successfully!", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Log.e("SaveImage", "Error saving image: " + e.getMessage(), e);
            Toast.makeText(this, "Error saving image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        result = cursor.getString(index);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result != null ? result.lastIndexOf('/') : -1;
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result != null ? result : "unknown_file.jpg";
    }

    private void checkAndRequestPermissions() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(permissions);
        }
    }

    private void onSaveProductClicked() {
        if (!validateRequiredFields()) {
            return;
        }

        try {
            String updatedName = etProductName.getText().toString().trim();
            double updatedPrice = Double.parseDouble(etProductPrice.getText().toString().trim());
            int updatedStock = Integer.parseInt(etStockQuantity.getText().toString().trim());
            String updatedRecipe = etProductRecipes.getText().toString().trim();
            int updatedCategoryId = spCategory.getSelectedItemPosition() + 1;
            long updatedCreatedAtLong = new Date().getTime();
            // Long -->  Date
            Date updatedCreatedAt = new Date(updatedCreatedAtLong);
            boolean updatedStatus = swActive.isChecked();
            Product updatedProduct = new Product();
            updatedProduct.setProductId(productId);
            updatedProduct.setProductName(updatedName);
            updatedProduct.setProductPrice(updatedPrice);
            updatedProduct.setProductRecipes(updatedRecipe);
            updatedProduct.setStockQuantity(updatedStock);
            updatedProduct.setStatus(updatedStatus);
            updatedProduct.setCategoryId(updatedCategoryId);
            updatedProduct.setProductImage(selectedImagePath.isEmpty() ? productImage : selectedImagePath);
            updatedProduct.setCreatedAt(updatedCreatedAt);
            productViewModel.updateProduct(updatedProduct);
            Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price or quantity", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateRequiredFields() {
        boolean isValid = true;
        if (etProductName.getText().toString().trim().isEmpty()) {
            etProductName.setError("Product name is required");
            isValid = false;
        }
        if (etProductPrice.getText().toString().trim().isEmpty()) {
            etProductPrice.setError("Product price is required");
            isValid = false;
        }
        if (etStockQuantity.getText().toString().trim().isEmpty()) {
            etStockQuantity.setError("Stock quantity is required");
            isValid = false;
        }
        return isValid;
    }

}