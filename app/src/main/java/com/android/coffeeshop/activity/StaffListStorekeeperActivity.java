package com.android.coffeeshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.coffeeshop.R;
import com.android.coffeeshop.adapter.StaffAdapter;
import com.android.coffeeshop.utils.StaffWithRole;
import com.android.coffeeshop.viewmodel.StaffViewModel;

import java.util.ArrayList;
import java.util.List;

public class StaffListStorekeeperActivity extends BaseActivity implements StaffAdapter.OnStaffActionListener {

    private RecyclerView rvStaffList;
    private ImageButton btnBack, btnAdd, btnEdit, btnDelete;
    private CheckBox checkboxAll;
    private StaffAdapter staffAdapter;
    private StaffViewModel staffViewModel;
    private List<StaffWithRole> allStaffList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        setupRecyclerView();
        loadStaffData();
        setupListeners();
    }

    private void initViews() {
        rvStaffList = findViewById(R.id.rvStaffList);
        btnBack = findViewById(R.id.btnBack);
        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        checkboxAll = findViewById(R.id.checkboxAll);
    }

    private void setupRecyclerView() {
        rvStaffList.setLayoutManager(new LinearLayoutManager(this));
        staffAdapter = new StaffAdapter(this, new ArrayList<>(), this);
        rvStaffList.setAdapter(staffAdapter);
    }

    private void loadStaffData() {
        staffViewModel = new StaffViewModel(getApplication());
        staffViewModel.getStaffList().observe(this, staffList -> {
            allStaffList = staffList;
            staffAdapter.updateStaffList(staffList);
        });
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddStaffActivity.class);
            startActivity(intent);
        });

        btnEdit.setOnClickListener(v -> {
            List<StaffWithRole> selectedStaff = staffAdapter.getSelectedStaff();

            if (selectedStaff.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn một nhân viên để chỉnh sửa", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedStaff.size() > 1) {
                Toast.makeText(this, "Vui lòng chỉ chọn một nhân viên để chỉnh sửa", Toast.LENGTH_SHORT).show();
                staffAdapter.clearSelection();
                return;
            }

            // Nếu chỉ có 1 staff được chọn, chuyển sang EditStaffActivity
            StaffWithRole staff = selectedStaff.get(0);
            Intent intent = new Intent(this, EditStaffActivity.class);
            intent.putExtra("USER_ID", staff.getUserId());
            startActivity(intent);
            staffAdapter.clearSelection();
        });

        btnDelete.setOnClickListener(v -> {
            List<StaffWithRole> selectedStaff = staffAdapter.getSelectedStaff();
            if (selectedStaff.isEmpty()) {
                Toast.makeText(this, getString(R.string.staff_delete_select_message), Toast.LENGTH_SHORT).show();
                return;
            }
            showDeleteConfirmationDialog(selectedStaff);
        });

        checkboxAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectAllStaff(isChecked);
        });
    }

    private void selectAllStaff(boolean isSelected) {
        if (isSelected) {
            staffAdapter.updateStaffList(allStaffList);
            for (StaffWithRole staff : allStaffList) {
                if (!staffAdapter.getSelectedStaff().contains(staff)) {
                    staffAdapter.getSelectedStaff().add(staff);
                }
            }
        } else {
            staffAdapter.clearSelection();
        }
        staffAdapter.notifyDataSetChanged();
    }

    private void showDeleteConfirmationDialog(List<StaffWithRole> staffToDelete) {
        // Tạo và cấu hình dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate custom layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_delete_confirmation, null);
        builder.setView(dialogView);

        // Lấy tham chiếu đến các thành phần trong layout
        TextView tvDeleteMessage = dialogView.findViewById(R.id.tvDeleteMessage);
        Button btnYes = dialogView.findViewById(R.id.btnYes);
        Button btnNo = dialogView.findViewById(R.id.btnNo);

        // Thiết lập nội dung message
        tvDeleteMessage.setText(getString(R.string.staff_delete_confirmation_message, staffToDelete.size()));

        // Tạo dialog
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Thiết lập sự kiện click cho nút Yes
        btnYes.setOnClickListener(v -> {
            // Tiến hành xóa staff đã chọn
            for (StaffWithRole staff : staffToDelete) {
                staffViewModel.deleteStaff(staff.getUserId());
            }
            // Xóa các lựa chọn và cập nhật UI
            staffAdapter.clearSelection();
            checkboxAll.setChecked(false);
            Toast.makeText(this, getString(R.string.staff_delete_success_message, staffToDelete.size()),
                    Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        // Thiết lập sự kiện click cho nút No
        btnNo.setOnClickListener(v -> {
            dialog.dismiss();
        });

        // Hiển thị dialog
        dialog.show();
    }

    @Override
    public void onStaffSelected(List<StaffWithRole> selectedStaff) {
        // Không cần xử lý logic đặc biệt nữa vì chúng ta đã xử lý trong
        // btnEdit.setOnClickListener
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_staff_list_storekeeper;
    }
}