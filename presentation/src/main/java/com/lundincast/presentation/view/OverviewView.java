package com.lundincast.presentation.view;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;

import java.util.ArrayList;

/**
 * Interface representing a View in a model view Presenter (MVP) pattern.
 * In this case it is used as a view representing the stats.
 */
public interface OverviewView extends LoadDataView {

    /**
     * Set total income in monthly overview
     */
    void setMonthIncome(double totalMonthIncome);

    /**
     * Set total expenses in monthly overview
     */
    void setMonthExpenses(double totalMonthExpenses);

    /**
     * Set data to Monthly PieChart for rendering
     */
    void setMonthlyPieChartData(PieData pieData, String category, double monthlyTotal, double percent);

    /**
     * Set data to timeframe spinner for rendering
     */
    void setSpinnerDataAndRender(ArrayList<String> data);

    /**
     * Set data to Category BarChart for rendering
     */
    void setCategoryBarChartData(BarData data);

    /**
     * Set data to category chooser spinner for rendering
     */
    void setCategorySpinnerDataAndRender(ArrayList<String> data);

    /**
     * Show a view with a progress bar indicating a loading process.
     */
    void showBarChartLoading();

    /**
     * Hide a loading view.
     */
    void hideBarChartLoading();
}
