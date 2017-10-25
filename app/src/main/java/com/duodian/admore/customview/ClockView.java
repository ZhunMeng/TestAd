package com.duodian.admore.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.duodian.admore.R;

/**
 * Created by duodian on 2017/10/13.
 * 时钟view
 */

public class ClockView extends View {
    private Paint paint;
    private DashPathEffect dashPathEffect;
    private int padding = 30;
    private Point centerPoint;//圆心
    private RectF rectF;
    private RectF rectFCircle;


    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        centerPoint = new Point(getWidth() / 2, getHeight() / 2);
        rectF = new RectF(0, 0, w, h);
        rectFCircle = new RectF(padding, padding, getWidth() - padding, getHeight() - padding);
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
          圆圈
         */
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(6);
        canvas.drawArc(rectFCircle, 75, 300, false, paint);

        /*
          指针
         */
        paint.setColor(getResources().getColor(R.color.blue));
        canvas.drawLine(centerPoint.x, centerPoint.y, centerPoint.x, centerPoint.y - 30, paint);
        canvas.rotate(135, centerPoint.x, centerPoint.y);
        canvas.drawLine(centerPoint.x, centerPoint.y, centerPoint.x, centerPoint.y - 30, paint);
    }
}
