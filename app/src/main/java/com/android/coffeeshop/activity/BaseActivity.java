package com.android.coffeeshop.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.android.coffeeshop.R;
import com.android.coffeeshop.utils.EditUserProfile;
import com.android.coffeeshop.viewmodel.UserViewModel;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.File;

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    private ImageView navHeaderImage;
    private TextView navUserName;
    private TextView navRole;
    private UserViewModel userViewModel;
    private EditUserProfile editUserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_base);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Khởi tạo DrawerLayout và NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navHeaderImage = navigationView.getHeaderView(0).findViewById(R.id.navImage);
        navUserName = navigationView.getHeaderView(0).findViewById(R.id.navUserName);
        navRole = navigationView.getHeaderView(0).findViewById(R.id.navRoleName);
        navigationView.setNavigationItemSelectedListener(this);

        // Thiết lập Toggle cho Navigation Drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Thêm layout nội dung của Activity con vào content_frame
        FrameLayout contentFrame = findViewById(R.id.fragment_container);
        getLayoutInflater().inflate(getLayoutResourceId(), contentFrame, true);


        // Lấy username từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        if (username != null) {
            // Lấy thông tin người dùng + role (EditUserProfile)
            editUserProfile = userViewModel.getUserWithRole(username);

            // Hiển thị các thông tin lên giao diện
            if (editUserProfile != null) {
                navUserName.setText(editUserProfile.getUserName());
                navRole.setText(editUserProfile.getRoleName());

                // Nếu có avatarUrl thì load lên ImageView
                String imagePath = editUserProfile.getAvatarUrl();
                if (imagePath != null && !imagePath.isEmpty()) {
                    File file = new File(imagePath);
                    Picasso.get()
                            .load(file)
                            .placeholder(R.drawable.ic_login_user_logo)
                            .error(R.drawable.ic_login_user_logo)
                            .into(navHeaderImage);
                } else {
                    navHeaderImage.setImageResource(R.drawable.ic_login_user_logo);
                }
            }
        }

    }

    protected abstract int getLayoutResourceId();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        editUserProfile = userViewModel.getUserWithRole(username);

        if (item.getItemId() == R.id.nav_home) {
            if (editUserProfile.getRoleName().equals("Employee")) {
                Intent intent = new Intent(this, HomeEmployeeActivity.class);
                startActivity(intent);
            } else if (editUserProfile.getRoleName().equals("Manager")) {
                Intent intent = new Intent(this, HomeManagerActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, HomeStoreKeeperActivity.class);
                startActivity(intent);
            }

        } else if (item.getItemId() == R.id.nav_edit_profile) {
            Intent intent = new Intent(this, EditUserProfileActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_change_password) {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_logout) {
            new AlertDialog.Builder(this)
                    .setMessage("Do you really want to logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        finishAffinity();
                        Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        dialog.dismiss();
                    })
                    .show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
