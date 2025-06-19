package com.android.coffeeshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.coffeeshop.R;
import com.android.coffeeshop.adapter.ScheduleAdapter;
import com.android.coffeeshop.entity.Schedule;
import com.android.coffeeshop.viewmodel.ScheduleViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ViewScheduleListActivity extends BaseActivity {

    private TextView monthYearTV, currentWeekTV;
    private TextView selectedDayTextView = null;
    private Button btnPrevious, btnNext, btnNewEvent;
    private RecyclerView calendarRecyclerView;
    private ScheduleAdapter scheduleAdapter;
    private Calendar currentCalendar;
    private ScheduleViewModel scheduleViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scheduleViewModel = new ViewModelProvider(this).get(ScheduleViewModel.class);

        monthYearTV = findViewById(R.id.monthYearTV);
        currentWeekTV = findViewById(R.id.currentWeekTV);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnNewEvent = findViewById(R.id.btnNewEvent);
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);

        calendarRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Calendar to track the current date
        currentCalendar = Calendar.getInstance();

        // Show current date and week
        showCurrentDate();
        showCurrentWeek();
        showDaysInWeek();

        // Set click listeners for navigation buttons
        btnPrevious.setOnClickListener(v -> previousWeekAction());
        btnNext.setOnClickListener(v -> nextWeekAction());
        btnNewEvent.setOnClickListener(v -> newEventAction());

        // Set OnClickListener for each day (day_1 to day_7)
        for (int i = 0; i < 7; i++) {
            String dayTextViewId = "day_" + (i + 1);
            int resID = getResources().getIdentifier(dayTextViewId, "id", getPackageName());
            TextView dayTextView = findViewById(resID);
            if (dayTextView != null) {
                int finalI = i;
                dayTextView.setOnClickListener(v -> onDayClick(finalI));
            }
        }
    }

    private void onDayClick(int dayIndex) {
        // Reset the previous selection
        if (selectedDayTextView != null) {
            selectedDayTextView.setBackgroundColor(getResources().getColor(android.R.color.transparent)); // Reset color
        }

        // Get the new selected day
        String dayTextViewId = "day_" + (dayIndex + 1);
        int resID = getResources().getIdentifier(dayTextViewId, "id", getPackageName());
        TextView dayTextView = findViewById(resID);

        if (dayTextView != null) {
            dayTextView.setBackgroundColor(getResources().getColor(R.color.lavender)); // Change to selected color
            selectedDayTextView = dayTextView; // Store the new selected day
        }

        // Lấy ngày được chọn từ currentCalendar
        Calendar selectedDate = (Calendar) currentCalendar.clone();
        selectedDate.set(Calendar.DAY_OF_WEEK, dayIndex + 1); // Cập nhật ngày được chọn
        // Chuyển đổi ngày thành UNIX timestamp
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String selectedDateString = dateFormat.format(selectedDate.getTime());


        loadScheduleDataForDate(selectedDateString, selectedDateString);

    }
    private void onItemClicked(Schedule schedule) {
        Intent intent = new Intent(ViewScheduleListActivity.this, EditScheduleActivity.class);
        intent.putExtra("SCHEDULE_ID", schedule.getScheduleId());
        intent.putExtra("START_DATE", schedule.getStartDate().toString());
        intent.putExtra("START_TIME", schedule.getStartTime().toString());
        intent.putExtra("END_TIME", schedule.getEndTime().toString());
        intent.putExtra("USER_ID", schedule.getUserId());
        startActivity(intent);
    }

    private void loadScheduleDataForDate(String startDate, String endDate) {
        scheduleViewModel.getScheduleData(startDate, endDate).observe(this, new Observer<List<Schedule>>() {
            @Override
            public void onChanged(List<Schedule> schedules) {
                List<String> shiftDetails = new ArrayList<>();
                for (Schedule schedule : schedules) {
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                    String startTime = timeFormat.format(schedule.getStartTime());
                    String endTime = timeFormat.format(schedule.getEndTime());
                    shiftDetails.add(startTime + " - " + endTime);
                }
                scheduleAdapter = new ScheduleAdapter(schedules, scheduleViewModel, position -> confirmDelete(position));
                calendarRecyclerView.setAdapter(scheduleAdapter);
                scheduleAdapter.setOnItemClickListener(new ScheduleAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Schedule schedule) {
                        onItemClicked(schedule);
                    }
                });
            }
        });
    }


    private void showCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
        String currentDate = dateFormat.format(currentCalendar.getTime());
        monthYearTV.setText(currentDate);
    }

    private void showCurrentWeek() {
        Calendar startOfWeek = (Calendar) currentCalendar.clone();
        startOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); // Set to Sunday
        Calendar endOfWeek = (Calendar) currentCalendar.clone();
        endOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY); // Set to Saturday

        SimpleDateFormat weekFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentWeek = "Current week: " + weekFormat.format(startOfWeek.getTime()) + " - " + weekFormat.format(endOfWeek.getTime());
        currentWeekTV.setText(currentWeek);
    }

    private void showDaysInWeek() {
        for (int i = 0; i < 7; i++) {
            Calendar day = (Calendar) currentCalendar.clone();
            day.set(Calendar.DAY_OF_WEEK, i + 1); // Update the day based on the index (Sunday to Saturday)

            int dayOfMonth = day.get(Calendar.DAY_OF_MONTH);

            // Update the TextView for each day (Creating TextViews from day_1 to day_7)
            String dayTextViewId = "day_" + (i + 1);
            int resID = getResources().getIdentifier(dayTextViewId, "id", getPackageName());
            TextView dayTextView = findViewById(resID);
            if (dayTextView != null) {
                dayTextView.setText(String.valueOf(dayOfMonth));
            }
        }
    }

    private void previousWeekAction() {
        currentCalendar.add(Calendar.WEEK_OF_YEAR, -1);
        showCurrentDate();
        showCurrentWeek();
        showDaysInWeek();
    }

    private void nextWeekAction() {
        currentCalendar.add(Calendar.WEEK_OF_YEAR, 1);
        showCurrentDate();
        showCurrentWeek();
        showDaysInWeek();
    }

    private void newEventAction() {
        Intent intent = new Intent(ViewScheduleListActivity.this, AddScheduleActivity.class);
        startActivity(intent);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Add New Shift");
//
//        final EditText shiftInput = new EditText(this);
//        shiftInput.setHint("Enter shift details");
//        builder.setView(shiftInput);
//
//        builder.setPositiveButton("Add", (dialog, which) -> {
//            String shiftDetails = shiftInput.getText().toString();
//            if (!shiftDetails.isEmpty()) {
//                Toast.makeText(ViewScheduleListActivity.this, "Shift added", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(ViewScheduleListActivity.this, "Shift details cannot be empty", Toast.LENGTH_SHORT).show();
//            }
//        });

//        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
//        builder.show();
    }

    private void confirmDelete(int position) {
        Schedule scheduleToDelete = scheduleAdapter.getSchedules().get(position);

        new AlertDialog.Builder(this)
                .setTitle("Delete Shift")
                .setMessage("Are you sure you want to delete this shift?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Xóa lịch từ cơ sở dữ liệu
                    scheduleViewModel.deleteSchedule(scheduleToDelete);

                    // Cập nhật lại giao diện
                    scheduleAdapter.getSchedules().remove(position);
                    scheduleAdapter.notifyItemRemoved(position);

                    Toast.makeText(ViewScheduleListActivity.this, "Shift deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_view_schedule_list;
    }
}
