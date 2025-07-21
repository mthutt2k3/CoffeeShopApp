package com.android.coffeeshop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.coffeeshop.R;
import com.android.coffeeshop.entity.User;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private Context context;
    private List<User> employeeList;
    private OnDeleteClickListener onDeleteClickListener;
    private OnEditClickListener onEditClickListener;

    public EmployeeAdapter(Context context, List<User> employeeList, OnDeleteClickListener onDeleteClickListener, OnEditClickListener onEditClickListener) {
        this.context = context;
        this.employeeList = employeeList;
        this.onDeleteClickListener = onDeleteClickListener;
        this.onEditClickListener = onEditClickListener;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.employee_item_layout, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        User employee = employeeList.get(position);

        holder.tvEmployeeName.setText(employee.getFullName());
        holder.tvPosition.setText(employee.getPosition());

        // Avatar
        String avatarUrl = employee.getAvatarUrl();
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            File imgFile = new File(avatarUrl);
            if (imgFile.exists()) {
                Picasso.get()
                        .load(imgFile)
                        .placeholder(R.drawable.img_avatar)
                        .error(R.drawable.img_error)
                        .into(holder.ivAvatar);
            } else {
                holder.ivAvatar.setImageResource(R.drawable.img_error);
            }
        } else {
            holder.ivAvatar.setImageResource(R.drawable.img_avatar); // fallback
        }

        // Trạng thái: Active / Inactive
        if (employee.isActive()) {
            holder.itemView.setAlpha(1.0f); // Bình thường
        } else {
            holder.itemView.setAlpha(0.5f); // Làm mờ
        }

        // Sự kiện chỉnh sửa
        holder.tvEmployeeName.setOnClickListener(v -> {
            if (onEditClickListener != null) {
                onEditClickListener.onEditClick(
                        employee.getUserId(),
                        employee.getFullName(),
                        employee.getUserName(),
                        employee.getEmail(),
                        employee.getPhoneNumber(),
                        employee.getPassword(),
                        employee.getPosition(),
                        employee.isActive()
                );
            }
        });

        // Sự kiện xóa
        holder.tvAction.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(employee.getUserId(), employee.getFullName());
            }
        });
    }


    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView tvEmployeeName, tvPosition, tvAction;
        ImageView ivAvatar;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmployeeName = itemView.findViewById(R.id.tvEmployeeName);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            tvAction = itemView.findViewById(R.id.tvAction);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int employeeId, String fullName);
    }

    public interface OnEditClickListener {
        void onEditClick(int employeeId, String fullName, String userName,
                         String email, String phoneNumber, String password, String position, boolean isActive);
    }
}
