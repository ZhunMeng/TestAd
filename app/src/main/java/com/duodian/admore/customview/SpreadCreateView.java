package com.duodian.admore.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.duodian.admore.R;


/**
 * Created by duodian on 2017/10/11.
 * 创建推广图标
 */

public class SpreadCreateView extends View {

    private Paint paint;
    private DashPathEffect dashPathEffect;
    private RectF rectF;
    private Path path;
    private int padding = 30;
    private int plusColor;
    private Point centerPoint;//圆心
    private int radius;


    public SpreadCreateView(Context context) {
        this(context, null);
    }

    public SpreadCreateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpreadCreateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SpreadCreateView);
        plusColor = typedArray.getColor(R.styleable.SpreadCreateView_plus_color, getResources().getColor(R.color.blue));
        typedArray.recycle();
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        dashPathEffect = new DashPathEffect(new float[]{6, 6}, 0);
        path = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF = new RectF(0, 0, w, h);
        centerPoint = new Point((int) (getWidth() - padding * 1.5f),
                (int) (getHeight() - 1.5f * padding));
        radius = padding * 2 / 3;
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
          加号
         */
        paint.setColor(plusColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(centerPoint.x - radius, centerPoint.y, centerPoint.x + radius, centerPoint.y, paint);
        canvas.drawLine(centerPoint.x, centerPoint.y - radius, centerPoint.x, centerPoint.y + radius, paint);

        /*
          方框
         */
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        paint.setStrokeWidth(6);
        path.moveTo(getWidth() - padding, getHeight() / 2);
        path.lineTo(getWidth() - padding, padding * 2);
        path.quadTo(getWidth() - padding, padding, getWidth() - padding * 2, padding);
        path.lineTo(padding * 2, padding);
        path.quadTo(padding, padding, padding, padding * 2);
        path.lineTo(padding, getHeight() - padding * 2);
        path.quadTo(padding, getHeight() - padding, padding * 2, getHeight() - padding);
        path.lineTo(getWidth() / 2, getHeight() - padding);
        canvas.drawPath(path, paint);
    }

}
