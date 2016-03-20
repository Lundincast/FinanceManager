package com.lundincast.presentation.view;

import com.github.mikephil.charting.data.PieData;

/**
 * Interface representing a View in a model view Presenter (MVP) pattern.
 * In this case it is used as a view representing the stats.
 */
public interface OverviewView extends LoadDataView {

    /**
     * Set data to Monthly PieChart for rendering
     */
    void setMonthlyPieChartData(PieData pieData, double monthlyTotal);
}
