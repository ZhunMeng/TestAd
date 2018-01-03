package com.duodian.admore.play.create;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AlertDialog;
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
import com.duodian.admore.plan.create.AppInfo;
import com.duodian.admore.utils.GlideRoundTransform;
import com.duodian.admore.utils.LogUtil;
import com.duodian.admore.utils.ToastUtil;
import com.duodian.admore.utils.Util;
import com.google.gson.Gson;

import java.io.File;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PlayPlanCreateActivity extends BaseActivity implements View.OnClickListener, ContentDialogFragment.ContentListener {

    private static final String TAG = "PlayPlanCreateActivity";
    public static final int CODE_REQUEST_PERMISSION_STORAGE = 1;
    public static final int CODE_REQUEST_IMAGE = 2;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;

    @BindView(R.id.button_planCreate)
    Button button_planCreate;


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

    @BindView(R.id.frame_banner)
    FrameLayout frame_banner;

    @BindView(R.id.textView_banner_hint)
    TextView textView_banner_hint;

    @BindView(R.id.linear_banner)
    LinearLayout linear_banner;

    @BindView(R.id.imageView_banner)
    ImageView imageView_banner;

    @BindView(R.id.textView_banner_re_upload)
    TextView textView_banner_re_upload;

    @BindView(R.id.textView_appPriceSelected)
    TextView textView_appPriceSelected;

    //    @BindView(R.id.recyclerView_dialog_app)
    RecyclerView recyclerView_dialog_app;

    @BindView(R.id.recyclerView_keywords)
    RecyclerView recyclerView_keywords;


    private AppListAdapter appListAdapter;
    private List<AppInfo> appInfoList;
    private AppInfo selectedAppInfo;
    private BannerInfo bannerInfo;

    private BottomSheetDialog dialog_apps;

    private List<ContentParam> contentParamsList;

    private ContentParamAdapter contentParamAdapter;
    private ContentDialogFragment contentDialogFragment;
    private OnPlayPlanCreateActivityActionListener onPlayPlanCreateActivityActionListener;
    private int playNum;
    private boolean enoughNum;
    private AlertDialog.Builder dialogBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_plan_create);
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
                    case R.id.menu_addContentParam:
                        initContentDialogFragment();
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
        textView_title.setText(getResources().getString(R.string.playPlanCreate));

        button_planCreate.setOnClickListener(this);
        frame_selectedApp.setOnClickListener(this);
        frame_banner.setOnClickListener(this);

        contentParamsList = new ArrayList<>();
        recyclerView_keywords.setLayoutManager(new LinearLayoutManager(this));
        contentParamAdapter = new ContentParamAdapter(contentParamsList);
        recyclerView_keywords.setAdapter(contentParamAdapter);
        getResourceNum();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_play_plan_create, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_planCreate:
                checkParameter();
                break;
            case R.id.frame_selectedApp:
                initDialogApps();
                getApps();
                break;
            case R.id.frame_banner:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_REQUEST_PERMISSION_STORAGE);
                } else {
                    pickImage();
                }
                break;
        }

    }

    /**
     * 相册选取banner
     */
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, CODE_REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_REQUEST_IMAGE:
                if (resultCode == RESULT_OK) {
                    String path = Util.getRealPathFromURI(getApplicationContext(), data.getData());
                    if (Util.getBitmapWidth(path) != 640 || Util.getBitmapHeight(path) != 335) {
                        ToastUtil.showToast(getApplicationContext(), "图片不符合640*335", Toast.LENGTH_LONG);
                        return;
                    }
                    if (selectedAppInfo == null) {
                        ToastUtil.showToast(getApplicationContext(), "请选择推广目标", Toast.LENGTH_LONG);
                        return;
                    }
                    File file = new File(path);
                    RequestBody requestBody =
                            new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("identifier", Global.userInfo.getIdentifier())
                                    .addFormDataPart("userAppId", selectedAppInfo.getUserAppId())
                                    .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file)).build();
                    uploadBanner(requestBody);

                    Glide.with(this).load(data.getData()).into(imageView_banner);
                    textView_banner_hint.setVisibility(View.GONE);
                    linear_banner.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    private void initContentDialogFragment() {
        if (contentDialogFragment == null) {// TODO: 2017/11/14 是否可以不加判断
            contentDialogFragment = new ContentDialogFragment();
            onPlayPlanCreateActivityActionListener = contentDialogFragment;
        }
        contentDialogFragment.show(getSupportFragmentManager(), "");
        if (onPlayPlanCreateActivityActionListener != null && contentParamsList.size() > 0) {
            onPlayPlanCreateActivityActionListener.onUpdateContent(contentParamsList.get(contentParamsList.size() - 1), -1);
        }

    }

    private void initDialogApps() {
        if (dialog_apps == null) {
            dialog_apps = new BottomSheetDialog(this);
            View view = getLayoutInflater().inflate(R.layout.dialog_apps_spread_create, null);
            recyclerView_dialog_app = view.findViewById(R.id.recyclerView_dialog_app);
            dialog_apps.setContentView(view);

            appInfoList = new ArrayList<>();
            recyclerView_dialog_app.setLayoutManager(new LinearLayoutManager(this));
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
        Observable<HttpResult<List<AppInfo>>> observable = iServiceApi.playMyApps(params, System.currentTimeMillis());
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

    /**
     * upload Banner
     */
    private void uploadBanner(RequestBody requestBody) {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        Observable<HttpResult<BannerInfo>> observable = iServiceApi.uploadBanner(requestBody);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<BannerInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull HttpResult<BannerInfo> listHttpResult) {
                        if (listHttpResult != null) {
                            if ("200".equalsIgnoreCase(listHttpResult.getCode()) && listHttpResult.isSuccess()) {
                                if (listHttpResult.getResult() != null) {
                                    bannerInfo = listHttpResult.getResult();
                                }
                            }
                        }
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

        if (bannerInfo == null) {
            ToastUtil.showToast(getApplicationContext(), "请选择Banner图片", Toast.LENGTH_LONG);
            return;
        }

        if (contentParamsList == null || contentParamsList.size() <= 0) {
            ToastUtil.showToast(getApplicationContext(), "请添加广告语", Toast.LENGTH_LONG);
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
        int num = 0;
        for (int i = 0; i < contentParamsList.size(); i++) {
            num += contentParamsList.get(i).getNumber();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("应用共投放 ").append(num).append(" 次/Play\n")
                .append("剩余").append("Play ").append(playNum).append(" 次/Play");
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
        params.put("userAppId", selectedAppInfo.getUserAppId());
        params.put("bannerId", bannerInfo.getBannerId());
        params.put("contentParams", new Gson().toJson(contentParamsList));
        Observable<HttpResult> observable = iServiceApi.playSavePlan(params, System.currentTimeMillis());
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
                        if (httpResult.isSuccess()) {
                            ToastUtil.showToast(getApplicationContext(), "创建计划成功", Toast.LENGTH_LONG);
                            PlayPlanCreateActivity.this.finish();
                        } else if (httpResult.getMessage() != null) {
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
                    }
                });
    }


    private void getResourceNum() {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(this);
        params.put("identifier", Global.userInfo.getIdentifier());
        Observable<HttpResult<PlayResourceData>> observable = iServiceApi.playLoadResourceData(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<PlayResourceData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<PlayResourceData> playResourceDataHttpResult) {
                        if (playResourceDataHttpResult != null) {
                            playNum = playResourceDataHttpResult.getResult().getPlayNum();
                            LogUtil.e(TAG, "playNum:" + playNum);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


    @Override
    public void onContentSetup(ContentParam contentParams, int position) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTimeInMillis(contentParams.getStartDateTime());
        int yearStart = calendarStart.get(Calendar.YEAR);
        int monthStart = calendarStart.get(Calendar.MONTH) + 1;
        int dayStart = calendarStart.get(Calendar.DAY_OF_MONTH);
        int hourStart = calendarStart.get(Calendar.HOUR_OF_DAY);
        int minuteStart = calendarStart.get(Calendar.MINUTE);

        if (contentParams.isLongTerm()) {
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTimeInMillis(contentParams.getEndDateTime());
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
            contentParams.setSpreadTime(stringBuilder.toString());

        } else {
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTimeInMillis(contentParams.getEndDateTime());
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
            contentParams.setSpreadTime(stringBuilder.toString());
        }
        if (position == -1) {//添加content
            contentParamsList.add(contentParams);
            contentParamAdapter.notifyItemInserted(contentParamsList.size() - 1);
        } else {//修改content
            contentParamsList.get(position).setSpreadTime(contentParams.getSpreadTime());
            contentParamsList.get(position).setLongTerm(contentParams.isLongTerm());
            contentParamsList.get(position).setEndDateTime(contentParams.getEndDateTime());
            contentParamsList.get(position).setStartDateTime(contentParams.getStartDateTime());
            contentParamsList.get(position).setContent(contentParams.getContent());
            contentParamsList.get(position).setNumber(contentParams.getNumber());
            contentParamAdapter.notifyItemChanged(position);
        }
        int num = 0;
        for (int i = 0; i < contentParamsList.size(); i++) {
            num += contentParamsList.get(i).getNumber();
        }
        if (contentParams != null && contentParams.getNumber() < num) {
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


    class ContentParamAdapter extends RecyclerView.Adapter<ContentParamAdapter.ContentParamViewHolder> {
        private List<ContentParam> contentParamList;

        ContentParamAdapter(List<ContentParam> contentParamList) {
            this.contentParamList = contentParamList;
        }

        @Override
        public ContentParamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_play_plan_create_content_param, parent, false);
            return new ContentParamViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ContentParamViewHolder holder, int position) {
            holder.textView_content.setText(contentParamList.get(position).getContent());
            holder.textView_longTerm.setVisibility(contentParamList.get(position).isLongTerm() ? View.VISIBLE : View.INVISIBLE);
            holder.textView_spreadNum.setText("投放量: " + contentParamList.get(position).getNumber() + " /次");
            holder.textView_spreadTime.setText(contentParamList.get(position).getSpreadTime());
        }

        @Override
        public int getItemCount() {
            return contentParamList.size();
        }


        class ContentParamViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.textView_content)
            TextView textView_content;

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

            ContentParamViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                textView_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position >= 0 && contentParamList.size() - 1 >= position) {
                            contentParamList.remove(position);
                            notifyItemRemoved(position);
                        }
                    }
                });

                textView_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position >= 0 && contentParamList.size() - 1 >= position) {
                            if (onPlayPlanCreateActivityActionListener != null) {
                                contentDialogFragment.show(getSupportFragmentManager(), "");
                                onPlayPlanCreateActivityActionListener.onUpdateContent(contentParamList.get(position), position);
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
                        .from(PlayPlanCreateActivity.this).inflate(R.layout.itemview_app_list_footer, parent, false);
                return new AppListFooterViewHolder(footerView);
            }
            return new AppListViewHolder(LayoutInflater
                    .from(PlayPlanCreateActivity.this).inflate(R.layout.itemview_plan_create_apps, parent, false));
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions,
                                           @android.support.annotation.NonNull int[] grantResults) {
        switch (requestCode) {
            case CODE_REQUEST_PERMISSION_STORAGE:
                if (PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                    pickImage();
                } else {
                    ToastUtil.showToast(getApplicationContext(), "未获取到读取权限", Toast.LENGTH_LONG);
                }

                break;
        }

    }
}
