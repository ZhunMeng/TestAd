package com.duodian.admore.plan.create;

import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialog;
import android.os.Bundle;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;

import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.utils.GlideRoundTransform;
import com.duodian.admore.utils.LogUtil;
import com.duodian.admore.utils.ToastUtil;
import com.duodian.admore.utils.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class SpreadPlanCreateActivity extends BaseActivity implements View.OnClickListener, KeywordDialogFragment.KeywordListener {

    private static final String TAG = "SpreadPlanCreateActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;

    @BindView(R.id.button_spreadCreate)
    Button button_spreadCreate;

    @BindView(R.id.frame_selectedApp)
    FrameLayout frame_selectedApp;

    @BindView(R.id.textView_hint)
    TextView textView_hint;

    @BindView(R.id.textView_spreadExplain)
    TextView textView_spreadExplain;

    @BindView(R.id.imageView_explain)
    ImageView imageView_explain;

    @BindView(R.id.linear_selected)
    LinearLayout linear_selected;

    @BindView(R.id.imageView_iconSelected)
    ImageView imageView_iconSelected;

    @BindView(R.id.textView_appNameSelected)
    TextView textView_appNameSelected;

    @BindView(R.id.textView_appPriceSelected)
    TextView textView_appPriceSelected;

    //    @BindView(R.id.recyclerView_dialog_app)
    RecyclerView recyclerView_dialog_app;

    @BindView(R.id.recyclerView_keywords)
    RecyclerView recyclerView_keywords;

    private AppListAdapter appListAdapter;
    private List<AppInfo> appInfoList;
    private AppInfo selectedAppInfo;

    private BottomSheetDialog dialog_apps;

    private List<KeywordParams> keywordParamsList;

    private KeywordAdapter keywordAdapter;
    private KeywordDialogFragment keywordDialogFragment;
    private OnPlanCreateActivityActionListener onPlanCreateActivityActionListener;

    private ResourceData resourceData;
    private boolean enoughNum;
    private AlertDialog.Builder dialogBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spread_plan_create);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.menu_addKeyword:
                        initKeywordDialogFragment();
                        break;
                }
                return true;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textView_title.setText(getResources().getString(R.string.spreadCreate));

        button_spreadCreate.setOnClickListener(this);
        frame_selectedApp.setOnClickListener(this);

        keywordParamsList = new ArrayList<>();
        recyclerView_keywords.setLayoutManager(new LinearLayoutManager(this));
        keywordAdapter = new KeywordAdapter(keywordParamsList);
        recyclerView_keywords.setAdapter(keywordAdapter);
        getResourceNum();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_plan_create, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_spreadCreate:
                checkParameter();
                break;
            case R.id.frame_selectedApp:
                initDialogApps();
                break;
        }

    }

    private void initKeywordDialogFragment() {
        if (keywordDialogFragment == null) {
            keywordDialogFragment = new KeywordDialogFragment();
            onPlanCreateActivityActionListener = keywordDialogFragment;
        }
        keywordDialogFragment.show(getSupportFragmentManager(), "");
        if (onPlanCreateActivityActionListener != null && keywordParamsList.size() > 0) {
            onPlanCreateActivityActionListener.onUpdateKeyword(keywordParamsList.get(keywordParamsList.size() - 1), -1);
        }

    }


    private void initDialogApps() {
        if (dialog_apps == null) {
            dialog_apps = new BottomSheetDialog(this);
            View view = getLayoutInflater().inflate(R.layout.dialog_apps_spread_create, null);
            recyclerView_dialog_app = view.findViewById(R.id.recyclerView_dialog_app);
            dialog_apps.setContentView(view);

            appInfoList = new ArrayList<>();
            recyclerView_dialog_app.setLayoutManager(new LinearLayoutManager(SpreadPlanCreateActivity.this));
            recyclerView_dialog_app.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            appListAdapter = new AppListAdapter(appInfoList);
            recyclerView_dialog_app.setAdapter(appListAdapter);
        }
        dialog_apps.show();
        getApps();
    }


    /**
     * 获取app列表
     */
    private void getApps() {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(this);
        params.put("identifier", Global.userInfo.getIdentifier());
        Observable<HttpResult<List<AppInfo>>> observable = iServiceApi.myApps(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<AppInfo>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull HttpResult<List<AppInfo>> listHttpResult) {
                        appInfoList.clear();
                        appInfoList.addAll(listHttpResult.getResult());
                        appListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e("SpreadPlanCreateActivity", e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void checkParameter() {
        if (selectedAppInfo == null) {
            ToastUtil.showToast(getApplicationContext(), "请选择推广目标", Toast.LENGTH_LONG);
            return;
        }


        if (keywordParamsList == null || keywordParamsList.size() <= 0) {
            ToastUtil.showToast(getApplicationContext(), "请添加关键词", Toast.LENGTH_LONG);
            return;
        }
//        if (!enoughNum) {
//            ToastUtil.showToast(getApplicationContext(), getResources().getString(R.string.spreadExplainNumNotEnough), Toast.LENGTH_LONG);
//            return;
//        }

        if (dialogBuilder == null) {
            dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle(getResources().getString(R.string.spreadHint));
        }
        float price = selectedAppInfo.getPrice();
        int num = 0;
        int balance = 0;
        for (int i = 0; i < keywordParamsList.size(); i++) {
            num += keywordParamsList.get(i).getNumber();
        }
        StringBuilder stringBuilder = new StringBuilder();
        String priceString;
        if (price > 0) {
            priceString = "付费";
            balance = resourceData.getPayBalanceNum();
        } else {
            priceString = "免费";
            balance = resourceData.getFreeBalanceNum();
        }
        stringBuilder.append(priceString).append("\n应用共投放 ").append(num).append(" 次\n")
                .append("剩余").append(priceString).append("资源包 ").append(balance).append(" 次");
        dialogBuilder.setMessage(stringBuilder.toString());
        dialogBuilder.setPositiveButton(R.string.makeSure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                savePlan();
            }
        }).setNegativeButton(R.string.backToChange, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }


    private void savePlan() {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(this);
        params.put("identifier", Global.userInfo.getIdentifier());
        if (selectedAppInfo == null) {
            ToastUtil.showToast(getApplicationContext(), "请选择推广目标", Toast.LENGTH_LONG);
            return;
        }
        params.put("userAppId", selectedAppInfo.getUserAppId());

        if (keywordParamsList == null || keywordParamsList.size() <= 0) {
            ToastUtil.showToast(getApplicationContext(), "请添加关键词", Toast.LENGTH_LONG);
            return;
        }
//        if (!enoughNum) {
//            ToastUtil.showToast(getApplicationContext(), getResources().getString(R.string.spreadExplainNumNotEnough), Toast.LENGTH_LONG);
//            return;
//        }
        params.put("keywordParams", new Gson().toJson(keywordParamsList));
        LogUtil.e(TAG, new Gson().toJson(keywordParamsList));
        Observable<HttpResult> observable = iServiceApi.savePlan(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        showLoadingDialog();
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(HttpResult httpResult) {
                        dismissLoadingDialog();
                        if (httpResult != null && httpResult.isSuccess() && "200".equalsIgnoreCase(httpResult.getCode())) {
                            ToastUtil.showToast(getApplicationContext(), "创建推广成功", Toast.LENGTH_LONG);
                        } else if (httpResult != null && httpResult.getMessage() != null) {
                            ToastUtil.showToast(getApplicationContext(), httpResult.getMessage(), Toast.LENGTH_LONG);
                        }
                        LogUtil.e(TAG, "onNext");
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        ToastUtil.showToast(getApplicationContext(), "未知错误,请重试", Toast.LENGTH_LONG);
                        LogUtil.e(TAG, "onError");
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "onComplete");
                        dismissLoadingDialog();
                    }
                });
    }


    private void getResourceNum() {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(this);
        params.put("identifier", Global.userInfo.getIdentifier());
        Observable<HttpResult<ResourceData>> observable = iServiceApi.loadResourceData(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<ResourceData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(HttpResult<ResourceData> resourceDataHttpResult) {
                        if (resourceDataHttpResult.getResult() != null) {
                            resourceData = resourceDataHttpResult.getResult();
                            LogUtil.e(TAG, resourceData.getPayBalanceNum() + "");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError");
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "onComplete");
                    }
                });


    }


    @Override
    public void onKeywordSetup(KeywordParams keywordParams, int position) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTimeInMillis(keywordParams.getStartDateTime());
        int yearStart = calendarStart.get(Calendar.YEAR);
        int monthStart = calendarStart.get(Calendar.MONTH) + 1;
        int dayStart = calendarStart.get(Calendar.DAY_OF_MONTH);
        int hourStart = calendarStart.get(Calendar.HOUR_OF_DAY);
        int minuteStart = calendarStart.get(Calendar.MINUTE);

        if (keywordParams.isLongTerm()) {
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTimeInMillis(keywordParams.getEndDateTime());
            int yearEnd = calendarEnd.get(Calendar.YEAR);
            int monthEnd = calendarEnd.get(Calendar.MONTH) + 1;
            int dayEnd = calendarEnd.get(Calendar.DAY_OF_MONTH);
            int hourEnd = calendarEnd.get(Calendar.HOUR_OF_DAY);
            int minuteEnd = calendarEnd.get(Calendar.MINUTE);

            StringBuilder stringBuilder = new StringBuilder("推广时间: ");
            stringBuilder.append(yearStart).append("-");
            if (monthStart < 10) {
                stringBuilder.append("0");
            }
            stringBuilder.append(monthStart).append("-");
            if (dayStart < 10) {
                stringBuilder.append("0");
            }
            stringBuilder.append(dayStart).append(" 至 ").append(yearEnd).append("-");
            if (monthEnd < 10) {
                stringBuilder.append("0");
            }
            stringBuilder.append(monthEnd).append("-");
            if (dayEnd < 10) {
                stringBuilder.append("0");
            }
            stringBuilder.append(dayEnd).append("  ");
            if (hourStart < 10) {
                stringBuilder.append("0");
            }
            stringBuilder.append(hourStart).append(":");
            if (minuteStart < 10) {
                stringBuilder.append("0");
            }
            stringBuilder.append(minuteStart).append(" -- ");
            if (hourEnd < 10) {
                stringBuilder.append("0");
            }
            stringBuilder.append(hourEnd).append(":");
            if (minuteEnd < 10) {
                stringBuilder.append("0");
            }
            stringBuilder.append(minuteEnd);
            keywordParams.setSpreadTime(stringBuilder.toString());

        } else {
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTimeInMillis(keywordParams.getEndDateTime());
            int hourEnd = calendarEnd.get(Calendar.HOUR_OF_DAY);
            int minuteEnd = calendarEnd.get(Calendar.MINUTE);

            StringBuilder stringBuilder = new StringBuilder("推广时间: ");
            stringBuilder.append(yearStart).append("-");
            if (monthStart < 10) {
                stringBuilder.append("0");
            }
            stringBuilder.append(monthStart).append("-");
            if (dayStart < 10) {
                stringBuilder.append("0");
            }
            stringBuilder.append(dayStart).append("  ");

            if (hourStart < 10) {
                stringBuilder.append("0");
            }
            stringBuilder.append(hourStart).append(":");
            if (minuteStart < 10) {
                stringBuilder.append("0");
            }
            stringBuilder.append(minuteStart).append(" -- ");
            if (hourEnd < 10) {
                stringBuilder.append("0");
            }
            stringBuilder.append(hourEnd).append(":");
            if (minuteEnd < 10) {
                stringBuilder.append("0");
            }
            stringBuilder.append(minuteEnd);
            keywordParams.setSpreadTime(stringBuilder.toString());
        }
        if (position == -1) {//添加keyword
            keywordParamsList.add(keywordParams);
            keywordAdapter.notifyItemInserted(keywordParamsList.size() - 1);
        } else {//修改keyword
            keywordParamsList.get(position).setSpreadTime(keywordParams.getSpreadTime());
            keywordParamsList.get(position).setLongTerm(keywordParams.isLongTerm());
            keywordParamsList.get(position).setEndDateTime(keywordParams.getEndDateTime());
            keywordParamsList.get(position).setStartDateTime(keywordParams.getStartDateTime());
            keywordParamsList.get(position).setKeyword(keywordParams.getKeyword());
            keywordParamsList.get(position).setNumber(keywordParams.getNumber());
            keywordAdapter.notifyItemChanged(position);
        }
        int num = 0;
        for (int i = 0; i < keywordParamsList.size(); i++) {
            num += keywordParamsList.get(i).getNumber();
        }
        if (resourceData != null && resourceData.getPayBalanceNum() < num) {
            enoughNum = false;
            textView_spreadExplain.setText(getResources().getString(R.string.spreadExplainNumNotEnough));
            textView_spreadExplain.setTextColor(getResources().getColor(R.color.red));
            ImageViewCompat.setImageTintList(imageView_explain, getResources().getColorStateList(R.color.red));
        } else {
            enoughNum = true;
            textView_spreadExplain.setTextColor(getResources().getColor(R.color.grayText));
            textView_spreadExplain.setText(getResources().getString(R.string.spreadExplain));
            ImageViewCompat.setImageTintList(imageView_explain, getResources().getColorStateList(R.color.grayText));
        }

    }


    class KeywordAdapter extends RecyclerView.Adapter<KeywordAdapter.KeywordViewHolder> {
        private List<KeywordParams> keywordParamsList;

        KeywordAdapter(List<KeywordParams> keywordParamsList) {
            this.keywordParamsList = keywordParamsList;
        }

        @Override
        public KeywordAdapter.KeywordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_plan_create_keyword, parent, false);
            return new KeywordViewHolder(view);
        }

        @Override
        public void onBindViewHolder(KeywordAdapter.KeywordViewHolder holder, int position) {
            holder.textView_keyword.setText(keywordParamsList.get(position).getKeyword());
            holder.textView_longTerm.setVisibility(keywordParamsList.get(position).isLongTerm() ? View.VISIBLE : View.INVISIBLE);
            holder.textView_spreadNum.setText("投放量: " + keywordParamsList.get(position).getNumber() + " /次");
            holder.textView_spreadTime.setText(keywordParamsList.get(position).getSpreadTime());
        }

        @Override
        public int getItemCount() {
            return keywordParamsList.size();
        }


        class KeywordViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.textView_keyword)
            TextView textView_keyword;

            @BindView(R.id.textView_longTerm)
            TextView textView_longTerm;

            @BindView(R.id.textView_update)
            TextView textView_update;

            @BindView(R.id.textView_delete)
            TextView textView_delete;

            @BindView(R.id.textView_spreadNum)
            TextView textView_spreadNum;

            @BindView(R.id.textView_spreadTime)
            TextView textView_spreadTime;

            KeywordViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                textView_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position >= 0 && keywordParamsList.size() - 1 >= position) {
                            keywordParamsList.remove(position);
                            notifyItemRemoved(position);
                        }
                    }
                });

                textView_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position >= 0 && keywordParamsList.size() - 1 >= position) {
                            if (onPlanCreateActivityActionListener != null) {
                                keywordDialogFragment.show(getSupportFragmentManager(), "");
                                onPlanCreateActivityActionListener.onUpdateKeyword(keywordParamsList.get(position), position);
                            }
                        }
                    }
                });
            }
        }
    }


    class AppListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_ITEM = 0;
        private static final int TYPE_FOOTER = 1;

        private List<AppInfo> appInfos;

        AppListAdapter(List<AppInfo> appInfos) {
            this.appInfos = appInfos;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_FOOTER) {
                View footerView = LayoutInflater
                        .from(SpreadPlanCreateActivity.this).inflate(R.layout.itemview_app_list_footer, parent, false);
                return new AppListFooterViewHolder(footerView);
            }
            return new AppListViewHolder(LayoutInflater
                    .from(SpreadPlanCreateActivity.this).inflate(R.layout.itemview_plan_create_apps, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof AppListViewHolder) {
                AppListViewHolder appListViewHolder = (AppListViewHolder) holder;
                Glide.with(getApplicationContext())
                        .load(appInfos.get(position).getSmallIcon())
                        .apply(new RequestOptions().transforms(new GlideRoundTransform(getApplicationContext(), 8)))
                        .transition(withCrossFade())
                        .into(appListViewHolder.imageView_icon);
                appListViewHolder.appName.setText(appInfos.get(position).getTrackName());
                if (appInfos.get(position).getPrice() > 0) {
                    appListViewHolder.appPrice.setText(String.valueOf(appInfos.get(position).getPrice()));
                } else {
                    appListViewHolder.appPrice.setText(getResources().getString(R.string.free));
                }
                if (position == 2) {
                    appListViewHolder.appPrice.setText("￥1.0");
                }
            }
        }


        @Override
        public int getItemCount() {
            return appInfos.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == appInfos.size()) {
                return TYPE_FOOTER;
            }
            return TYPE_ITEM;
        }

        class AppListViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.imageView_icon)
            ImageView imageView_icon;

            @BindView(R.id.appName)
            TextView appName;

            @BindView(R.id.appPrice)
            TextView appPrice;

            @BindView(R.id.linear_itemView)
            LinearLayout linear_itemView;


            AppListViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                linear_itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (appInfos.size() > position) {
                            setSelectedAppInfo(appInfos.get(position));
                        }
                    }
                });
            }
        }

        class AppListFooterViewHolder extends RecyclerView.ViewHolder {

            AppListFooterViewHolder(View footerView) {
                super(footerView);
                ButterKnife.bind(this, footerView);
            }
        }

    }


    /**
     * 显示选择的app
     *
     * @param appInfo
     */
    private void setSelectedAppInfo(AppInfo appInfo) {
        try {
            selectedAppInfo = appInfo.clone();  // TODO: 2017/11/13 clone  failure
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        dialog_apps.hide();
        textView_hint.setVisibility(View.GONE);
        linear_selected.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(appInfo.getSmallIcon())
                .apply(new RequestOptions().transforms(new GlideRoundTransform(getApplicationContext(), 8)))
                .transition(withCrossFade())
                .into(imageView_iconSelected);
        textView_appNameSelected.setText(appInfo.getTrackName());
        if (appInfo.getPrice() > 0) {
            textView_appPriceSelected.setText(String.valueOf(appInfo.getPrice()));
        } else {
            textView_appPriceSelected.setText(getResources().getString(R.string.free));
        }

    }

}
