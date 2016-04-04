package com.lundincast.presentation.view.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.lundincast.presentation.R;
import com.lundincast.presentation.dagger.components.TransactionComponent;
import com.lundincast.presentation.presenter.OverviewPresenter;
import com.lundincast.presentation.view.OverviewView;
import com.lundincast.presentation.view.activity.MainActivity;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A {@link Fragment} subclass for "Overview" tab in Main Activity
 */
public class OverviewFragment extends BaseFragment implements OverviewView,
                                                              OnChartValueSelectedListener,
                                                              AdapterView.OnItemSelectedListener {



    @Inject OverviewPresenter overviewPresenter;

    @Bind(R.id.ll_loading) LinearLayout ll_loading;
    @Bind(R.id.piechart_monthly_distribution) PieChart piechart_monthly;
    @Bind(R.id.sp_distribution_timeframe) Spinner sp_distribution_timeframe;
    @Bind(R.id.ll_barchart_loading) LinearLayout ll_barchart_loading;
    @Bind(R.id.barchart_category_history) BarChart barchart_category_history;
    @Bind(R.id.sp_category_list) Spinner sp_category_list;

    private double totalPrice;
    private double displayedPriceValue;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, fragmentView);

        // set up PieChart
        piechart_monthly.setDescription("");
        // radius of the center hole in percent of maximum radius
        piechart_monthly.setHoleRadius(80f);
        piechart_monthly.setTransparentCircleRadius(55f);
        // Disable text on slice
        piechart_monthly.setDrawSliceText(false);
        // Set center text properties
        piechart_monthly.setCenterTextColor(Color.DKGRAY);
        piechart_monthly.setCenterTextSize(30f);
        piechart_monthly.setDrawCenterText(true);
        // Disable legend
        Legend l = piechart_monthly.getLegend();
        l.setEnabled(false);

        // set up BarChart
        barchart_category_history.setDescription("");
        barchart_category_history.setDrawBarShadow(false);
        barchart_category_history.setDrawGridBackground(false);
        barchart_category_history.setTouchEnabled(false);

        XAxis xAxis = barchart_category_history.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        YAxis leftAxis = barchart_category_history.getAxisLeft();
        leftAxis.setSpaceBottom(0);

        YAxis rightAxis = barchart_category_history.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);

        Legend leg = barchart_category_history.getLegend();
        leg.setEnabled(false);

        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
        this.loadData();

        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("pref_key_currency")) {
                    OverviewFragment.this.setPieChartCenterText(displayedPriceValue);
                }
            }
        };
        ((MainActivity) getActivity()).sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override public void onResume() {
        super.onResume();
        this.overviewPresenter.resume();
    }

    @Override public void onPause() {
        super.onPause();
        this.overviewPresenter.pause();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
        this.overviewPresenter.destroy();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * Show a view with a progress bar indicating a loading process.
     */
    @Override
    public void showLoading() {
        this.ll_loading.setVisibility(View.VISIBLE);
    }

    /**
     * Hide a loading view.
     */
    @Override
    public void hideLoading() {
        this.ll_loading.setVisibility(View.GONE);
    }

    @Override
    public void showBarChartLoading() {
        this.ll_barchart_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBarChartLoading() {
        this.ll_barchart_loading.setVisibility(View.GONE);
    }

    private void initialize() {
        this.getComponent(TransactionComponent.class).inject(this);
        this.overviewPresenter.setView(this);
    }

    @Override
    public void setMonthlyPieChartData(PieData pieData, double monthlyTotal) {
        this.totalPrice = monthlyTotal;
        if (monthlyTotal != 0) {
            piechart_monthly.setData(pieData);
            this.setPieChartCenterText(monthlyTotal);
            piechart_monthly.notifyDataSetChanged();
            piechart_monthly.invalidate();
            piechart_monthly.animateXY(800, 800, Easing.EasingOption.EaseOutSine, Easing.EasingOption.EaseOutSine);
            piechart_monthly.setOnChartValueSelectedListener(this);
        }
        this.displayedPriceValue = monthlyTotal;
    }



    private void setPieChartCenterText(double priceValue) {
        if (priceValue != 0) {
            String currencyPref = ((MainActivity) getActivity()).sharedPreferences.getString("pref_key_currency", "1");
            if (currencyPref.equals("1")) {
                piechart_monthly.setCenterText(String.format("%.2f", priceValue) + " €");
            } else if (currencyPref.equals("2")) {
                piechart_monthly.setCenterText(String.format("%.2f", priceValue) + " $");
            } else {
                piechart_monthly.setCenterText(String.format("%.2f", priceValue) + " £");
            }
            this.displayedPriceValue = priceValue;
        }
    }

    @Override
    public void setSpinnerDataAndRender(ArrayList<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                                                android.R.layout.simple_spinner_item,
                                                                data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_distribution_timeframe.setAdapter(adapter);
        sp_distribution_timeframe.setOnItemSelectedListener(this);
    }

    @Override
    public void setCategoryBarChartData(BarData data) {
        barchart_category_history.setData(data);
        barchart_category_history.notifyDataSetChanged();
        barchart_category_history.invalidate();
        barchart_category_history.animateXY(800, 1000, Easing.EasingOption.EaseOutSine, Easing.EasingOption.EaseOutSine);
    }

    @Override
    public void setCategorySpinnerDataAndRender(ArrayList<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                                                    android.R.layout.simple_spinner_item,
                                                                    data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_category_list.setAdapter(adapter);
        sp_category_list.setSelection(0, false);
        sp_category_list.setOnItemSelectedListener(this);
    }


    private void loadData() {
        this.overviewPresenter.initialize();
    }

    // Callbacks for spinner selection
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_distribution_timeframe:
                // un-highlight chart item just in case
                piechart_monthly.highlightValue(null);
                this.overviewPresenter.updatePieChartData(parent, view, position, id);
                break;
            case R.id.sp_category_list:
                this.overviewPresenter.updateCategoryBarChartData(parent, view, position, id);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing here
    }

    // Callbacks for pieChart selection
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Integer colorEntry = piechart_monthly.getData().getDataSet().getColors().get(e.getXIndex());
        piechart_monthly.setCenterTextColor(colorEntry);
        this.setPieChartCenterText(e.getVal());
    }

    @Override
    public void onNothingSelected() {
        this.setPieChartCenterText(totalPrice);
        piechart_monthly.setCenterTextColor(Color.BLACK);
    }
}
