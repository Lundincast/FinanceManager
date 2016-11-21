package com.lundincast.presentation.view.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.lundincast.presentation.model.CategoryModel;
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
import io.realm.Realm;

/**
 * A {@link Fragment} subclass for "Overview" tab in Main Activity
 */
public class OverviewFragment extends BaseFragment implements OverviewView,
                                                              OnChartValueSelectedListener,
                                                              AdapterView.OnItemSelectedListener {



    @Inject OverviewPresenter overviewPresenter;

    @Bind(R.id.ll_loading) LinearLayout ll_loading;
    @Bind(R.id.tv_overview_title) TextView tv_overview_title;
    @Bind(R.id.tv_total_income) TextView tv_total_income;
    @Bind(R.id.tv_total_expenses) TextView tv_total_expenses;
    @Bind(R.id.piechart_monthly_distribution) PieChart piechart_monthly;
    @Bind(R.id.sp_distribution_timeframe) Spinner sp_distribution_timeframe;
    @Bind(R.id.ll_barchart_loading) LinearLayout ll_barchart_loading;
    @Bind(R.id.barchart_category_history) BarChart barchart_category_history;
    @Bind(R.id.sp_category_list) Spinner sp_category_list;

    private double totalPrice;
    private double displayedPriceValue;
    private double displayedPercentage;
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
        piechart_monthly.setCenterTextSize(25f);
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
        leftAxis.setLabelCount(4, true);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = barchart_category_history.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawAxisLine(false);

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
                    OverviewFragment.this.setPieChartCenterText(null, displayedPriceValue, displayedPercentage);
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
        this.overviewPresenter.setCurrency(((MainActivity) getActivity()).sharedPreferences);
    }

    @Override
    public void setMonthOverviewTitle(String month) {
        tv_overview_title.setText("Overview - " + month);
    }

    @Override
    public void setMonthIncome(double totalMonthIncome) {
        tv_total_income.setText(String.valueOf(String.format("%.2f", totalMonthIncome)) + " " + overviewPresenter.currencySymbol);
    }

    @Override
    public void setMonthExpenses(double totalMonthExpenses) {
        tv_total_expenses.setText(String.valueOf(String.format("%.2f", totalMonthExpenses)) + " " + overviewPresenter.currencySymbol);
    }

    @Override
    public void setMonthlyPieChartData(PieData pieData, String category, double monthlyTotal, double percent) {
        this.totalPrice = monthlyTotal;
        if (monthlyTotal != 0) {
            piechart_monthly.setData(pieData);
            this.setPieChartCenterText(category, monthlyTotal, percent);
            piechart_monthly.notifyDataSetChanged();
            piechart_monthly.invalidate();
            piechart_monthly.animateXY(800, 800, Easing.EasingOption.EaseOutSine, Easing.EasingOption.EaseOutSine);
            piechart_monthly.setOnChartValueSelectedListener(this);
        }
        this.displayedPriceValue = monthlyTotal;
    }



    private void setPieChartCenterText(String category, double priceValue, double percent) {
        if (priceValue != 0) {
            if (category == null) {
                category = "TOTAL";
            }
            SpannableString centerText = new SpannableString(category.toUpperCase() +
                                                             "\n" + String.format("%.2f", priceValue) +
                                                             " " + overviewPresenter.currencySymbol);
            centerText.setSpan(new RelativeSizeSpan(0.5f), 0, category.length(), 0);
            if (percent != 0) {
                SpannableString percentLine = new SpannableString(String.format("%.2f", percent) + " %");
                percentLine.setSpan(new RelativeSizeSpan(0.5f), 0, percentLine.length(), 0);
                piechart_monthly.setCenterText(TextUtils.concat(centerText, "\n", percentLine));
            } else {
                piechart_monthly.setCenterText(centerText);
            }
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
        Realm realm = Realm.getDefaultInstance();
        CategoryModel category = realm.where(CategoryModel.class).equalTo("color", colorEntry).findFirst();
        this.setPieChartCenterText(category.getName(), e.getVal(), e.getVal() * 100 / totalPrice);
    }

    @Override
    public void onNothingSelected() {
        this.setPieChartCenterText(null, totalPrice, 0);
        this.displayedPercentage = 0;
        piechart_monthly.setCenterTextColor(Color.BLACK);
    }
}
