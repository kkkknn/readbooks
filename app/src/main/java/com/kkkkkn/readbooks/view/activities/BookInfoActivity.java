package com.kkkkkn.readbooks.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.model.adapter.BookChaptersAdapter;
import com.kkkkkn.readbooks.model.adapter.SearchBookResultAdapter;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.model.entity.ChapterInfo;
import com.kkkkkn.readbooks.presenter.Presenter_Info;
import com.kkkkkn.readbooks.presenter.Presenter_Login;
import com.kkkkkn.readbooks.util.ImageUtil;
import com.kkkkkn.readbooks.view.customView.CustomToast;
import com.kkkkkn.readbooks.view.view.BookInfoActivityView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class BookInfoActivity extends BaseActivity implements BookInfoActivityView {
    private final static String TAG="BookInfoActivity";
    private TextView book_name,author_name,book_about;
    private ImageView book_img;
    private BookInfo bookInfo;
    private ArrayList<ChapterInfo> chapterList=new ArrayList<>();
    private ListView chapter_listView;
    private BookChaptersAdapter chaptersAdapter;
    private Button btnStartRead,btnAddEnjoy;
    private boolean isEnd=false;
    private ProgressBar loading;
    private TextView nothing_tv;


    private Presenter_Info presenter_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        initView();

        presenter_info=new Presenter_Info(getApplicationContext(),this);
        presenter_info.init();

        //查找图书信息是否存在
        bookInfo=(BookInfo)getIntent().getSerializableExtra("bookInfo");
        //获取图书信息
        if(bookInfo==null||bookInfo.isEmpty()){
            finish();
            return;
        }
        author_name.setText(bookInfo.getAuthorName());
        book_name.setText(bookInfo.getBookName());
        book_about.setText(bookInfo.getBookAbout());
        ImageUtil.loadImage(bookInfo.getBookImgUrl(),getApplicationContext(),book_img);
        //发送获取图书章节列表
        presenter_info.getBookChapters(chapterList.size(),bookInfo.getBookId());
    }

    //初始化绑定控件
    private void initView(){
        //绑定控件
        book_name=findViewById(R.id.bookInfo_bookName);
        author_name=findViewById(R.id.bookInfo_authorName);
        book_img=findViewById(R.id.bookInfo_bookImg);
        book_about=findViewById(R.id.bookInfo_bookAbout);
        chapter_listView=findViewById(R.id.bookInfo_chapters_listView);
        chaptersAdapter=new BookChaptersAdapter(chapterList,getApplicationContext());
        View footView;
        LayoutInflater inflater = LayoutInflater.from(this);
        footView = inflater.inflate(R.layout.loading_layout, null);
        loading = footView.findViewById(R.id.loading_view);
        nothing_tv = footView.findViewById(R.id.nothing_view);
        chapter_listView.addFooterView(footView);
        chapter_listView.setAdapter(chaptersAdapter);
        btnStartRead=findViewById(R.id.btn_StartRead);
        btnAddEnjoy=findViewById(R.id.btn_AddEnjoy);
        //跳转到浏览界面，从第一章开始阅读
        btnStartRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMsgDialog(11,"开始阅读");
                /*Intent intent=new Intent(getApplicationContext(),BookBrowsingActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("bookInfo",bookInfo);
                bundle.putInt("pageFlag",0);
                bundle.putInt("chapterFlag",0);
                bundle.putInt("lineFlag",0);
                intent.putExtras(bundle);
                startActivity(intent);*/
            }
        });

        //添加到主页书架
        btnAddEnjoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*SqlBookUtil util=SqlBookUtil.getInstance(getApplicationContext());
                util.initDataBase();
                if(util.addEnjoyBook(bookInfo)){
                    //showToast(getApplicationContext(),"加入书架成功");
                }else {
                    //showToast(getApplicationContext(),"加入书架失败");
                }*/
            }
        });

        //章节点击跳转
        chapter_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //跳转到浏览界面，携带章节列表及点击项
                Intent intent=new Intent(getApplicationContext(),BookBrowsingActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("chapterInfo",chapterList.get(i));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        chapter_listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (absListView.getLastVisiblePosition() == absListView.getCount() - 1) {
                        //是否还有未加载的数据
                        if(isEnd){
                            //listview 展示没有更多标签
                            nothing_tv.setVisibility(View.VISIBLE);
                        }else {
                            //显示加载框
                            nothing_tv.setVisibility(View.GONE);
                            loading.setVisibility(View.VISIBLE);
                            //加载更多功能的代码
                            presenter_info.getBookChapters(chapterList.size(),bookInfo.getBookId());
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) { }
        });
    }

    @Override
    public void syncChapterList(final ArrayList<ChapterInfo> arrayList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (chapterList != null) {
                    if(arrayList.size()<presenter_info.getPageSize()){
                        isEnd=true;
                    }
                    loading.setVisibility(View.GONE);
                    chapterList.addAll(arrayList);
                    ((BookChaptersAdapter)((HeaderViewListAdapter)chapter_listView.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();

                }
            }
        });
    }

    @Override
    public void toLoginActivity() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    @Override
    public void showMsgDialog(final int type,final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(type>0){
                    CustomToast.showToast(getApplicationContext(),msg, Toast.LENGTH_SHORT,R.drawable.icon_msg_succese);
                }else {
                    CustomToast.showToast(getApplicationContext(),msg,Toast.LENGTH_SHORT,R.drawable.icon_msg_error);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chapterList.clear();
        presenter_info.release();
    }

}