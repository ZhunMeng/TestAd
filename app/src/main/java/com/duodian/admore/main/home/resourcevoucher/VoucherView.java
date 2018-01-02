package com.duodian.admore.main.home.resourcevoucher;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.duodian.admore.R;

/**
 * Created by duodian on 2017/10/21.
 * resource voucher background view
 */

public class VoucherView extends View {

    private Paint paint;

    private int type;
    private int status;
    private int radius;

    public VoucherView(Context context) {
        this(context, null);
    }

    public VoucherView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoucherView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VoucherView, defStyleAttr, 0);
        type = a.getInt(R.styleable.VoucherView_voucher_type, 0);
        a.recycle();
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = h / 26;

    }

    public void setTypeAndStatus(int type, int status) {
        this.type = type;
        this.status = status;
        Drawable backGroundDrawable;
        if (status == 1) {//未兑换
            if (type == 0) {
                backGroundDrawable = getResources().getDrawable(R.drawable.shape_gradient_round_rect_blue);
            } else if (type == 1) {
                backGroundDrawable = getResources().getDrawable(R.drawable.shape_gradient_round_rect_red);
            } else if (type == 2) {
                backGroundDrawable = getResources().getDrawable(R.drawable.shape_gradient_round_rect_violet);
            } else {
                backGroundDrawable = getResources().getDrawable(R.drawable.shape_gradient_round_rect_gray);
            }
        } else {
            backGroundDrawable = getResources().getDrawable(R.drawable.shape_gradient_round_corner_gray_f1);
        }

        setBackground(backGroundDrawable);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.WHITE);
        for (int i = 0; i < 6; i++) {
            canvas.drawCircle(-radius / 2, radius * 3 + i * 4 * radius, radius * 5 / 4, paint);
            canvas.drawCircle(getWidth() + radius / 2, radius * 3 + i * 4 * radius, radius * 5 / 4, paint);
        }

    }
}
