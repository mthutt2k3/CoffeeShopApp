package com.android.coffeeshop.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.coffeeshop.R;
import com.android.coffeeshop.adapter.ScheduleEmployeeAdapter;
import com.android.coffeeshop.entity.Schedule;
import com.android.coffeeshop.viewmodel.ScheduleViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ViewEmployeeScheduleActivity extends BaseActivity {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_view_employee_schedule;
    }
    private TextView monthYearTV, currentWeekTV;
    private TextView selectedDayTextView = null;
    private Button btnPrevious, btnNext, btnNewEvent;
    private RecyclerView calendarRecyclerView;
    private ScheduleEmployeeAdapter scheduleAdapter;
    private Calendar currentCalendar;
    private ScheduleViewModel scheduleViewModel;
    private String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initData();
        showTime();
        addEvents();
    }

    private void initData() {
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

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userName = sharedPreferences.getString("username", null);
    }

    private void showTime() {
        // Show current date and week
        showCurrentDate();
        showCurrentWeek();
        showDaysInWeek();
    }

    private void addEvents() {
        btnPrevious.setOnClickListener(v -> previousWeekAction());
        btnNext.setOnClickListener(v -> nextWeekAction());

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

        loadScheduleDataForDate(userName, selectedDateString, selectedDateString);

    }

    private void loadScheduleDataForDate(String userName, String startDate, String endDate) {
        scheduleViewModel.getScheduleOfEmployee(userName, startDate, endDate).observe(this, new Observer<List<Schedule>>() {
            @Override
            public void onChanged(List<Schedule> schedules) {
                // Chuyển đổi và cập nhật dữ liệu vào Adapter
                List<String> shiftDetails = new ArrayList<>();
                for (Schedule schedule : schedules) {
                    // Định dạng thời gian bắt đầu và kết thúc
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                    String startTime = timeFormat.format(schedule.getStartTime());
                    String endTime = timeFormat.format(schedule.getEndTime());
                    shiftDetails.add(startTime + " - " + endTime);
                }

                // Cập nhật adapter
                scheduleAdapter = new ScheduleEmployeeAdapter(schedules, scheduleViewModel, null);
                calendarRecyclerView.setAdapter(scheduleAdapter);
            }
        });
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

//    @Override
//    protected int getLayoutResourceId() {
//        return R.layout.activity_view_employee_schedule;
//    }
}