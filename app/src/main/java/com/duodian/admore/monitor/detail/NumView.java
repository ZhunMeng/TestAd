package com.duodian.admore.monitor.detail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.duodian.admore.R;
import com.duodian.admore.utils.LogUtil;

/**
 * Created by duodian on 2017/12/26.
 * num view
 */

public class NumView extends View {

    private Paint paint;
    private int colorCircle;
    private int colorDraw;
    private int radius;
    private int centreX;
    private int centreY;
    private int strokeWidth;
    private int totalNum;
    private int passNumNow;
    private int degreeNow;
    private int percentNow;
    private int passNum;
    private int degree;
    private int percent;
    private Rect rect;
    private Rect passRect;
    private RectF viewRectF;
    private Rect percentRect;
    private int numberTextSize;
    private int percentTextSize;
    private LinearGradient linearGradient;

    private Point textPoint;

    public NumView(Context context) {
        this(context, null);
    }

    public NumView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MonitorDetailView, defStyleAttr, 0);
        colorCircle = typedArray.getColor(R.styleable.MonitorDetailView_circle_color, Color.WHITE);
        colorDraw = typedArray.getColor(R.styleable.MonitorDetailView_draw_color, 0);
        strokeWidth = typedArray.getColor(R.styleable.MonitorDetailView_stroke_width, 32);

        typedArray.recycle();
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        numberTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 28, getResources().getDisplayMetrics());
        percentTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LogUtil.e("VIEW", "onSizeChanged");
        centreX = w / 2;
        centreY = h / 2;
        radius = w / 2 - strokeWidth / 2;
        viewRectF = new RectF(strokeWidth / 2, strokeWidth / 2, w - strokeWidth / 2, h - strokeWidth / 2);
        linearGradient = new LinearGradient(0, 0, 0, getHeight(), getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryLight), Shader.TileMode.CLAMP);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画圆
        paint.setShader(linearGradient);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(centreX, centreY, radius, paint);

        if (rect == null) return;
        paint.setTextSize(numberTextSize);
        paint.setShader(null);
        paint.setColor(Color.WHITE);
        canvas.drawArc(viewRectF, -90, -degreeNow, false, paint);
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(String.valueOf(passNumNow), 0, String.valueOf(passNumNow).length(), getWidth() / 2 - rect.width() / 2, getHeight() / 2, paint);
        paint.setColor(getResources().getColor(R.color.translucent_white));
        canvas.drawText(" / " + String.valueOf(totalNum), 0, (" / " + String.valueOf(totalNum)).length(),
                getWidth() / 2 - rect.width() / 2 + passRect.width(), getHeight() / 2, paint);

        paint.setTextSize(percentTextSize);
        canvas.drawText(String.valueOf(percentNow + " % "), 0, String.valueOf(percentNow + " % ").length(),
                getWidth() / 2 - percentRect.width() / 2, getHeight() / 2 + percentRect.height() * 3, paint);

    }

    private ValueAnimator valueAnimator;

    public void setNum(final int passNum, final int totalNum) {
        this.passNum = passNum;
        this.totalNum = totalNum;
        if (totalNum <= 0) return;
        post(new Runnable() {
            @Override
            public void run() {
//                LogUtil.e("VIEW", "setNum");

                final float fraction = passNum * 1.0f / totalNum;
                degree = (int) (360 * fraction);
                percent = (int) (fraction * 100);

                paint.setTextSize(numberTextSize);
                String text = passNum + " / " + totalNum;
                rect = new Rect();
                paint.getTextBounds(text, 0, text.length(), rect);
                Paint.FontMetrics metrics = paint.getFontMetrics();
                LogUtil.e("VIEW", "--" + (metrics.ascent - metrics.top));
                passRect = new Rect();
                String passText = String.valueOf(passNum);
                paint.getTextBounds(passText, 0, passText.length(), passRect);
                percentRect = new Rect();
                paint.setTextSize(percentTextSize);
                paint.getTextBounds(String.valueOf(percent + " % "), 0, String.valueOf(percent + " % ").length(), percentRect);

                textPoint = new Point(getWidth() / 2 - rect.width() / 2, getHeight() / 2 - rect.height() - (int) metrics.ascent + (int) metrics.top);


                valueAnimator = ValueAnimator.ofFloat(0, 1f).setDuration(600);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float fraction = animation.getAnimatedFraction();
                        degreeNow = (int) (degree * fraction);
                        percentNow = (int) (percent * fraction);
                        passNumNow = (int) (passNum * fraction);
                        postInvalidate();
                    }
                });
                valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });
                valueAnimator.start();
            }
        });

    }

}
