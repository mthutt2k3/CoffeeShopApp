package com.android.coffeeshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.coffeeshop.R;
import com.android.coffeeshop.utils.StaffWithRole;

import java.util.ArrayList;
import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {

    private Context context;
    private List<StaffWithRole> staffList;
    private List<StaffWithRole> selectedStaff;
    private OnStaffActionListener listener;

    public StaffAdapter(Context context, List<StaffWithRole> staffList, OnStaffActionListener listener) {
        this.context = context;
        this.staffList = staffList;
        this.selectedStaff = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_staff, parent, false);
        return new StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        StaffWithRole staff = staffList.get(position);
        holder.tvName.setText(staff.getFullName());
        holder.tvSalary.setText("$" + staff.getSalary());
        holder.tvRole.setText(staff.getRoleName());

        holder.checkbox.setChecked(selectedStaff.contains(staff));
        holder.checkbox.setEnabled(true);

        holder.itemView.setOnClickListener(v -> {
            toggleSelection(staff);
        });

        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                if (isChecked) {
                    selectedStaff.add(staff);
                } else {
                    selectedStaff.remove(staff);
                }
                listener.onStaffSelected(selectedStaff);
            }
        });
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    public void updateStaffList(List<StaffWithRole> newStaffList) {
        this.staffList = newStaffList;
        notifyDataSetChanged();
    }

    public List<StaffWithRole> getSelectedStaff() {
        return selectedStaff;
    }

    public void clearSelection() {
        selectedStaff.clear();
        notifyDataSetChanged();
    }

    private void toggleSelection(StaffWithRole staff) {
        if (selectedStaff.contains(staff)) {
            selectedStaff.remove(staff);
        } else {
            selectedStaff.add(staff);
        }
        listener.onStaffSelected(selectedStaff);
        notifyDataSetChanged();
    }

    public interface OnStaffActionListener {
        void onStaffSelected(List<StaffWithRole> selectedStaff);
    }

    static class StaffViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;
        TextView tvName, tvSalary, tvRole;

        StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkbox);
            tvName = itemView.findViewById(R.id.tvName);
            tvSalary = itemView.findViewById(R.id.tvSalary);
            tvRole = itemView.findViewById(R.id.tvRole);
        }
    }
}