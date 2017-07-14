package com.suikajy.newsapp.module.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suikajy.newsapp.R;
import com.suikajy.newsapp.module.base.presenter.IRxBusPresenter;

/**
 * Created by suikajy on 2017/4/4.
 */

public class ToyFragment extends BaseFragment<IRxBusPresenter> {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toy, null);
        return view;
    }

    @Override
    protected void updateViews(boolean b) {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected int attachLayoutRes() {
        return 0;
    }

    @Override
    protected void initInjector() {

    }
}
