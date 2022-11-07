package com.kkkkkn.readbooks.view.activities

import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.drawerlayout.widget.DrawerLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kkkkkn.readbooks.R
import com.kkkkkn.readbooks.databinding.ActivityMainBinding
import com.kkkkkn.readbooks.model.adapter.BookShelfAdapter
import com.kkkkkn.readbooks.model.entity.AnimationConfig
import com.kkkkkn.readbooks.model.entity.BookInfo
import com.kkkkkn.readbooks.presenter.Presenter_Main
import com.kkkkkn.readbooks.util.StringUtil
import com.kkkkkn.readbooks.view.customView.UpdateDialog
import com.kkkkkn.readbooks.view.customView.UpdateDialog.OnClickBottomListener
import com.kkkkkn.readbooks.view.view.MainActivityView
import java.io.File

/**
 * 程序主界面，每次进入的时候获取读取本地图书并进行加载
 */
class MainActivity : BaseActivity<ActivityMainBinding>(), MainActivityView {
    private var lastBackClick: Long = 0
    private val arrayList: ArrayList<BookInfo> = ArrayList()
    private var presenter_main: Presenter_Main? = null
    private var mAdapter: BookShelfAdapter? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var updateDialog: UpdateDialog? = null
    private var loginActivityResultLauncher: ActivityResultLauncher<Intent>? = null
    private var tv_userName: AppCompatTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        loginActivityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val resultCode = result.resultCode
            if (resultCode == RESULT_OK) {
                presenter_main!!.getBookShelfList()
                presenter_main!!.checkUpdate()
            }
        }
        presenter_main = Presenter_Main(applicationContext, this)
        presenter_main!!.init()

        //申请权限
        //checkPermission();
        val info = presenter_main!!.token
        if (info.account_token.isEmpty() || info.account_id == 0) {
            toLoginActivity()
        } else {
            tv_userName!!.text = info.account_name
            presenter_main!!.getBookShelfList()
            presenter_main!!.checkUpdate()
        }
    }

    private fun initView() {
        val layout = LayoutInflater.from(this@MainActivity)
            .inflate(R.layout.option_nav_header, null) as LinearLayoutCompat
        tv_userName = layout.findViewById(R.id.nav_user_name)
        mViewBinding.toolbar.setNavigationOnClickListener {
            if (!mViewBinding.drawerMain.isOpen) {
                mViewBinding.drawerMain.open()
            }
        }
        mViewBinding.mainSearchButton.setOnClickListener { toSearchActivity() }
        mViewBinding.mainSwipeRefreshLayout.setOnRefreshListener { presenter_main!!.getBookShelfList() }
        if (mViewBinding.mainBooksGridView.adapter == null) {
            mAdapter = BookShelfAdapter(arrayList, applicationContext)
        }
        mViewBinding.mainBooksGridView.adapter = mAdapter
        mViewBinding.mainBooksGridView.onItemClickListener =
            OnItemClickListener { adapterView, view, i, _ ->
                val bookInfo = adapterView.adapter.getItem(i) as BookInfo
                jump2ReadView(view, bookInfo)
            }
    }

    override fun toSearchActivity() {
        val intent = Intent(this@MainActivity, SearchActivity::class.java)
        startActivity(intent)
    }

    //监听返回键，连续按2次直接退出程序
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val nowBackClick = System.currentTimeMillis()
            if (lastBackClick != 0L && nowBackClick - lastBackClick < 1500) {
                //程序退出
                exitAll()
            } else {
                //500ms以上，弹窗不处理
                lastBackClick = nowBackClick
                Toast.makeText(applicationContext, "请再按一次以退出程序", Toast.LENGTH_SHORT).show()
            }
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun jump2ReadView(view: View, bookInfo: BookInfo) {
        /*AnimationConfig animationConfig=getScreenSize(view);
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
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.i(TAG, "onAnimationRepeat: ");
            }
        });

        //播放动画
        view.startAnimation(animationSet);*/
        val bundle = Bundle()
        bundle.putSerializable("bookInfo", bookInfo)
        val intent = Intent(applicationContext, BookBrowsingActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun getScreenSize(view: View): AnimationConfig {
        val animationConfig = AnimationConfig()
        //获取屏幕大小
        val windowManager = windowManager
        val display = windowManager.defaultDisplay
        val screenPoint = Point()
        display.getSize(screenPoint)
        val view_width = view.width
        val view_height = view.height
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val move_x = (screenPoint.x - view_width) / 2 - location[0] // view距离 屏幕左边的距离（即x轴方向）
        val move_y = (screenPoint.y - view_height) / 2 - location[1] // view距离 屏幕顶边的距离（即y轴方向）
        //缩放比例
        val width_scale = screenPoint.x.toFloat() / view_width
        val height_scale = screenPoint.y.toFloat() / view_height
        animationConfig.view = view
        animationConfig.moveX = move_x / width_scale
        animationConfig.moveY = move_y / height_scale
        animationConfig.scaleX = width_scale
        animationConfig.scaleY = height_scale
        return animationConfig
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun startInstallPermissionSettingActivity() {
        //注意这个是8.0新API
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun syncBookShelf(list: ArrayList<BookInfo>) {
        //同步更新书架
        if (presenter_main != null) {
            presenter_main!!.updateBookShelf(list)
        }
        runOnUiThread {
            if (arrayList != null) {
                arrayList.clear()
                arrayList.addAll(list)
                mAdapter!!.notifyDataSetChanged()
                swipeRefreshLayout!!.isRefreshing = false
            }
        }
    }

    override fun showUpdateDialog(msg: String, url: String, version: String) {
        runOnUiThread {
            if (!StringUtil.isEmpty(version) && !StringUtil.isEmpty(msg) && !StringUtil.isEmpty(url)) {
                updateDialog = UpdateDialog(this@MainActivity)
                updateDialog!!.setInfo("检测到新版本", msg)
                    .setOnClickListener(object : OnClickBottomListener {
                        override fun onOkClick() {
                            updateDialog!!.setProgressType(true)
                            val name = "$version.apk"
                            val path =
                                Environment.getExternalStorageDirectory().path + "/apks/"
                            presenter_main!!.updateAPK(name, path, url)
                        }

                        override fun onCancelClick() {
                            updateDialog!!.dismiss()
                        }
                    })
                updateDialog!!.show()
            }
        }
    }

    override fun toLoginActivity() {
        loginActivityResultLauncher!!.launch(Intent(this, LoginActivity::class.java))
    }

    override fun updateProgress(progress: Int) {
        //更新进度
        runOnUiThread {
            updateDialog!!.setProgress(progress)
            if (progress == 100) {
                updateDialog!!.dismiss()
                updateDialog = null
            }
        }
    }

    override fun installAPK(filePath: String) {
        Log.i(
            TAG,
            "installAPK: $filePath"
        )
        //apk安装跳转
        val apk = File(filePath)
        if (!apk.exists()) {
            Log.i(TAG, "installApk: apk不存在")
            return
        }
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (apk.name.endsWith(".apk")) {
            try {
                //兼容7.0
                val uri: Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val hasInstallPermission = packageManager.canRequestPackageInstalls()
                    if (!hasInstallPermission) {
                        startInstallPermissionSettingActivity()
                    }
                    uri = FileProvider.getUriForFile(
                        applicationContext,
                        "$packageName.fileprovider", apk
                    ) //通过FileProvider创建一个content类型的Uri
                    intent.setDataAndType(uri, "application/vnd.android.package-archive") // 对应apk类型
                } else // 适配Android 7系统版本
                    uri = FileProvider.getUriForFile(
                        applicationContext,
                        "$packageName.fileprovider", apk
                    ) //通过FileProvider创建一个content类型的Uri
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) //添加这一句表示对目标应用临时授权该Uri所代表的文件
                    intent.setDataAndType(uri, "application/vnd.android.package-archive") // 对应apk类型
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //弹出安装界面
            startActivity(intent)
        } else {
            Log.i(TAG, "installApk: 不是apk文件!")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        arrayList!!.clear()
        presenter_main!!.release()
    }

    override fun showMsgDialog(type: Int, msg: String) {}

    companion object {
        private const val TAG = "主界面"
    }

    override fun getViewBinding(): ActivityMainBinding {
        Log.i(TAG, "getViewBinding: ")
        return ActivityMainBinding.inflate(layoutInflater)
    }
}