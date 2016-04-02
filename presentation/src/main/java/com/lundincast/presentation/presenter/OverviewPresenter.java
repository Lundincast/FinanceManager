package com.lundincast.presentation.presenter;

import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.model.TransactionModel;
import com.lundincast.presentation.view.fragment.OverviewFragment;
import com.lundincast.presentation.view.utilities.FullMonthDateFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
public class OverviewPresenter implements Presenter {

    private final Realm realm;
    private RealmResults<TransactionModel> transactionListByMonth;
    private RealmChangeListener transactionListByMonthListener;
    private RealmResults<TransactionModel> transactionListByCategory;
    private RealmChangeListener transactionListByCategoryListener;

    private OverviewFragment viewOverView;

    @Inject
    public OverviewPresenter() {
        this.realm = Realm.getDefaultInstance();
    }

    /**
     * Setter method to reference corresponding view (OverviewFragment in this case)
     *
     * @param view
     */
    public void setView(OverviewFragment view) {
        this.viewOverView = view;
    }

    @Override
    public void resume() {
        transactionListByMonthListener = new RealmChangeListener() {
            @Override
            public void onChange() {
                OverviewPresenter.this.generatePieData();
            }
        };
        transactionListByMonth.addChangeListener(transactionListByMonthListener);
        transactionListByCategoryListener = new RealmChangeListener() {
            @Override
            public void onChange() {
                OverviewPresenter.this.generateBarChartData("Housing");
            }
        };
        transactionListByCategory.addChangeListener(transactionListByCategoryListener);
    }

    @Override
    public void pause() {
        transactionListByMonth.removeChangeListener(transactionListByMonthListener);
        transactionListByCategory.removeChangeListener(transactionListByCategoryListener);
    }

    @Override
    public void destroy() {

    }

    /**
     * Initializes the presenter by retrieving the monthly transaction list.
     */
    public void initialize() {
        this.loadTransactionList();
    }

    /**
     * Loads transactions.
     */
    private void loadTransactionList() {
        this.showViewLoading();
        this.getMonthlyOverallTransactionList(null);
        this.setTimeframeSpinner();
        this.getCategoryHistoryList();
        this.setCategorySpinner();
    }

    private void showViewLoading() {
        this.viewOverView.showLoading();
    }

    private void hideViewLoading() {
        this.viewOverView.hideLoading();
    }

    private void showBarChartViewLoading() {
        this.viewOverView.showBarChartLoading();
    }

    private void hideBarChartViewLoading() {
        this.viewOverView.hideBarChartLoading();
    }

    private void renderMonthlyPieChart(PieData pieData, double monthlyTotal) {
        this.hideViewLoading();
        this.viewOverView.setMonthlyPieChartData(pieData, monthlyTotal);
    }

    private void renderCategoryBarChart(BarData data) {
        this.hideBarChartViewLoading();
        if (data != null) {
            this.viewOverView.setCategoryBarChartData(data);
        }
    }

    private void getMonthlyOverallTransactionList(Calendar setCal) {
        Calendar cal = Calendar.getInstance();
        if (setCal == null) {
            cal.setTime(new Date());
        } else {
            cal.setTime(setCal.getTime());
        }

        // get data from db depending on date
        transactionListByMonth = realm.where(TransactionModel.class)
                .equalTo("month", cal.get(Calendar.MONTH))
                .equalTo("year", cal.get(Calendar.YEAR))
                .findAll();

        this.generatePieData();
    }

    private void setTimeframeSpinner() {
        ArrayList<String> values = new ArrayList<>();
        values.add("This month");
        values.add("Last month");
        this.viewOverView.setSpinnerDataAndRender(values);
    }

    private void getCategoryHistoryList() {

        this.generateBarChartData("Housing");   // TODO useless ??
    }

    private void setCategorySpinner() {
        ArrayList<String> values = new ArrayList<>();
        RealmResults<CategoryModel> categories = realm.where(CategoryModel.class).findAll();
        for (CategoryModel category : categories) {
            values.add(category.getName());
        }
        this.viewOverView.setCategorySpinnerDataAndRender(values);
    }

