package com.kkkkkn.readbooks.view.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.gyf.immersionbar.ImmersionBar
import com.kkkkkn.readbooks.databinding.ActivityBookBrowsingBinding
import com.kkkkkn.readbooks.model.clientsetting.SettingConf
import com.kkkkkn.readbooks.model.entity.BookInfo
import com.kkkkkn.readbooks.model.entity.ChapterInfo
import com.kkkkkn.readbooks.presenter.PresenterBrowsing
import com.kkkkkn.readbooks.view.customView.flipping.BrowsingView.FlushType
import com.kkkkkn.readbooks.view.customView.LoadingDialog
import com.kkkkkn.readbooks.view.customView.SettingDialog
import com.kkkkkn.readbooks.view.view.BrowsingActivityView
import es.dmoral.toasty.Toasty
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class BookBrowsingActivity : BaseActivity<ActivityBookBrowsingBinding>(), BrowsingActivityView {
    private val chapterList = ArrayList<ChapterInfo>()
    private var chapterCount = 0
    private var progressDialog: ProgressDialog? = null
    private var loadingDialog: LoadingDialog? = null
    private var bookInfo: BookInfo? = null
    private var presenterBrowsing: PresenterBrowsing? = null
    private var flushType = FlushType.FLUSH_PAGE
    private var settingConf: SettingConf? = null

    //静态广播
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == Intent.ACTION_TIME_TICK) {
                //Log.i(TAG, "onReceive: 时间变化广播接收");
                //获得系统的时间，单位为毫秒,转换为妙
                val totalMilliSeconds = System.currentTimeMillis()
                val totalSeconds = totalMilliSeconds / 1000
                //求出现在的分
                val totalMinutes = totalSeconds / 60
                val currentMinute = totalMinutes % 60
                //求出现在的小时
                val totalHour = totalMinutes / 60
                val currentHour = totalHour % 24
                //开始设置浏览界面时间
                val timeStr = "$currentHour:$currentMinute"
                //browsingView.setTimeStr(timeStr);
            } else if (action == Intent.ACTION_BATTERY_CHANGED) {
                //获取当前电量
                val level = intent.getIntExtra("level", 0)
                //Log.i(TAG, "onReceive: 电量变化广播接收"+level);
                //开始设置浏览界面电量
                //browsingView.setBatteryStr(level+"%");
            }
        }
    }

    private fun initView() {
        mViewBinding.browView.setListener(object : BookCallback {
            override fun jump2nextChapter() {
                Log.i(TAG, "jump2nextChapter: 跳转到下一章节")
                val chapterInfo = chapterList[chapterCount]
                val count = chapterInfo.chapterNum
                if (count == bookInfo!!.chapterSum) {
                    Toasty.warning(applicationContext, "已无更多章节", Toast.LENGTH_SHORT, true).show()
                } else {
                    flushType = FlushType.NEXT_PAGE
                    if (chapterCount < chapterList.size - 1) {
                        val nextChapterInfo = chapterList[++chapterCount]
                        presenterBrowsing!!.getChapterContent(nextChapterInfo.chapterPath)
                        Log.i(TAG, "jump2nextChapter: 获取下一章节了1"+chapterInfo.chapterName)
                        Log.i(TAG, "jump2nextChapter: 获取下一章节了2"+nextChapterInfo.chapterName)
                    } else {
                        presenterBrowsing!!.getChapterList(bookInfo!!.bookId, count + 1)
                    }
                }
            }

            override fun jump2lastChapter() {
                Log.i(TAG, "jump2nextChapter: 跳转到上一章节")
                if (chapterCount == 0) {
                    val chapterInfo = chapterList[chapterCount]
                    val count = chapterInfo.chapterNum
                    if (count == 0) {
                        Toasty.warning(applicationContext, "已是第一章节", Toast.LENGTH_SHORT, true).show()
                    } else {
                        flushType = FlushType.LAST_PAGE
                        presenterBrowsing!!.getChapterList(bookInfo!!.bookId, count - 1)
                    }
                } else {
                    flushType = FlushType.LAST_PAGE
                    val last_chapter_info = chapterList[--chapterCount]
                    presenterBrowsing!!.getChapterContent(last_chapter_info.chapterPath)
                }
            }

            override fun showSetting() {
                showSettingDialog()
            }
        })

        //加载框设置
        progressDialog = ProgressDialog(this)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER) //转盘
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setTitle("提示")
        progressDialog!!.setMessage("正在加载，请稍后……")
        loadingDialog = LoadingDialog(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setStatusBarColor(this,getResources().getDrawable(R.drawable.browsingview));
        initView()



        presenterBrowsing = PresenterBrowsing(applicationContext, this)

        //读取默认配置信息
        settingConf = presenterBrowsing!!.loadConfig()
        loadReadConf(settingConf)

        //获取携带信息
        val bundle = intent.extras
        if (bundle == null) {
            //没有携带信息
            finish()
            return
        }
        //序列化处理，防止转换警告
        bookInfo = bundle.getSerializable("bookInfo") as BookInfo?
        val chapterInfo = bundle.getSerializable("chapterInfo") as ChapterInfo?


        if (bookInfo == null) {
            //无图书信息，直接退出
            finish()
            return
        }

        flushChapterContent(chapterInfo)

        /*//注册静态广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(broadcastReceiver, filter);*/
    }

    override fun editStatusBar(){
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .navigationBarDarkIcon(true)
            .init()
    }

    //刷新当前章节内容
    private fun flushChapterContent(chapterInfo: ChapterInfo?) {
        //请求并获取章节内容
        if (chapterInfo != null) {
            if (chapterList.size > 0) {
                presenterBrowsing!!.getChapterContent(chapterInfo.chapterPath)
                Log.i(TAG, "flushChapterContent: 哈哈11")
            } else {
                Log.i(TAG, "flushChapterContent: 获取章节表")
                //取余数
                chapterCount = presenterBrowsing!!.chapterCount2listCount(chapterInfo.chapterNum)
                presenterBrowsing!!.getChapterList(bookInfo!!.bookId, chapterInfo.chapterNum)
            }
        } else {
            //获取缓存内是否有浏览章节进度
            val count = presenterBrowsing!!.getBookProgress(bookInfo!!.bookId)
            chapterCount = presenterBrowsing!!.chapterCount2listCount(count)
            presenterBrowsing!!.getChapterList(bookInfo!!.bookId, count)
        }
    }

    override fun showMsgDialog(type: Int, msg: String) {
        runOnUiThread {
            if (type > 0) {
                Toasty.success(applicationContext, msg, Toast.LENGTH_SHORT, true).show()
            } else {
                Toasty.error(applicationContext, msg, Toast.LENGTH_SHORT, true).show()
            }
        }
    }

    override fun syncChapterList(list: ArrayList<ChapterInfo>) {
        var info: ChapterInfo? = null
        if (chapterList.size > 0) {
            //获取当前章节，并重新进行排序
            info = chapterList[chapterCount]
        }
        //刷新章节列表
        chapterList.addAll(list)
        //修改后的章节列表 根据章节ID进行从新排序，并更新chapterCount的值
        chapterList.sort()
        if (info != null) {
            chapterCount = when (flushType) {
                FlushType.LAST_PAGE -> chapterList.indexOf(info) - 1
                FlushType.NEXT_PAGE -> chapterList.indexOf(info) + 1
                FlushType.THIS_PAGE -> chapterList.indexOf(info)
                else -> chapterList.indexOf(info)
            }
        }
        presenterBrowsing!!.getChapterContent(chapterList[chapterCount].chapterPath)
    }

    override fun toLoginActivity() {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
    }

    override fun syncReadView(jsonArray: JSONArray) {
        //设置view的字符串，并且刷新并重新绘制
        val len=jsonArray.length()
        val ares= Array<String>(len){""}
        for (i in 0 until len) {
            try {
                ares[i] = jsonArray.getString(i)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }


        mViewBinding.browView.setTextContent(ares, flushType)
    }

    override fun setLoading(type: Boolean) {
        runOnUiThread {
            if (type) {
                loadingDialog!!.showLoading("加载中")
            } else {
                loadingDialog!!.hideLoading()
            }
        }
    }

    fun loadReadConf(settingConf: SettingConf?) {
        if (settingConf == null) {
            return
        }
        mViewBinding.browView.setTextColor(settingConf.fontColor)
        mViewBinding.browView.setTextSize(settingConf.fontSize)
        mViewBinding.browView.setBackgroundStyle(settingConf.backgroundStyle)
        setBrightness(settingConf.brightness / 10f)
    }

    override fun onDestroy() {
        super.onDestroy()
        //获取是否是收藏列表内的图书
        if (chapterList.size > 0) {
            //写入当前浏览记录到数据库
            val info = chapterList[chapterCount]
            presenterBrowsing!!.setBookProgress(bookInfo!!.bookId, info.chapterNum)
        }
        //取消注册静态广播
        //unregisterReceiver(broadcastReceiver);
    }

    interface BookCallback {
        fun jump2nextChapter()
        fun jump2lastChapter()
        fun showSetting()
    }

    //弹出设置dialog 动画弹出
    private fun showSettingDialog() {
        val dialog = SettingDialog(this).setEventListener(object : SettingDialog.EventListener {
            override fun changeFontSize(size: Float) {
                mViewBinding.browView.setTextSize(size)
                settingConf!!.fontSize = size
                presenterBrowsing!!.setReadConfig(settingConf)
            }

            override fun changeBackground(style: Int) {
                mViewBinding.browView.setBackgroundStyle(style)
                settingConf!!.backgroundStyle = style
                presenterBrowsing!!.setReadConfig(settingConf)
            }

            override fun changeLight(count: Int) {
                setBrightness(count / 10f)
                Log.d(
                    TAG,
                    "changeLight: $count"
                )
                settingConf!!.brightness = count
                presenterBrowsing!!.setReadConfig(settingConf)
            }

            override fun resetSystemLight(): Int {
                val count = systemBrightness
                val value = count.toInt() / 25
                settingConf!!.brightness = value
                presenterBrowsing!!.setReadConfig(settingConf)
                return value
            }
        })
        //获取并设置系统亮度，参数值
        dialog.setConfData(settingConf)
        dialog.show()
    }

    //设置当前系统亮度
    private fun setBrightness(count: Float) {
        val window = (this as Activity).window
        val lp = window.attributes
        if (count < 0) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        } else {
            lp.screenBrightness = count
        }
        window.attributes = lp
    }

    //获取当前系统亮度
    private val systemBrightness: Float
        get() {
            var systemBrightness = 0
            try {
                systemBrightness =
                    Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)
            } catch (e: SettingNotFoundException) {
                e.printStackTrace()
            }
            return systemBrightness.toFloat()
        }

    companion object {
        private const val TAG = "BookBrowsingActivity"
    }

    override fun getViewBinding(): ActivityBookBrowsingBinding {
        return ActivityBookBrowsingBinding.inflate(layoutInflater)
    }
}