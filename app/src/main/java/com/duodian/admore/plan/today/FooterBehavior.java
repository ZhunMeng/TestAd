package com.duodian.admore.plan.today;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by duodian on 2017/10/31.
 * footer behavior
 */

public class FooterBehavior extends CoordinatorLayout.Behavior {
    public FooterBehavior() {
        super();
    }

    public FooterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof RecyclerView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float tranY = Math.abs(dependency.getTop());
        child.setTranslationY(tranY);
        return true;
    }
}
