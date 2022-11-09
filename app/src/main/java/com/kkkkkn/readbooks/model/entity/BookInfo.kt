package com.kkkkkn.readbooks.model.entity

import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable

/**
 * 图书实体类
 * private String bookName; 图书名字
 * private String bookUrl;  图书连接
 * private String authorName;   作者名字
 * private String bookAbout;    图书介绍
 * private String bookImgUrl;   图书图片链接
 * private String newChapterName;   最新章节名字
 * private String isEnjoy;     图书是否已收藏
 * private int bookFromType;    图书来源
 * private String chapterPagesUrlStr;   图书章节页码 网页版
 */
class BookInfo : Serializable {
    internal var bookId = 0
    internal var bookName: String? = null
    private var bookUrl: String? = null
    internal var authorName: String? = null
    internal var bookAbout: String? = null
    internal var bookImgUrl: String? = null
    var newChapterName: String? = null
    internal var chapterSum = 0
    private var sourceName: String? = null
    fun getBookId(): Int {
        return bookId
    }

    fun setBookId(bookId: Int) {
        this.bookId = bookId
    }

    fun getBookImgUrl(): String? {
        return bookImgUrl
    }

    fun setBookImgUrl(bookImgUrl: String?) {
        this.bookImgUrl = bookImgUrl
    }

    fun getBookAbout(): String? {
        return bookAbout
    }

    fun setBookAbout(bookAbout: String?) {
        this.bookAbout = bookAbout
    }

    fun getBookName(): String? {
        return bookName
    }

    fun setBookName(bookName: String?) {
        this.bookName = bookName
    }

    fun getBookUrl(): String? {
        return bookUrl
    }

    fun setBookUrl(bookUrl: String?) {
        this.bookUrl = bookUrl
    }

    fun getAuthorName(): String? {
        return authorName
    }

    fun setAuthorName(authorName: String?) {
        this.authorName = authorName
    }

    override fun toString(): String {
        return "SQLBookInfo{" +
                "bookName='" + bookName + '\'' +
                ", bookUrl='" + bookUrl + '\'' +
                ", authorName='" + authorName + '\'' +
                ", bookAbout='" + bookAbout + '\'' +
                ", bookImgUrl='" + bookImgUrl + '\'' +
                ", newChapterName='" + newChapterName + '\'' +
                '}'
    }

    val isEmpty: Boolean
        get() = bookName!!.isEmpty() || bookUrl!!.isEmpty()

    fun getChapterSum(): Int {
        return chapterSum
    }

    fun setChapterSum(chapterSum: Int) {
        this.chapterSum = chapterSum
    }

    fun getSourceName(): String? {
        return sourceName
    }

    fun setSourceName(sourceName: String?) {
        this.sourceName = sourceName
    }

    companion object {
        fun changeObject(`object`: JSONObject): BookInfo {
            val bookInfo = BookInfo()
            try {
                bookInfo.authorName = `object`.getString("author_name")
                bookInfo.chapterSum = `object`.getInt("book_chapter_sum")
                bookInfo.bookAbout = `object`.getString("book_about")
                bookInfo.bookId = `object`.getInt("book_id")
                bookInfo.bookImgUrl = `object`.getString("book_img_url")
                bookInfo.bookName = `object`.getString("book_name")
                bookInfo.bookUrl = `object`.getString("book_url")
                bookInfo.sourceName = `object`.getString("source_name")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return bookInfo
        }
    }
}