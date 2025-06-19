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

public class AddProductActivity extends BaseActivity {

    private EditText edtProductName, edtProductPrice, edtStockQuantity, edtProductRecipe;
    private Spinner spCategory;
    private Button btnSaveProduct;
    private ImageView ivProductImage;
    private TextView txtFileName;
    private ProductViewModel productViewModel;
    private CategoryViewModel categoryViewModel;
    private String selectedImagePath = "";

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
        //setContentView(R.layout.activity_add_product);

        edtProductName = findViewById(R.id.edtProductName);
        edtProductPrice = findViewById(R.id.edtProductPrice);
        edtStockQuantity = findViewById(R.id.edtStockQuantity);
        edtProductRecipe = findViewById(R.id.edtProductRecipe);
        spCategory = findViewById(R.id.spCategory);
        btnSaveProduct = findViewById(R.id.btnSaveProduct);
        ivProductImage = findViewById(R.id.imgPreview);
        txtFileName = findViewById(R.id.txtFileName);

        productViewModel = new ProductViewModel(getApplication());
        categoryViewModel = new CategoryViewModel(getApplication());
        checkAndRequestPermissions();
        loadCategories();
        btnSaveProduct.setOnClickListener(v -> onSaveProductClicked());
        ivProductImage.setOnClickListener(this::openFileChooserAdd);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_product;
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
    private void onSaveProductClicked() {
        if (!validateRequiredFields()) {
            return;
        }

        if (selectedImagePath.isEmpty()) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String productName = edtProductName.getText().toString().trim();
            double productPrice = Double.parseDouble(edtProductPrice.getText().toString().trim());
            int stockQuantity = Integer.parseInt(edtStockQuantity.getText().toString().trim());
            String productRecipe = edtProductRecipe.getText().toString().trim();
            int categoryId = spCategory.getSelectedItemPosition() + 1;
            Date createdAt = new Date();
            Product newProduct = new Product();
            newProduct.setProductName(productName);
            newProduct.setProductPrice(productPrice);
            newProduct.setStockQuantity(stockQuantity);
            newProduct.setProductRecipes(productRecipe);
            newProduct.setCategoryId(categoryId);
            newProduct.setProductImage(selectedImagePath);
            //Unix timestamp
            newProduct.setCreatedAt(createdAt);
            newProduct.setStatus(true);

            Log.d("ImagePath", "Image path: " + selectedImagePath);
            Log.d("newProduct", "newProduct: " + newProduct);
            productViewModel.addProduct(newProduct);
            Toast.makeText(this, "Product added successfully. Image saved at: " + selectedImagePath, Toast.LENGTH_SHORT).show();
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price or quantity", Toast.LENGTH_SHORT).show();
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
        });
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
    public void openFileChooserAdd(View view) {
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


    private boolean validateRequiredFields() {
        boolean isValid = true;
        if (edtProductName.getText().toString().trim().isEmpty()) {
            edtProductName.setError("Product name is required");
            isValid = false;
        }
        if (edtProductPrice.getText().toString().trim().isEmpty()) {
            edtProductPrice.setError("Product price is required");
            isValid = false;
        }
        if (edtStockQuantity.getText().toString().trim().isEmpty()) {
            edtStockQuantity.setError("Stock quantity is required");
            isValid = false;
        }
        return isValid;
    }


}