package com.kkkkkn.readbooks.view.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.model.adapter.BookShelfAdapter;
import com.kkkkkn.readbooks.model.adapter.SearchBookResultAdapter;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.model.entity.AnimationConfig;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.model.entity.BookShelfItem;
import com.kkkkkn.readbooks.presenter.Presenter_Main;
import com.kkkkkn.readbooks.util.StringUtil;
import com.kkkkkn.readbooks.view.customView.CustomSearchView;
import com.kkkkkn.readbooks.view.customView.SoftKeyBoardListener;
import com.kkkkkn.readbooks.view.customView.UpdateDialog;
import com.kkkkkn.readbooks.view.view.MainActivityView;


import java.io.File;
import java.util.ArrayList;



/**
 * 程序主界面，每次进入的时候获取读取本地图书并进行加载
 */
public class MainActivity extends BaseActivity implements MainActivityView {
    private final static String TAG="主界面";
    private long lastBackClick;
    private ArrayList<BookInfo> arrayList=new ArrayList<BookInfo>();
    private Presenter_Main presenter_main;
    private BookShelfAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private UpdateDialog updateDialog;
    private SoftKeyBoardListener softKeyBoardListener;
    private CustomSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setSoftKeyBoardListener();

        presenter_main=new Presenter_Main(getApplicationContext(),this);
        presenter_main.init();

        //申请权限
        //checkPermission();

