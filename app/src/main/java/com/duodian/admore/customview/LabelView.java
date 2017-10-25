package com.duodian.admore.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.duodian.admore.R;
import com.duodian.admore.utils.LogUtil;

/**
 * Created by duodian on 2017/10/24.
 * 标签view
 */

public class LabelView extends View {

    private Paint paint;
    private int color;
    private int degree;
    private Point point;
    private int radius;
    private RectF rectBig;
    private RectF rectSmall;

    public void setStatus(int status) {
        this.status = status;
        if (status == 2) {
            color = getResources().getColor(R.color.blue);
        } else {
            color = getResources().getColor(R.color.red);
        }
        paint.setColor(color);
        invalidate();
    }

    private int status;
    private Rect rectText;

    public LabelView(Context context) {
        this(context, null);
    }

    public LabelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LabelView, defStyleAttr, 0);
        color = a.getInt(R.styleable.LabelView_label_color, getResources().getColor(R.color.blue));
        degree = a.getInt(R.styleable.LabelView_label_degree, -45);
        status = a.getInt(R.styleable.LabelView_label_type, 1);
        a.recycle();
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics()));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        point = new Point(w / 2, h / 2);
        radius = w / 2;
        rectText = new Rect();
        rectBig = new RectF(0, 0, w, h);
        rectSmall = new RectF(26, 26, w - 26, h - 26);
        LogUtil.e("LabelView", point.x + "-" + point.y + "-" + paint.getTextSize() + "-" + radius + "-" + degree);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.rotate(degree, point.x, point.y);
        canvas.translate(-radius / 2, 0);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        canvas.drawArc(rectBig, -90f, 180f, false, paint);
        paint.setStrokeWidth(2);
//        canvas.drawCircle(point.x, point.y, radius - 28, paint);
        canvas.drawArc(rectSmall, -90f, 180f, false, paint);
        paint.getTextBounds("已", 0, "已".length(), rectText);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText("已", point.x - rectText.width() / 2, point.y - 8, paint);
        paint.getTextBounds("兑换", 0, "兑换".length(), rectText);
        if (status == 2) {//已兑换
            canvas.drawText("兑换", point.x - rectText.width() / 2, point.y + rectText.height(), paint);
        } else {//已过期
            canvas.drawText("过期", point.x - rectText.width() / 2, point.y + rectText.height(), paint);
        }

    }

}
