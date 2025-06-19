package com.android.coffeeshop.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.android.coffeeshop.R;
import com.android.coffeeshop.entity.Schedule;
import com.android.coffeeshop.entity.User;
import com.android.coffeeshop.utils.UserWithRole;
import com.android.coffeeshop.viewmodel.ScheduleViewModel;
import com.android.coffeeshop.viewmodel.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.List;

public class ScheduleEmployeeAdapter extends RecyclerView.Adapter<ScheduleEmployeeAdapter.ScheduleViewHolder> {
    private UserViewModel userViewModel;
    private List<Schedule> schedules; // List of Schedule objects
    private OnDeleteClickListener onDeleteClickListener;
    private ScheduleViewModel scheduleViewModel;

    public ScheduleEmployeeAdapter(List<Schedule> schedules, ScheduleViewModel scheduleViewModel, OnDeleteClickListener listener) {
        this.schedules = schedules;
        this.onDeleteClickListener = listener;
        this.scheduleViewModel = scheduleViewModel;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
        notifyDataSetChanged();
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {
        Schedule schedule = schedules.get(position);

        userViewModel = new ViewModelProvider((ViewModelStoreOwner) holder.itemView.getContext()).get(UserViewModel.class);

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        String startTime = timeFormat.format(schedule.getStartTime());
        String endTime = timeFormat.format(schedule.getEndTime());

        holder.shiftTextView.setText(startTime + " - " + endTime);

        int userId = schedule.getUserId();
        scheduleViewModel.getUserById(userId).observeForever(new Observer<User>() {
            @Override
            public void onChanged(User user) {

                holder.userTextView.setText(user != null ? user.getFullName() : "Unknown User");

                    UserWithRole userWithRole = userViewModel.getUserWithRoleById(user.getUserId());

                    if ("Employee".equalsIgnoreCase(userWithRole.getRoleName())) {
                        holder.deleteButton.setVisibility(View.GONE);
                    } else {
                        holder.deleteButton.setVisibility(View.VISIBLE);
                    }

            }
        });



        // Xử lý sự kiện xóa
        holder.deleteButton.setOnClickListener(v -> {
            // Show confirmation dialog before deleting
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Delete Shift")
                    .setMessage("Are you sure you want to delete this shift?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // If confirmed, call deleteSchedule to remove this schedule from the database
                        scheduleViewModel.deleteSchedule(schedule);

                        // Remove the item from the list and notify the adapter
                        schedules.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(holder.itemView.getContext(), "Shift deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
        holder.deleteButton.setOnClickListener(v -> onDeleteClickListener.onDeleteClick(position));
    }

    public void onDeleteClick(int position) {
        schedules.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {

        TextView shiftTextView;
        TextView userTextView;
        Button deleteButton;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            shiftTextView = itemView.findViewById(R.id.shiftTextView);
            userTextView = itemView.findViewById(R.id.tvUserNameSchedule); // New TextView for user name
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
