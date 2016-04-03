package com.lundincast.presentation.view.fragment;

import android.app.Fragment;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
public class CreateOrUpdateCategoryFragment extends BaseFragment {

    @Bind(R.id.iv_category_icon) ImageView iv_category_icon;
    @Bind(R.id.et_category_name) EditText et_category_name;
    // View in activity can't be accessed from fragment
    ImageView iv_delete;
    ImageView iv_done;

    @Inject CreateCategoryPresenter createCategoryPresenter;

    private CreateOrUpdateCategoryActivity activity;
    private Realm realm;
    private String color;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_create_or_update_category, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeInjection();
        ButterKnife.bind(this, getView());
        // TODO
        this.realm = Realm.getDefaultInstance();
        activity = (CreateOrUpdateCategoryActivity) getActivity();

        // get iv_done view from activity and set onClick listener
        iv_done = (ImageView) activity.findViewById(R.id.iv_done);
        iv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_category_name.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Category name cannot be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    CreateOrUpdateCategoryFragment.this.createCategoryPresenter
                            .saveCategory(activity.categoryId, et_category_name.getText().toString(), activity.color);
                    activity.finish();
                }
            }
        });

        if (activity.categoryId != -1) {
            // Toolbar title by default is "New category". Set to "Edit category" in this case.
            TextView tv_title = (TextView) activity.findViewById(R.id.tv_title);
            tv_title.setText(R.string.edit_category);
            // set category details
            CategoryModel category = realm.where(CategoryModel.class).equalTo("id", activity.categoryId).findFirst();
            et_category_name.setText(category.getName());
            activity.color = category.getColor();
            // set category circle drawable color
            LayerDrawable bgDrawable = (LayerDrawable) iv_category_icon.getBackground();
            GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.circle_id);
            shape.setColor(category.getColor());

            // set delete option in activity toolbar
            iv_delete = (ImageView) activity.findViewById(R.id.iv_delete);
            iv_delete.setVisibility(View.VISIBLE);
            iv_delete.setClickable(true);
            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CreateOrUpdateCategoryFragment.this.displayDeleteConfirmationDialog();
                }
            });
        } else {
            LayerDrawable bgDrawable = (LayerDrawable) iv_category_icon.getBackground();
            GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.circle_id);
            activity.color = 0xfff44336;
            shape.setColor(activity.color); // Set red as default
        }

        // Set onclicklistener on category circle ImageView
        iv_category_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CreateOrUpdateCategoryActivity) CreateOrUpdateCategoryFragment.this.getActivity()).displayColorChooserDialog();
            }
        });
    }

    private void initializeInjection() {
        this.getComponent(CategoryComponent.class).inject(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        this.realm = null;
    }

    public void displayDeleteConfirmationDialog() {
        // Display dialog to ask user confirmation to delete category
        new MaterialDialog.Builder(getActivity())
                .title(R.string.delete_category_title_question)
                .content(R.string.delete_category_complete_question)
                .positiveText(R.string.delete)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        CreateOrUpdateCategoryFragment.this.createCategoryPresenter.deleteCategory(activity.categoryId);
                        dialog.dismiss();
                        activity.finish();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * Called from CreateOrUpdateCategoryActivity after a color is selected
     *
     * @param selectedColor The @ColorInt selected from dialog
     */
    public void onColorSelected(@ColorInt int selectedColor) {
        LayerDrawable bgDrawable = (LayerDrawable) iv_category_icon.getBackground();
        GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.circle_id);
        shape.setColor(selectedColor);
    }
}
