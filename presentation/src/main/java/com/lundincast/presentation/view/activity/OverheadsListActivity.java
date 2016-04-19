package com.lundincast.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.lundincast.presentation.R;
import com.lundincast.presentation.dagger.HasComponent;
import com.lundincast.presentation.dagger.components.DaggerOverheadsComponent;
import com.lundincast.presentation.dagger.components.OverheadsComponent;
import com.lundincast.presentation.dagger.modules.OverheadsModule;
import com.lundincast.presentation.model.OverheadModel;
import com.lundincast.presentation.navigation.Navigator;
import com.lundincast.presentation.view.fragment.OverheadsListFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity to display the list of overheads.
 */
public class OverheadsListActivity  extends BaseActivity implements HasComponent<OverheadsComponent>,
                                                                OverheadsListFragment.OverheadsListListener {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, OverheadsListActivity.class);
    }

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.iv_back) ImageView iv_back;
    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.iv_add_icon) ImageView iv_add_icon;

    @Inject Navigator navigator;

    private OverheadsComponent overheadsComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_overheads);
        ButterKnife.bind(this);

        setUpToolbar();

        initializeInjector();
    }

    private void setUpToolbar() {
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        tv_title.setText(R.string.overheads);
    }

    private void initializeInjector() {
        this.getApplicationComponent().inject(this);
        this.overheadsComponent = DaggerOverheadsComponent.builder()
                .applicationComponent(getApplicationComponent())
                .overheadsModule(new OverheadsModule())
                .build();
    }

    @OnClick(R.id.iv_back)
    void onBackClicked() {
        finish();
    }

    @Override
    public OverheadsComponent getComponent() {
        return overheadsComponent;
    }

    @Override
    public void onOverheadsClicked(OverheadModel overheadModel) {

    }
}
