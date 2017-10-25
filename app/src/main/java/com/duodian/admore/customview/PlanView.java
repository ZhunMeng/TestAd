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

public class PlanView extends View {

    private Paint paint;
    private DashPathEffect dashPathEffect;
    private int padding = 30;
    private RectF rectF;
    private RectF rectFPlan;
    private int planColor;

    public PlanView(Context context) {
        this(context, null);
    }

    public PlanView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PlanView);
        planColor = typedArray.getColor(R.styleable.PlanView_plan_color, getResources().getColor(R.color.blue));
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
        rectFPlan = new RectF(padding, padding, getWidth() - padding, getHeight() - padding * 2 / 3);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*
          周边虚线
         */
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        paint.setStrokeWidth(1);
        paint.setPathEffect(dashPathEffect);
        canvas.drawRoundRect(rectF, 8, 8, paint);
        paint.setPathEffect(null);

         /*
          边框
         */
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(6);
        canvas.drawRoundRect(rectFPlan, 20, 20, paint);

        /*
          细节
         */
        canvas.drawLine(getWidth() / 3, padding * 2 / 3, getWidth() / 3, padding * 3 / 2, paint);
        canvas.drawLine(getWidth() * 2 / 3, padding * 2 / 3, getWidth() * 2 / 3, padding * 3 / 2, paint);
        canvas.drawLine(getWidth() / 3 - padding / 6, getHeight() / 2 - padding / 2, getWidth() / 3 + padding / 6, getHeight() / 2 - padding / 2, paint);
        canvas.drawLine(getWidth() / 3 + padding, getHeight() / 2 - padding / 2, getWidth() * 2 / 3 + padding / 6, getHeight() / 2 - padding / 2, paint);

        canvas.drawLine(getWidth() / 3 - padding / 6, getHeight() / 2 + padding / 2, getWidth() / 3 + padding / 6, getHeight() / 2 + padding / 2, paint);
        canvas.drawLine(getWidth() / 3 + padding, getHeight() / 2 + padding / 2, getWidth() * 2 / 3 + padding / 6, getHeight() / 2 + padding / 2, paint);

        paint.setColor(planColor);
        canvas.drawLine(getWidth() / 3 - padding / 6, getHeight() / 2 + padding * 3 / 2, getWidth() / 3 + padding / 6, getHeight() / 2 + padding * 3 / 2, paint);
        canvas.drawLine(getWidth() / 3 + padding, getHeight() / 2 + padding * 3 / 2, getWidth() * 2 / 3 + padding / 6, getHeight() / 2 + padding * 3 / 2, paint);
    }
}
