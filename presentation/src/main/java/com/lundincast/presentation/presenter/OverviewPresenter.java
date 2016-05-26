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
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.model.TransactionModel;
import com.lundincast.presentation.view.activity.CreateTransactionActivity;
import com.lundincast.presentation.view.fragment.OverviewFragment;
import com.lundincast.presentation.utils.CustomDateFormatter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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
    private RealmChangeListener realmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            loadTransactionList();
        }
    };
    private RealmResults<TransactionModel> transactionListByMonth;
    private RealmChangeListener transactionListByMonthListener;
    private RealmResults<TransactionModel> transactionListByCategory;
    private RealmChangeListener transactionListByCategoryListener;

    private OverviewFragment viewOverView;

    private ArrayList<String> categorySpinnerList;
    private String currentCategorySpinnerChoice;

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
                OverviewPresenter.this.setTimeframeSpinner();
                OverviewPresenter.this.setCategorySpinner();
                OverviewPresenter.this.generateBarChartData(currentCategorySpinnerChoice);
            }
        };
//        transactionListByMonth.addChangeListener(transactionListByMonthListener);
        transactionListByCategoryListener = new RealmChangeListener() {
            @Override
            public void onChange() {
                OverviewPresenter.this.setCategorySpinner();
                OverviewPresenter.this.generateBarChartData(currentCategorySpinnerChoice);
            }
        };
//        transactionListByCategory.addChangeListener(transactionListByCategoryListener);
        realm.addChangeListener(realmChangeListener);
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
        this.setCategorySpinner();
        this.getCategoryHistoryList();
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
        this.viewOverView.setMonthlyPieChartData(pieData, null, monthlyTotal, 0);
    }

    private void renderCategoryBarChart(BarData data) {
        this.hideBarChartViewLoading();
        this.viewOverView.setCategoryBarChartData(data);
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
                .equalTo("transactionType", CreateTransactionActivity.TRANSACTION_TYPE_EXPENSE)
                .equalTo("month", cal.get(Calendar.MONTH))
                .equalTo("year", cal.get(Calendar.YEAR))
                .findAll();

        this.generatePieData();
    }

    private void setTimeframeSpinner() {
        ArrayList<String> values = new ArrayList<>();
        if (transactionListByMonth.size() != 0) {
            values.add("This month");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        if (realm.where(TransactionModel.class).equalTo("month", cal.get(Calendar.MONTH) - 1)
                                               .equalTo("year", cal.get(Calendar.YEAR)).findFirst() != null) {
            values.add("Last month");
        }
        this.viewOverView.setSpinnerDataAndRender(values);
    }

    private void getCategoryHistoryList() {
        if (categorySpinnerList.size() == 0) {
            this.generateBarChartData(null);
        } else if (currentCategorySpinnerChoice != null) {
            this.generateBarChartData(currentCategorySpinnerChoice);
        } else {
            this.generateBarChartData(categorySpinnerList.get(0));
        }
    }

    private void setCategorySpinner() {
        categorySpinnerList = new ArrayList<>();
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        Calendar sixMonthAgo = Calendar.getInstance();
        sixMonthAgo.add(Calendar.MONTH, -5);
        sixMonthAgo.set(Calendar.DAY_OF_MONTH, 1);
        RealmResults<TransactionModel> lastSixMonthsTransactionList =
                realm.where(TransactionModel.class)
                            .equalTo("transactionType", CreateTransactionActivity.TRANSACTION_TYPE_EXPENSE)
                            .between("date", sixMonthAgo.getTime(), today.getTime())
                            .findAll();
        for (TransactionModel transaction : lastSixMonthsTransactionList) {
            String categoryName = transaction.getCategory().getName();
            if (!categorySpinnerList.contains(categoryName)) {
                categorySpinnerList.add(categoryName);
            }
        }
        this.viewOverView.setCategorySpinnerDataAndRender(categorySpinnerList);
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
                xVals.add(CustomDateFormatter.getShortMonthName(sixMonthAgo.get(Calendar.MONTH)));
                sixMonthAgo.add(Calendar.MONTH, 1);
            }

            // fill yTotals by adding transaction price to total per month
            double[] yTotals = new double[xVals.size()];
            Calendar cal2 = Calendar.getInstance();
            for (TransactionModel transaction : transactionListByCategory) {
                cal2.setTime(transaction.getDate());
                String monthName = CustomDateFormatter.getShortMonthName(cal2.get(Calendar.MONTH));

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
            set.setValueFormatter(new CustomValueFormatter());

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set);

            BarData data = new BarData(xVals, dataSets);
            data.setValueTextSize(10f);

            this.renderCategoryBarChart(data);
        }
    }

    public void updateCategoryBarChartData(AdapterView<?> parent, View view, int position, long id) {
        TextView tv = (TextView) view;
        this.currentCategorySpinnerChoice = tv.getText().toString();
        if (currentCategorySpinnerChoice != null) {
            this.generateBarChartData(currentCategorySpinnerChoice);
        } else {
            this.generateBarChartData(tv.getText().toString());
        }
    }


    public class CustomValueFormatter implements ValueFormatter {

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return format(value);
        }

        private String format(Number n) {
            NumberFormat format = DecimalFormat.getInstance();
            format.setMinimumFractionDigits(0);
            format.setMaximumFractionDigits(2);
            return format.format(n);
        }
    }

}
