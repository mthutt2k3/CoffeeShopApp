package com.android.coffeeshop.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.android.coffeeshop.R;
import com.android.coffeeshop.utils.EditUserProfile;
import com.android.coffeeshop.viewmodel.UserViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class EditUserProfileActivity extends BaseActivity {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_edit_user_profile; // Layout cho Activity này
    }

    private ImageView avatarUrl;
    private TextView tvUserName;

    private EditText edFullName;

    private EditText edPhoneNumber;
    private TextView tvRoleName;
    private Button btnUpdateProfile;

    private UserViewModel userViewModel;
    private EditUserProfile editUserProfile;
    private String selectedImagePath = "";
    // Launcher để yêu cầu chọn ảnh từ bộ nhớ
    @SuppressLint("SetTextI18n")
    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        // Lưu ảnh vào bộ nhớ ngoài của ứng dụng
                        saveImageToExternalStorage(imageUri);
                        // Sau khi lưu xong, ta đã có selectedImagePath

                        File file = new File(selectedImagePath);
                        Picasso.get()
                                .load(file)
                                .placeholder(R.drawable.ic_login_user_logo)
                                .error(R.drawable.ic_login_user_logo)
                                .into(avatarUrl);
                    }
                } else {
                    Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                }
            });

    // Launcher để yêu cầu quyền đọc/ghi bộ nhớ (nếu cần)
    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
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
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Ánh xạ View
        avatarUrl = findViewById(R.id.imageView);
        tvUserName = findViewById(R.id.tvUserName);
        edFullName = findViewById(R.id.edFullName);
        edPhoneNumber = findViewById(R.id.edPhoneNumber);
        tvRoleName = findViewById(R.id.tvRoleName);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);

        // Kiểm tra và yêu cầu quyền nếu cần
        checkAndRequestPermissions();

        // Lấy username từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        if (username != null) {
            // Lấy thông tin người dùng + role (EditUserProfile)
            editUserProfile = userViewModel.getUserWithRole(username);

            // Hiển thị các thông tin lên giao diện
            if (editUserProfile != null) {
                tvUserName.setText(editUserProfile.getUserName());
                edFullName.setText(editUserProfile.getFullName());
                edPhoneNumber.setText(editUserProfile.getPhone());
                tvRoleName.setText(editUserProfile.getRoleName());

                // Nếu có avatarUrl thì load lên ImageView
                String imagePath = editUserProfile.getAvatarUrl();
                if (imagePath != null && !imagePath.isEmpty()) {
                    File file = new File(imagePath);
                    Picasso.get()
                            .load(file)
                            .placeholder(R.drawable.ic_login_user_logo)
                            .error(R.drawable.ic_login_user_logo)
                            .into(avatarUrl);
                } else {
                    avatarUrl.setImageResource(R.drawable.ic_login_user_logo);
                }
            }
        }
        avatarUrl.setOnClickListener(v -> openFileChooser());

        // Mở giao diện chọn ảnh khi nhấn vào ImageView (nếu muốn)
        btnUpdateProfile.setOnClickListener(v -> onEditProfileButtonClick());


    }

    private void onEditProfileButtonClick() {
        if (editUserProfile == null) {
            Toast.makeText(this, "User info is null!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy dữ liệu đã chỉnh sửa
        String newFullName = edFullName.getText().toString().trim();
        String newPhone = edPhoneNumber.getText().toString().trim();

        // Nếu người dùng có chọn ảnh mới, selectedImagePath != ""
        if (!selectedImagePath.isEmpty()) {
            editUserProfile.setAvatarUrl(selectedImagePath);
        }

        // Gán vào đối tượng EditUserProfile
        editUserProfile.setFullName(newFullName);
        editUserProfile.setPhone(newPhone);

        // Gọi hàm updateUserProfile(...) từ ViewModel
        // Ở đây ta cần userId (có thể editUserProfile có sẵn getUserId())
        int userId = editUserProfile.getUserId();
        userViewModel.updateUserProfile(newFullName, newPhone, editUserProfile.getAvatarUrl(), userId);

        // Thông báo thành công
        Toast.makeText(this, "Update Profile Success!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Kiểm tra và yêu cầu quyền đọc/ghi bộ nhớ nếu cần.
     */
    private void checkAndRequestPermissions() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        // Nếu chưa có quyền, yêu cầu
        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(permissions);
        }
    }

    /**
     * Mở giao diện chọn ảnh trong thư viện.
     */
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }

    /**
     * Lưu ảnh đã chọn vào thư mục "user_images_upload" trong bộ nhớ ngoài của ứng dụng.
     */
    private void saveImageToExternalStorage(Uri imageUri) {
        try (InputStream inputStream = getContentResolver().openInputStream(imageUri)) {
            if (inputStream == null) {
                Toast.makeText(this, "Cannot open image stream", Toast.LENGTH_SHORT).show();
                return;
            }
            // Tạo hoặc kiểm tra thư mục
            File dir = new File(getExternalFilesDir(null), "user_images_upload");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Tạo file đích dựa trên tên gốc
            File file = new File(dir, getFileName(imageUri));
            if (file.exists()) {
                file.delete();
            }

            // Sao chép nội dung ảnh vào file đích
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Cập nhật đường dẫn đã lưu
            selectedImagePath = file.getAbsolutePath();

        } catch (IOException e) {
            Log.e("SaveImage", "Error saving image: " + e.getMessage(), e);
            Toast.makeText(this, "Error saving image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Lấy tên file gốc từ Uri (tương tự như trong AddProductActivity).
     */
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
            int cut = (result != null) ? result.lastIndexOf('/') : -1;
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return (result != null) ? result : "unknown_file.jpg";
    }
}