package com.duodian.admore.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.duodian.admore.R;

/**
 * Created by duodian on 2017/10/13.
 * 计划view
 */

public class CurrentPlanView extends View {

    private Paint paint;
    private DashPathEffect dashPathEffect;
    private int padding = 25;
    private RectF rectF;
    private RectF rectFPlan;
    private int planColor;

    public CurrentPlanView(Context context) {
        this(context, null);
    }

    public CurrentPlanView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CurrentPlanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CurrentPlanView);
        planColor = typedArray.getColor(R.styleable.CurrentPlanView_current_plan_color, getResources().getColor(R.color.blue));
        typedArray.recycle();
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        dashPathEffect = new DashPathEffect(new float[]{6, 6}, 0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF = new RectF(0, 0, w, h);
        rectFPlan = new RectF(padding, padding, getWidth() - padding, getHeight() - padding);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*
          周边虚线
         */
        paint.setColor(getResources().getColor(R.color.colorPrimary));
//        paint.setStrokeWidth(1);
//        paint.setPathEffect(dashPathEffect);
//        canvas.drawRoundRect(rectF, 8, 8, paint);
//        paint.setPathEffect(null);

         /*
          边框
         */
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(6);
        canvas.drawRoundRect(rectFPlan, 20, 20, paint);

        /*
          细节
         */
        paint.setColor(planColor);
        canvas.drawLine(getWidth() * 2 / 3, padding * 2 / 3, getWidth() * 2 / 3, padding * 4 / 3, paint);
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        canvas.drawLine(getWidth() / 3, padding * 2 / 3, getWidth() / 3, padding * 4 / 3, paint);
        canvas.drawLine(padding, getHeight() / 3, getWidth() - padding, getHeight() / 3, paint);
    }
}
