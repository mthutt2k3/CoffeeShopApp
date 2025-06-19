package com.android.coffeeshop.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.android.coffeeshop.R;
import com.android.coffeeshop.utils.DailyOrderStats;
import com.android.coffeeshop.utils.DateUtils;
import com.android.coffeeshop.viewmodel.OrderViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class FinancialReportActivity extends BaseActivity implements OnChartValueSelectedListener {

    private TextView tvWeekRange;
    private LineChart lineChart;
    private OrderViewModel orderViewModel;
    private ImageButton btnBack;
    private float maxSales = 0f;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_financial_report;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
        initData();
        setupListeners();
    }

    private void initViews() {
        tvWeekRange = findViewById(R.id.tvWeekRange);
        lineChart = findViewById(R.id.lineChart);
        btnBack = findViewById(R.id.btnBack);

        // Hiển thị phạm vi tuần
        tvWeekRange.setText(DateUtils.getWeekRangeDisplay());

        // Thiết lập cấu hình cơ bản cho biểu đồ
        setupChart();
    }

    private void setupChart() {
        lineChart.setOnChartValueSelectedListener(this);
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerDragEnabled(true);

        // Cấu hình trục X
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        // Cấu hình trục Y bên trái
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(0f);

        // Ẩn trục Y bên phải
        lineChart.getAxisRight().setEnabled(false);

        // Cấu hình các hiển thị khác
        lineChart.getLegend().setEnabled(false);
        lineChart.setDrawBorders(false);

        // Animation
        lineChart.animateX(1500);
    }

    private void initData() {
        // Khởi tạo ViewModel
        orderViewModel = new OrderViewModel(getApplication());

        // Lấy dữ liệu biểu đồ
        orderViewModel.getDailyStatsForCurrentWeek().observe(this, dailyStats -> {
            if (dailyStats != null && !dailyStats.isEmpty()) {
                setupLineChart(dailyStats);
            }
        });
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupLineChart(List<DailyOrderStats> dailyStats) {
        ArrayList<Entry> values = new ArrayList<>();
        List<String> xLabels = new ArrayList<>();

        // Chuyển đổi dữ liệu cho biểu đồ
        maxSales = 0f;
        int maxIndex = 0;

        for (int i = 0; i < dailyStats.size(); i++) {
            DailyOrderStats stat = dailyStats.get(i);
            float value = stat.getTotalSales();
            values.add(new Entry(i, value));
            xLabels.add(DateUtils.formatDateForChart(stat.getOrderDate()));

            // Tìm giá trị cao nhất
            if (value > maxSales) {
                maxSales = value;
                maxIndex = i;
            }
        }

        // Cấu hình trục X
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));

        // Tạo dataset
        LineDataSet set;

        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            set = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set.setValues(values);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            // Tạo dataset mới
            set = new LineDataSet(values, "Doanh thu");
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setCubicIntensity(0.2f);
            set.setDrawFilled(true);
            set.setDrawCircles(true);
            set.setCircleRadius(4f);
            set.setCircleColor(Color.rgb(0, 188, 212));
            set.setLineWidth(2f);
            set.setColor(Color.rgb(0, 188, 212));
            set.setFillColor(Color.rgb(0, 188, 212));
            set.setFillAlpha(50);
            set.setDrawHighlightIndicators(true);
            set.setDrawHorizontalHighlightIndicator(false);
            set.setHighlightLineWidth(1.5f);
            set.setHighLightColor(Color.rgb(255, 165, 0));

            // Nếu SDK hỗ trợ gradient fill
            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
                set.setFillDrawable(drawable);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set);

            // Tạo LineData đối tượng với dataset
            LineData data = new LineData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTextColor(Color.BLACK);

            // Đặt data cho biểu đồ
            lineChart.setData(data);
        }

        // Đánh dấu giá trị cao nhất
        if (maxSales > 0) {
            lineChart.highlightValue(maxIndex, 0);

            // Thêm limitLine cho giá trị cao nhất
            LimitLine ll = new LimitLine(maxSales, String.format("%.2f", maxSales));
            ll.setLineWidth(2f);
            ll.setTextSize(12f);
            ll.setTextColor(Color.rgb(255, 0, 0));
            ll.setLineColor(Color.rgb(255, 0, 0));

            YAxis leftAxis = lineChart.getAxisLeft();
            leftAxis.removeAllLimitLines();
            leftAxis.addLimitLine(ll);
        }

        lineChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        // Khi người dùng chọn một điểm trên biểu đồ
    }

    @Override
    public void onNothingSelected() {
        // Khi không có gì được chọn
    }
}
