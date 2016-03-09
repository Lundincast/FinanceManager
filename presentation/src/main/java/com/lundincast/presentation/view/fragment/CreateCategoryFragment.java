package com.lundincast.presentation.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.lundincast.presentation.R;
import com.lundincast.presentation.dagger.components.CategoryComponent;
import com.lundincast.presentation.presenter.CreateCategoryPresenter;
import com.lundincast.presentation.view.activity.CreateCategoryActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A {@link Fragment} subclass for creating new category
 */
public class CreateCategoryFragment extends BaseFragment
                                implements AdapterView.OnItemSelectedListener {

    @Bind(R.id.et_category_name) EditText et_category_name;
    @Bind(R.id.sp_category_color) Spinner sp_category_color;
    // View in activity can't be accessed from fragment
    ImageView iv_done;
    private CreateCategoryActivity activity;

    @Inject CreateCategoryPresenter createCategoryPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_create_category, container, false);

        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeInjection();
        ButterKnife.bind(this, getView());
        initializeSpinner();

        // get iv_done view from activity and set onClick listener
        activity = (CreateCategoryActivity) getActivity();
        iv_done = (ImageView) activity.findViewById(R.id.iv_done);
        iv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateCategoryFragment.this.createCategoryPresenter.saveCategory();
                CreateCategoryFragment.this.activity.finish();
            }
        });
    }

    private void initializeInjection() {
        this.getComponent(CategoryComponent.class).inject(this);
    }

    private void initializeSpinner() {
        // create an ArrayAdapter using the colors string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.colors_array,
                android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        sp_category_color.setAdapter(adapter);

        // set Spinner's listeners
        sp_category_color.setOnItemSelectedListener(this);
        sp_category_color.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Hide keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_category_name.getWindowToken(), 0);
                return false;
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
