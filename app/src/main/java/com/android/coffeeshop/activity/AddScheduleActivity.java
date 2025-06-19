package com.android.coffeeshop.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.coffeeshop.R;
import com.android.coffeeshop.entity.Schedule;
import com.android.coffeeshop.entity.User;
import com.android.coffeeshop.viewmodel.EmployeeViewModel;
import com.android.coffeeshop.viewmodel.ScheduleViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddScheduleActivity extends BaseActivity {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_schedule;
    }
    private EditText edtStartDate;
//    private EditText edtEndDate;
    private EditText edtStartTime;
    private EditText edtEndTime;
    private Spinner spEmployee;
    private Button btnSaveSchedule;

    private EmployeeViewModel employeeViewModel;
    private ScheduleViewModel scheduleViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        employeeViewModel = new EmployeeViewModel(getApplication());
        scheduleViewModel = new ScheduleViewModel(getApplication());
        edtStartDate = findViewById(R.id.edtStartDate);
        edtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(edtStartDate);
            }
        });
//        edtEndDate = findViewById(R.id.edtEndDate);
//        edtEndDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePickerDialog(edtEndDate);
//            }
//        });
        edtStartTime = findViewById(R.id.edtStartTime);
        edtStartTime.setOnClickListener(v -> showTimePickerDialog(edtStartTime));
        edtEndTime = findViewById(R.id.edtEndTime);
        edtEndTime.setOnClickListener(v -> showTimePickerDialog(edtEndTime));
        spEmployee = findViewById(R.id.spEmployee);
        btnSaveSchedule = findViewById(R.id.btnSaveSchedule);
        loadEmployees();
        btnSaveSchedule.setOnClickListener(v -> onSaveScheduleClicked());
    }
    private void showTimePickerDialog(EditText edt) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minuteOfHour) -> {
                    String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minuteOfHour);
                    edt.setText(selectedTime);
                }, hour, minute, true);

        timePickerDialog.show();
    }
    private void showDatePickerDialog(EditText edt) {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Set the selected date to the EditText
                    String selectedDate = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    edt.setText(selectedDate);
                }, year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }
    private void loadEmployees() {
        employeeViewModel.getEmployees().observe(this, employees -> {
            List<String> employeeNames = new ArrayList<>();
            for (User employee : employees ) {
                employeeNames.add(employee.getUserName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, employeeNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spEmployee.setAdapter(adapter);
        });
    }
    private void onSaveScheduleClicked() {
        if (!validateRequiredFields()) {
            return;
        }

        try {
            String startDate = edtStartDate.getText().toString().trim();
//            String endDate = edtEndDate.getText().toString().trim();
            User user = employeeViewModel.getUserByUserName(spEmployee.getSelectedItem().toString());
            Schedule newSchedule = new Schedule();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            newSchedule.setEndDate(dateFormat.parse(startDate));
            newSchedule.setStartDate(dateFormat.parse(startDate));
            newSchedule.setUserId(user.getUserId());
            String startTimeString = edtStartTime.getText().toString().trim();
            String endTimeString = edtEndTime.getText().toString().trim();
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date startTime = timeFormat.parse(startTimeString);
            Date endTime = timeFormat.parse(endTimeString);
            newSchedule.setStartTime(startTime);
            newSchedule.setEndTime(endTime);

            scheduleViewModel.addSchedule(newSchedule);
            Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show();
//            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid schedule", Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean validateRequiredFields() {
        boolean isValid = true;
        if (edtStartDate.getText().toString().trim().isEmpty()) {
            edtStartDate.setError("Date is required");
            isValid = false;
            return false;
        }
        if (edtStartTime.getText().toString().trim().isEmpty()) {
            edtStartTime.setError("Start Time is required");
            isValid = false;
            return false;
        }
        if (edtEndTime.getText().toString().trim().isEmpty()) {
            edtEndTime.setError("End Time is required");
            isValid = false;
            return false;
        }
        String startTimeString = edtStartTime.getText().toString().trim();
        String endTimeString = edtEndTime.getText().toString().trim();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date startTime = timeFormat.parse(startTimeString);
            Date endTime = timeFormat.parse(endTimeString);

            if (startTime.compareTo(endTime) >= 0) {
                edtEndTime.setError("End time must be after start time");
                isValid = false;
                Toast.makeText(this,"End time must be after start time", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            return  false;
        }


        return isValid;
    }
}