        AccountInfo info=presenter_main.getToken();
        if(info.getAccount_token().isEmpty()||info.getAccount_id()==0){
            toLoginActivity();
        }else{
            presenter_main.getBookShelfList();
            presenter_main.checkUpdate();
        }

    }


    private void initView(){
        searchView=findViewById(R.id.searchView);
        searchView.setEnable(false);
        /*searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: 11111111111");
                toSearchActivity(view);
            }
        });*/

        GridView mGridView;
        mGridView=findViewById(R.id.main_booksGridView);
        swipeRefreshLayout=findViewById(R.id.main_SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter_main.getBookShelfList();
            }
        });
        if(mGridView.getAdapter()==null){
            mAdapter = new BookShelfAdapter(arrayList,getApplicationContext());
        }

        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BookInfo bookInfo=(BookInfo) adapterView.getAdapter().getItem(i);
                if(bookInfo!=null){
                    jump2ReadView(view,bookInfo);
                }
            }
        });
    }

    private void setSoftKeyBoardListener() {
        softKeyBoardListener = new SoftKeyBoardListener(MainActivity.this);
        //软键盘状态监听
        softKeyBoardListener.setListener(new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                //软键盘已经显示，做逻辑
                searchView.onKeyBoardState(true);
            }

            @Override
            public void keyBoardHide(int height) {
                //软键盘已经隐藏,做逻辑
                searchView.onKeyBoardState(false);
            }
        });
    }

    public void toSearchActivity(View view) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    //监听返回键，连续按2次直接退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            long nowBackClick=System.currentTimeMillis();
            if(lastBackClick!=0&&(nowBackClick-lastBackClick)<1500){
                //程序退出
                this.exitAll();
            }else{
                //500ms以上，弹窗不处理
                lastBackClick=nowBackClick;
                Toast.makeText(this,"请再按一次以退出程序",Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void jump2ReadView(View view,BookInfo bookInfo){
        AnimationConfig animationConfig=getScreenSize(view);
        if(animationConfig.isEmpty()){
            return;
        }
        //创建动画容器 true 为补间动画
        AnimationSet animationSet=new AnimationSet(true);
        //创建平移动画
        TranslateAnimation translateAnimation = new TranslateAnimation(0,animationConfig.moveX,0,animationConfig.moveY);
        //创建缩放动画
        ScaleAnimation scaleAnimation=new ScaleAnimation(1.0f,animationConfig.scaleX,1.0f,animationConfig.scaleY,animationConfig.view.getWidth()/2f,animationConfig.view.getHeight()/2f);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setDuration(1000);
        //播放完不恢复位置
        animationSet.setFillAfter(true);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.i(TAG, "onAnimationStart: ");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画显示完成后 ，跳转到浏览界面

                Bundle bundle=new Bundle();
                bundle.putSerializable("bookInfo",bookInfo);
                Intent intent=new Intent(getApplicationContext(),BookBrowsingActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.i(TAG, "onAnimationRepeat: ");
            }
        });

        //播放动画
        view.startAnimation(animationSet);
    }


    private AnimationConfig getScreenSize(View view){
        AnimationConfig animationConfig=new AnimationConfig();
        //获取屏幕大小
        WindowManager windowManager=getWindowManager();
        Display display=windowManager.getDefaultDisplay();
        Point screenPoint=new Point();
        display.getSize(screenPoint);
        int view_width=view.getWidth();
        int view_height=view.getHeight();
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int move_x = (screenPoint.x-view_width)/2-location[0]; // view距离 屏幕左边的距离（即x轴方向）
        int move_y = (screenPoint.y-view_height)/2-location[1]; // view距离 屏幕顶边的距离（即y轴方向）
        //缩放比例
        float width_scale=(float) screenPoint.x/view_width;
        float height_scale=(float) screenPoint.y/view_height;

        animationConfig.view=view;
        animationConfig.moveX=move_x/width_scale;
        animationConfig.moveY=move_y/height_scale;
        animationConfig.scaleX=width_scale;
        animationConfig.scaleY=height_scale;
        return animationConfig;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(presenter_main!=null){
            presenter_main.getBookShelfList();
        }
    }

    @Override
    public void syncBookShelf(final ArrayList<BookInfo> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (arrayList != null) {
                    arrayList.clear();
                    arrayList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void showUpdateDialog(final String msg,final String url,final String version) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!StringUtil.isEmpty(version)&&!StringUtil.isEmpty(msg)&&!StringUtil.isEmpty(url)){

                    updateDialog = new UpdateDialog(MainActivity.this);
                    updateDialog.setInfo("检测到新版本",msg)
                            .setOnClickListener(new UpdateDialog.OnClickBottomListener() {
                                @Override
                                public void onOkClick() {
                                    updateDialog.setProgressType(true);
                                    final String name = version + ".apk";
                                    final String path = Environment.getExternalStorageDirectory().getPath() + "/apks/";
                                    presenter_main.updateAPK(name, path, url);
                                }

                                @Override
                                public void onCancelClick() {
                                    updateDialog.dismiss();
                                }
                            });
                    updateDialog.show();

                }
            }
        });

    }



    @Override
    public void toLoginActivity() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    @Override
    public void updateProgress(final int progress) {
        //更新进度
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateDialog.setProgress(progress);
                if(progress==100){
                    updateDialog.dismiss();
                    updateDialog=null;
                }
            }
        });
    }

    @Override
    public void installAPK(String filePath) {
        Log.i(TAG, "installAPK: "+filePath);
        //apk安装跳转
        File apk=new File(filePath);
        if (!apk.exists()) {
            Log.i(TAG, "installApk: apk不存在");
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (apk.getName().endsWith(".apk")) {
            try {
                //兼容7.0
                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    boolean hasInstallPermission = getPackageManager().canRequestPackageInstalls();
                    if(!hasInstallPermission){
                        startInstallPermissionSettingActivity();
                    }
                    uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", apk);//通过FileProvider创建一个content类型的Uri
                    intent.setDataAndType(uri, "application/vnd.android.package-archive"); // 对应apk类型

                } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){// 适配Android 7系统版本
                    uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", apk);//通过FileProvider创建一个content类型的Uri
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                    intent.setDataAndType(uri, "application/vnd.android.package-archive"); // 对应apk类型

                }else{
                    intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            //弹出安装界面
            startActivity(intent);
        } else {
            Log.i(TAG, "installApk: 不是apk文件!");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        arrayList.clear();
        presenter_main.release();
    }

    @Override
    public void showMsgDialog(int type, String msg) {

    }
}
