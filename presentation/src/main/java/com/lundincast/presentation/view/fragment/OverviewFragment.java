package com.lundincast.presentation.view.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.lundincast.presentation.R;
import com.lundincast.presentation.dagger.components.TransactionComponent;
import com.lundincast.presentation.presenter.OverviewPresenter;
import com.lundincast.presentation.view.OverviewView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A {@link Fragment} subclass for "Overview" tab in Main Activity
 */
public class OverviewFragment extends BaseFragment implements OverviewView {

    @Inject OverviewPresenter overviewPresenter;

    @Bind(R.id.ll_loading) LinearLayout ll_loading;
    @Bind(R.id.piechart_monthly_distribution) PieChart piechart_monthly;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, fragmentView);

        // set up chart
        piechart_monthly.setDescription("");

        // radius of the center hole in percent of maximum radius
        piechart_monthly.setHoleRadius(80f);
        piechart_monthly.setTransparentCircleRadius(55f);
        // Disable text on slice
        piechart_monthly.setDrawSliceText(false);
        // Set center text properties
        piechart_monthly.setCenterTextColor(Color.parseColor("#FF9100"));
        piechart_monthly.setCenterTextSize(30f);
        piechart_monthly.setDrawCenterText(true);
        // Disable legend
        Legend l = piechart_monthly.getLegend();
        l.setEnabled(false);

        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
        this.loadData();
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

    private void initialize() {
        this.getComponent(TransactionComponent.class).inject(this);
        this.overviewPresenter.setView(this);
    }

    @Override
    public void setMonthlyPieChartData(PieData pieData, double monthlyTotal) {
        piechart_monthly.setData(pieData);
        piechart_monthly.setCenterText(String.format("%.2f", monthlyTotal) + " €");
        piechart_monthly.notifyDataSetChanged();
        piechart_monthly.invalidate();
    }

    private void loadData() {
        this.overviewPresenter.initialize();
    }
}
