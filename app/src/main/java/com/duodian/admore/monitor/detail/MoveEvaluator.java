package com.duodian.admore.monitor.detail;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by duodian on 2017/12/26.
 * MoveEvaluator
 */

public class MoveEvaluator implements TypeEvaluator<PointF> {

    private PointF controlPoint;

    public MoveEvaluator(PointF controlPoint) {
        this.controlPoint = controlPoint;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        return calculate(fraction, startValue, controlPoint, endValue);
    }

    private PointF calculate(float fraction, PointF pointF1, PointF pointF2, PointF pointF3) {
        PointF pointF = new PointF();
        float temp = 1 - fraction;
        pointF.x = temp * temp * pointF1.x + 2 * fraction * temp * pointF2.x + fraction * fraction * pointF3.x;
        pointF.y = temp * temp * pointF1.y + 2 * fraction * temp * pointF2.y + fraction * fraction * pointF3.y;
        return pointF;
    }

}
