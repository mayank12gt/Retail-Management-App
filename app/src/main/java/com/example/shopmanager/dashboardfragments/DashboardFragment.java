package com.example.shopmanager.dashboardfragments;

import android.graphics.Color;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopmanager.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class DashboardFragment extends Fragment {


    LineChart lineChart;
    PieChart pieChart;
    public DashboardFragment() {

    }


    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View v=inflater.inflate(R.layout.fragment_dashboard, container, false);
         lineChart = v.findViewById(R.id.lineChart);
       pieChart = v.findViewById(R.id.pieChart);
        
        createLineChart();
        createPieChart();

        

       return v;
    }

    private void createPieChart() {


        // Dummy data for percentage of items sold by categories
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(30f, "Groceries"));
        entries.add(new PieEntry(10f, "Toiletries"));
        entries.add(new PieEntry(10f, "Dairy"));
        entries.add(new PieEntry(15f, "Books"));
        entries.add(new PieEntry(10f, "Snacks"));
        entries.add(new PieEntry(15f, "Beverages"));
        entries.add(new PieEntry(15f, "Convenience Items"));


        PieDataSet dataSet = new PieDataSet(entries, "Items Sold by Categories");

        // Setting up colors for each entry
        dataSet.setColors(new int[]{Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.MAGENTA});

        // Creating a PieData object with the PieDataSet
        PieData pieData = new PieData(dataSet);

        // Setting up the PieChart
        pieChart.setData(pieData);
        pieChart.getDescription().setText("Percentage of Items Sold by Categories");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        // Customizing the legend
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);

        // Refresh the chart to apply changes
        pieChart.invalidate();
    }

    private void createLineChart() {

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 15000));
        entries.add(new Entry(1, 20000));
        entries.add(new Entry(2, 18000));
        entries.add(new Entry(3, 22000));
        entries.add(new Entry(4, 25000));


        LineDataSet dataSet = new LineDataSet(entries, "Total Sales (in ₹)");
        int startColor = Color.rgb(255, 0, 0); // Red
        int endColor = Color.rgb(0, 255, 0);   // Green
        dataSet.setGradientColor(startColor, endColor);


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);
        LineData lineData = new LineData(dataSets);


        lineChart.setData(lineData);
        lineChart.getDescription().setText("Total Sales per Month");
        lineChart.setDrawGridBackground(false);


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new MonthAxisValueFormatter()); // Assuming you want to display month names


        YAxis yAxisLeft = lineChart.getAxisLeft();
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);


        Legend legend = lineChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        // Refresh the chart to apply changes
        lineChart.invalidate();
    }
}
class MonthAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM", Locale.getDefault());

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        int month = (int) value;


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);


        return dateFormat.format(calendar.getTime());
    }
}