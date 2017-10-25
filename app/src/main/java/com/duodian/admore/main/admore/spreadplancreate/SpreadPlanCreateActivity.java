package com.duodian.admore.main.admore.spreadplancreate;

import android.support.design.widget.BottomSheetDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.login.UserInfo;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

public class SpreadPlanCreateActivity extends BaseActivity implements View.OnClickListener, KeywordDialogFragment.KeywordListener {

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

    private AppListAdapter adapter;

    private BottomSheetDialog dialog_apps;

    private List<KeywordParams> keywordParamsList;

    private KeywordAdapter keywordAdapter;
    private KeywordDialogFragment keywordDialogFragment;
    private OnSpreadActivityActionListener onSpreadActivityActionListener;


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
                        initDialogKeyword();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_spread_create, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_spreadCreate:

                break;
            case R.id.frame_selectedApp:
                initDialogApps();
                getApps();
                break;
        }

    }

    private void initDialogKeyword() {
        if (keywordDialogFragment == null) {
            keywordDialogFragment = new KeywordDialogFragment();
            onSpreadActivityActionListener = keywordDialogFragment;
        }
        keywordDialogFragment.show(getSupportFragmentManager(), "");
        if (onSpreadActivityActionListener != null && keywordParamsList.size() > 0) {
            onSpreadActivityActionListener.onUpdateKeyword(keywordParamsList.get(keywordParamsList.size() - 1), -1);
        }

    }


    private void initDialogApps() {
        dialog_apps = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_apps_spread_create, null);
//        ButterKnife.bind(this, view);
        recyclerView_dialog_app = view.findViewById(R.id.recyclerView_dialog_app);
        dialog_apps.setContentView(view);
        dialog_apps.show();

        List<AppInfo> appInfos = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            AppInfo appInfo = new AppInfo();
            appInfo.setTrackName("MIX滤镜大师 - 创意无限的图像编辑与海报定制");
            appInfo.setPrice(0);
            appInfo.setSmallIcon("http://is3.mzstatic.com/image/thumb/Purple128/v4/43/56/ea/4356eab8-0b17-909e-180b-d67c40cad4da/source/60x60bb.jpg");
            appInfos.add(appInfo);
        }
        recyclerView_dialog_app.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AppListAdapter(appInfos);
        recyclerView_dialog_app.setAdapter(adapter);
    }

    private void getApps() {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(this);
        String identifier = null;
        Object object = Util.readObjectFromSharedPreference(getApplicationContext(), Global.USERINFO);
        if (object != null) {
            try {
                UserInfo userInfo = (UserInfo) object;
                if (!TextUtils.isEmpty(userInfo.getIdentifier())) {
                    identifier = userInfo.getIdentifier();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        params.put("identifier", identifier);
        Observable<HttpResult<List<AppInfo>>> observable = iServiceApi.myApps(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<AppInfo>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull HttpResult<List<AppInfo>> listHttpResult) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("SpreadPlanCreateActivity", e.toString());
                    }

                    @Override
                    public void onComplete() {

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
            int hourEnd = (int) (keywordParams.getEndDateTime() / 1000 / 60 / 60);
            int minuteEnd = (int) (keywordParams.getEndDateTime() / 1000 / 60 % 60);

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
    }


    class KeywordAdapter extends RecyclerView.Adapter<KeywordAdapter.KeywordViewHolder> {
        private List<KeywordParams> keywordParamsList;

        KeywordAdapter(List<KeywordParams> keywordParamsList) {
            this.keywordParamsList = keywordParamsList;
        }

        @Override
        public KeywordAdapter.KeywordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_spread_create_keyword, parent, false);
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
                            if (onSpreadActivityActionListener != null) {
                                keywordDialogFragment.show(getSupportFragmentManager(), "");
                                onSpreadActivityActionListener.onUpdateKeyword(keywordParamsList.get(position), position);
                            }
                        }
                    }
                });
            }
        }
    }


    class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppListViewHolder> {
        private List<AppInfo> appInfos;

        AppListAdapter(List<AppInfo> appInfos) {
            this.appInfos = appInfos;
        }

        @Override
        public AppListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AppListViewHolder(LayoutInflater.from(SpreadPlanCreateActivity.this).inflate(R.layout.itemview_spread_create_apps, parent, false));
        }

        @Override
        public void onBindViewHolder(AppListViewHolder holder, int position) {
            Glide.with(getApplicationContext())
                    .load(appInfos.get(position).getSmallIcon())
                    .apply(new RequestOptions().transforms(new RoundedCorners(24)))
                    .into(holder.imageView_icon);
            holder.appName.setText(appInfos.get(position).getTrackName());
            if (appInfos.get(position).getPrice() > 0) {
                holder.appPrice.setText(String.valueOf(appInfos.get(position).getPrice()));
            } else {
                holder.appPrice.setText(getResources().getString(R.string.free));
            }
            if (position == 2) {
                holder.appPrice.setText("￥1.0");
            }
        }


        @Override
        public int getItemCount() {
            return appInfos.size();
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

    }


    /**
     * 显示选择的app
     *
     * @param appInfo
     */
    private void setSelectedAppInfo(AppInfo appInfo) {
        dialog_apps.hide();
        textView_hint.setVisibility(View.GONE);
        linear_selected.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(appInfo.getSmallIcon())
                .apply(new RequestOptions().transform(new RoundedCorners(24)))
                .into(imageView_iconSelected);
        textView_appNameSelected.setText(appInfo.getTrackName());
        if (appInfo.getPrice() > 0) {
            textView_appPriceSelected.setText(String.valueOf(appInfo.getPrice()));
        } else {
            textView_appPriceSelected.setText(getResources().getString(R.string.free));
        }

    }

}
