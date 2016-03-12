package com.lundincast.presentation.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.presenter.CreateCategoryPresenter;
import com.lundincast.presentation.view.activity.CreateOrUpdateCategoryActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * A {@link Fragment} subclass for creating new category
 */
public class CreateOrUpdateCategoryFragment extends BaseFragment
                                implements AdapterView.OnItemSelectedListener {

    @Bind(R.id.et_category_name) EditText et_category_name;
    @Bind(R.id.sp_category_color) Spinner sp_category_color;
    // View in activity can't be accessed from fragment
    ImageView iv_delete;
    ImageView iv_done;
    String color = "red";
    CreateOrUpdateCategoryActivity activity;
    private ArrayAdapter<CharSequence> spinnerAdapter;

    @Inject Realm realm;
    @Inject CreateCategoryPresenter createCategoryPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_create_category, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeInjection();
        ButterKnife.bind(this, getView());
        initializeSpinner();

        // get iv_done view from activity and set onClick listener
        activity = (CreateOrUpdateCategoryActivity) getActivity();
        iv_done = (ImageView) activity.findViewById(R.id.iv_done);
        sp_category_color.setOnItemSelectedListener(this);
        iv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_category_name.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Category name cannot be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    CreateOrUpdateCategoryFragment.this.createCategoryPresenter.saveCategory(activity.categoryId, et_category_name.getText().toString(), color);
                    activity.finish();
                }
            }
        });

        if (activity.categoryId != -1) {
            // set category details
            CategoryModel category = realm.where(CategoryModel.class).equalTo("id", activity.categoryId).findFirst();
            et_category_name.setText(category.getName());
            this.color = category.getColor();
            sp_category_color.setSelection(spinnerAdapter.getPosition(category.getColor()));
            // set delete option in activity toolbar
            iv_delete = (ImageView) activity.findViewById(R.id.iv_delete);
            iv_delete.setVisibility(View.VISIBLE);
            iv_delete.setClickable(true);
            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CreateOrUpdateCategoryFragment.this.createCategoryPresenter.deleteCategory(activity.categoryId);
                    activity.finish();
                }
            });
        }



    }

    private void initializeInjection() {
        this.getComponent(CategoryComponent.class).inject(this);
    }

    private void initializeSpinner() {
        // create an ArrayAdapter using the colors string array and a default spinner layout
        spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.colors_name,
                android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        sp_category_color.setAdapter(spinnerAdapter);

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
        color = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onStop() {
        super.onStop();
        this.realm = null;
    }
}