    private void generatePieData() {
        // There may be a better way to do this. HashMap<K,V> looked attractive but
        // it doesn't accept a double as value

        ArrayList<String> categoryNames = new ArrayList<String>();
        int position;



        // Build categoryNames array from transactionListByMonth RealmResult
        for (TransactionModel transaction : transactionListByMonth) {
            String name = transaction.getCategory().getName();
            if (!categoryNames.contains(name)) {
                categoryNames.add(name);
            }
        }

        // Create new array for totals per category with same size as categoryNames
        double[] totalsPerCategory = new double[categoryNames.size()];

        // calculate total per category by iterating again through transactionListByMonth
        for (TransactionModel transaction : transactionListByMonth) {
            String category = transaction.getCategory().getName();
            // Get category position in categoryNames ArrayList
            position = categoryNames.indexOf(category);
            // fill totalsPerCategory array with prices
            totalsPerCategory[position] = totalsPerCategory[position] + transaction.getPrice();
        }

        // Calculate total spending for month
        double totalSpent = 0;
        for (double total : totalsPerCategory) {
            totalSpent += total;
        }

        // Set entries for PieChart
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < totalsPerCategory.length; i++) {
            entries.add(new Entry((float) totalsPerCategory[i], i));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");

        // build colors array from resources
        int[] colorsArray = new int[categoryNames.size()];
        for (int i = 0; i < categoryNames.size(); i++) {
            CategoryModel category = realm.where(CategoryModel.class).equalTo("name", categoryNames.get(i)).findFirst();
            int intColor = category.getColor();
            String hexColor = String.format("#%06X", (0xFFFFFF & intColor));
            colorsArray[i] = Color.parseColor(hexColor);
        }

        dataSet.setColors(colorsArray);
        dataSet.setSliceSpace(3f);

        PieData pieData = new PieData(categoryNames, dataSet);
        pieData.setDrawValues(false);

        this.renderMonthlyPieChart(pieData, totalSpent);
    }

    public void updatePieChartData(AdapterView<?> parent, View view, int position, long id) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        if (position == 1) {
            cal.add(Calendar.MONTH, -1);
        }
        this.getMonthlyOverallTransactionList(cal);
    }

    private void generateBarChartData(String categoryName) {
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());

        Calendar sixMonthAgo = Calendar.getInstance();
        sixMonthAgo.add(Calendar.MONTH, -5);
        sixMonthAgo.set(Calendar.DAY_OF_MONTH, 1);

        transactionListByCategory = realm.where(TransactionModel.class)
                                                    .equalTo("category.name", categoryName)
                                                    .between("date", sixMonthAgo.getTime(), today.getTime())
                                                    .findAll();

        if (transactionListByCategory.size() == 0) {
            this.renderCategoryBarChart(null);
        } else {
            // create months array
            ArrayList<String> xVals = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                xVals.add(FullMonthDateFormatter.getShortMonthName(sixMonthAgo.get(Calendar.MONTH)));
                sixMonthAgo.add(Calendar.MONTH, 1);
            }

            // fill yTotals by adding transaction price to total per month
            double[] yTotals = new double[xVals.size()];
            Calendar cal2 = Calendar.getInstance();
            for (TransactionModel transaction : transactionListByCategory) {
                cal2.setTime(transaction.getDate());
                String monthName = FullMonthDateFormatter.getShortMonthName(cal2.get(Calendar.MONTH));

                yTotals[xVals.indexOf(monthName)] += transaction.getPrice();
            }

            // fill yVals with totals
            ArrayList<BarEntry> yVals = new ArrayList<>();
            for (int i = 0; i < xVals.size(); i++) {
                yVals.add(new BarEntry((float) yTotals[i], i));
            }

            BarDataSet set = new BarDataSet(yVals, "DataSet");

            // set color for this set
            CategoryModel category = realm.where(CategoryModel.class).equalTo("name", categoryName).findFirst();
            if (category != null) {
                int intColor = category.getColor();
                String hexColor = String.format("#%06X", (0xFFFFFF & intColor));
                set.setColor(Color.parseColor(hexColor));
            }

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set);

            BarData data = new BarData(xVals, dataSets);
            data.setValueTextSize(10f);

            this.renderCategoryBarChart(data);
        }
    }

    public void updateCategoryBarChartData(AdapterView<?> parent, View view, int position, long id) {
        TextView tv = (TextView) view;
        this.generateBarChartData(tv.getText().toString());
    }
}
