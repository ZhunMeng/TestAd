package com.duodian.admore.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.duodian.admore.utils.LogUtil;

import java.util.Random;

/**
 * Created by duodian on 2017/9/30.
 * 星空View
 */

public class NightSkyView extends View {

    private static final String TAG = "NightSkyView";
    private Paint paint;
    private Paint lightPaint;
    private Paint starPaint;
    private Paint meteorPaint;
    private String moonColor = "#e0dae5";
    private RadialGradient moonRadialGradient;
    private RadialGradient starRadialGradient;
    private LinearGradient meteorLinearGradient;
    private Random random;
    private int[] starsX;
    private int[] starsY;
    private int starsNum = 100;

    public NightSkyView(Context context) {
        this(context, null);
    }

    public NightSkyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NightSkyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor(moonColor));
        paint.setStyle(Paint.Style.FILL);
        lightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lightPaint.setStyle(Paint.Style.FILL);
        starPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        starPaint.setStyle(Paint.Style.FILL);
        meteorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        meteorPaint.setStyle(Paint.Style.FILL);
        meteorPaint.setStrokeCap(Paint.Cap.ROUND);

        random = new Random();
        starsX = new int[starsNum];
        starsY = new int[starsNum];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 4, getHeight() / 4, getWidth() / 10, paint);
        canvas.drawCircle(getWidth() / 4, getHeight() / 4, getWidth() / 8, lightPaint);
        long time = System.currentTimeMillis();

        for (int i = 0; i < starsX.length; i++) {
            starRadialGradient = new RadialGradient(starsX[i], starsY[i], 16 * random.nextFloat() + 1,
                    Color.parseColor("#ffe0dae5"), Color.parseColor("#00000000"), Shader.TileMode.CLAMP);
            starPaint.setShader(starRadialGradient);
            canvas.drawCircle(starsX[i], starsY[i], 10, starPaint);
        }
        LogUtil.e(TAG, "timeUsed:" + (System.currentTimeMillis() - time));
        canvas.save();
        canvas.rotate(-45, getWidth() / 2, getHeight() / 2);
        canvas.drawRoundRect(getWidth() / 2+ getWidth() / 4, getHeight() / 2, getWidth() / 2 + getWidth() / 4+ 500, getHeight() / 2 + 10, 20, 20, meteorPaint);
        canvas.restore();
        getStars();
//        invalidate();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        LogUtil.e(TAG, w + ":" + h);
        moonRadialGradient = new RadialGradient(getWidth() / 4, getHeight() / 4, getWidth() / 8,
                Color.parseColor("#ffe0dae5"), Color.parseColor("#00000000"), Shader.TileMode.CLAMP);
        lightPaint.setShader(moonRadialGradient);
        meteorLinearGradient = new LinearGradient(getWidth() / 2 + getWidth() / 4, getHeight() / 2, getWidth() / 2 + getWidth() / 4+ 500, getHeight() / 2 + 10,
                Color.parseColor("#ffe0dae5"), Color.parseColor("#00000000"), Shader.TileMode.CLAMP);
        meteorPaint.setShader(meteorLinearGradient);
        getStars();
    }

    public void getStars() {
        for (int i = 0; i < starsNum; i++) {
            starsX[i] = (int) (random.nextFloat() * getWidth());
            starsY[i] = (int) (random.nextFloat() * getHeight());
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }
}
