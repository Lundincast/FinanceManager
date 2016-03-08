package com.lundincast.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lundincast.presentation.R;

import butterknife.ButterKnife;

/**
 * Created by lundincast on 6/03/16.
 */
public class NumericKeyboardFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_numeric_keyboard, container, false);
        ButterKnife.bind(this, fragmentView);

        return fragmentView;
    }
}
