package com.duodian.admore.play.create;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.duodian.admore.R;
import com.duodian.admore.utils.LogUtil;
import com.duodian.admore.utils.ToastUtil;
import com.duodian.admore.utils.Util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by duodian on 2017/10/17.
 * 添加keyword fragment
 */

public class ContentDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener, OnPlayPlanCreateActivityActionListener {

    private static final String TAG = "ContentDialogFragment";
    private ContentListener contentListener;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetBehavior bottomSheetBehavior;
    private View rootView;
    private int position = -1;
    private ContentParam contentParams;

    public ContentDialogFragment() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ContentListener) {
            contentListener = (ContentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        contentListener = null;
    }

    @BindView(R.id.linear_contentParam)
    LinearLayout linear_contentParam;

    @BindView(R.id.frame_contentParam)
    FrameLayout frame_contentParam;

    @BindView(R.id.textView_addComplete)
    TextView textView_addComplete;

    @BindView(R.id.editText_contentParam)
    EditText editText_contentParam;

    @BindView(R.id.editText_limitNum)
    EditText editText_limitNum;

    @BindView(R.id.textView_startDate)
    TextView textView_startDate;

    @BindView(R.id.textView_endDate)
    TextView textView_endDate;

    @BindView(R.id.textView_startDateTime)
    TextView textView_startDateTime;

    @BindView(R.id.textView_endDateTime)
    TextView textView_endDateTime;

    @BindView(R.id.checkbox_endDate)
    CheckBox checkbox_endDate;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LogUtil.e(TAG, "onCreateDialog");
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        rootView = View.inflate(getContext(), R.layout.fragment_content_param, null);
        ButterKnife.bind(this, rootView);
        bottomSheetDialog.setContentView(rootView);
        bottomSheetBehavior = BottomSheetBehavior.from((View) rootView.getParent());
        ((View) rootView.getParent()).setBackgroundColor(Color.TRANSPARENT);
        int height = Util.getScreenHeight(getContext());
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) frame_contentParam.getLayoutParams();
        layoutParams.height = height;
        frame_contentParam.setLayoutParams(layoutParams);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                /*
                 * PeekHeight默认高度256dp 会在该高度上悬浮
                 * 设置等于view的高 就不会卡住
                 */
                bottomSheetBehavior.setPeekHeight(rootView.getHeight());
            }
        });
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset <= -.16) {
                    if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_DRAGGING
                            || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_SETTLING) {
                        dismiss();
                    }
                }
            }
        });
        textView_addComplete.setOnClickListener(this);
        checkbox_endDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textView_endDate.setBackgroundResource(R.drawable.shape_round_corner_grayf2_edit);
                    textView_endDate.setClickable(true);
                } else {
                    textView_endDate.setBackgroundResource(R.drawable.shape_round_corner_gray_light_padding_4_solid);
                    textView_endDate.setText(null);
                    textView_endDate.setTag(null);
                    textView_endDate.setClickable(false);
                }
            }
        });


        textView_startDate.setOnClickListener(this);
        textView_endDate.setOnClickListener(this);
        textView_startDateTime.setOnClickListener(this);
        textView_endDateTime.setOnClickListener(this);
        textView_endDate.setClickable(false);
        if (contentParams != null) {
//            if (position > -1) {//修改keyword
            editText_contentParam.setText(contentParams.getContent());
            editText_limitNum.setText(String.valueOf(contentParams.getNumber()));
            checkbox_endDate.setChecked(contentParams.isLongTerm());
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.setTimeInMillis(contentParams.getStartDateTime());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            textView_startDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            textView_startDateTime.setText(hourOfDay + ":" + minute);
            long time = hourOfDay * 60 * 60 * 1000 + minute * 60 * 1000;
            textView_startDateTime.setTag(time);
            textView_startDate.setTag(contentParams.getStartDateTime() - time);

            Calendar calendarEnd = Calendar.getInstance(Locale.CHINA);
            calendarEnd.setTimeInMillis(contentParams.getEndDateTime());
            int yearEnd = calendarEnd.get(Calendar.YEAR);
            int monthEnd = calendarEnd.get(Calendar.MONTH);
            int dayOfMonthEnd = calendarEnd.get(Calendar.DAY_OF_MONTH);
            int hourOfDayEnd = calendarEnd.get(Calendar.HOUR_OF_DAY);
            int minuteEnd = calendarEnd.get(Calendar.MINUTE);
            long timeEnd = hourOfDayEnd * 60 * 60 * 1000 + minuteEnd * 60 * 1000;

            if (contentParams.isLongTerm()) {
                textView_endDate.setText(yearEnd + "-" + (monthEnd + 1) + "-" + dayOfMonthEnd);
                textView_endDate.setTag(contentParams.getEndDateTime() - timeEnd);
            }
            StringBuilder stringBuilder = new StringBuilder();
            if (hourOfDayEnd < 10) {
                stringBuilder.append("0");
            }
            stringBuilder.append(hourOfDayEnd).append(":");
            if (minuteEnd < 10) {
                stringBuilder.append("0");
            }
            stringBuilder.append(minuteEnd);
            textView_endDateTime.setText(stringBuilder.toString());
            textView_endDateTime.setTag(timeEnd);
        }
        return bottomSheetDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.e(TAG, "onStart");
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.e(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e(TAG, "onDestroy");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.textView_addComplete:
                if (TextUtils.isEmpty(editText_contentParam.getText().toString().trim())) {
                    ToastUtil.showToast(getActivity(), getResources().getString(R.string.inputContentParam), Toast.LENGTH_LONG);
                    return;
                }
                if (TextUtils.isEmpty(editText_limitNum.getText().toString().trim())) {
                    ToastUtil.showToast(getActivity(), getResources().getString(R.string.inputSpreadNum), Toast.LENGTH_LONG);
                    return;
                }
                if (Integer.parseInt(editText_limitNum.getText().toString().trim()) <= 0) {
                    ToastUtil.showToast(getActivity(), "推广次数必须大于0", Toast.LENGTH_LONG);
                    return;
                }
                if (TextUtils.isEmpty(textView_startDate.getText().toString().trim())) {
                    ToastUtil.showToast(getActivity(), getResources().getString(R.string.inputStartDate), Toast.LENGTH_LONG);
                    return;
                }
                if (checkbox_endDate.isChecked() && TextUtils.isEmpty(textView_endDate.getText().toString().trim())) {
                    ToastUtil.showToast(getActivity(), getResources().getString(R.string.inputEndDate), Toast.LENGTH_LONG);
                    return;
                }
                if (TextUtils.isEmpty(textView_startDateTime.getText().toString().trim())) {
                    ToastUtil.showToast(getActivity(), getResources().getString(R.string.inputStartDateTime), Toast.LENGTH_LONG);
                    return;
                }
                if (TextUtils.isEmpty(textView_endDateTime.getText().toString().trim())) {
                    ToastUtil.showToast(getActivity(), getResources().getString(R.string.inputEndDateTime), Toast.LENGTH_LONG);
                    return;
                }
                if ((long) textView_startDateTime.getTag() >= (long) textView_endDateTime.getTag()) {
                    ToastUtil.showToast(getActivity(), getResources().getString(R.string.inputCorrectDateTime), Toast.LENGTH_LONG);
                    return;
                }

                if ((long) textView_endDateTime.getTag() + (long) textView_startDate.getTag() < System.currentTimeMillis()) {
                    ToastUtil.showToast(getActivity(), getResources().getString(R.string.inputCorrectDateTimeNow), Toast.LENGTH_LONG);
                    return;
                }


                if (contentListener != null) {
                    ContentParam contentParams = new ContentParam();
                    contentParams.setContent(editText_contentParam.getText().toString().trim());
                    contentParams.setNumber(Integer.valueOf(editText_limitNum.getText().toString().trim()));
                    contentParams.setStartDateTime((long) textView_startDate.getTag() + (long) textView_startDateTime.getTag());
                    if (checkbox_endDate.isChecked()) {
                        contentParams.setEndDateTime((long) textView_endDate.getTag() + (long) textView_endDateTime.getTag());
                    } else {
                        contentParams.setEndDateTime((long) textView_startDate.getTag() + (long) textView_endDateTime.getTag());
                    }
                    contentParams.setLongTerm(checkbox_endDate.isChecked());
                    contentListener.onContentSetup(contentParams, position);
                    dismiss();
                }
                break;
            case R.id.textView_startDate:
                showDatePickerDialog(getContext(), textView_startDate);
                Util.hideSoftInput(getActivity(), editText_contentParam);
                break;
            case R.id.textView_endDate:
                showDatePickerDialog(getContext(), textView_endDate);
                Util.hideSoftInput(getActivity(), editText_contentParam);
                break;
            case R.id.textView_startDateTime:
                showTimePickerDialog(getContext(), textView_startDateTime);
                Util.hideSoftInput(getActivity(), editText_contentParam);
                break;
            case R.id.textView_endDateTime:
                showTimePickerDialog(getContext(), textView_endDateTime);
                Util.hideSoftInput(getActivity(), editText_contentParam);
                break;
        }


    }


    private void showDatePickerDialog(Context context, final TextView textView) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                textView.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                Calendar calendar1 = Calendar.getInstance(Locale.CHINA);
                calendar1.set(year, month, dayOfMonth, 0, 0, 0);
                long time = calendar1.getTimeInMillis() / 1000 * 1000;
                textView.setTag(time);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog(Context context, final TextView textView) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int hourOfDay = calendar.get((Calendar.HOUR_OF_DAY));
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                textView.setText(hourOfDay + ":" + minute);
                long time = hourOfDay * 60 * 60 * 1000 + minute * 60 * 1000;
                textView.setTag(time);
            }
        }, hourOfDay, minute, true);
        timePickerDialog.show();
    }

    @Override
    public void onUpdateContent(ContentParam contentParams, int position) {
        this.position = position;
        this.contentParams = contentParams;
    }


    public interface ContentListener {
        void onContentSetup(ContentParam contentParams, int position);
    }

}
