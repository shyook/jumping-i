package ubivelox.com.jumping.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ubivelox.com.jumping.database.DbHelper.Companion.COLUMNS_DATE
import ubivelox.com.jumping.database.DbHelper.Companion.COLUMNS_ENTERANCE_TIME
import ubivelox.com.jumping.database.DbHelper.Companion.COLUMNS_ID
import ubivelox.com.jumping.utils.TimeUtility

/**
 * Created by UBIVELOX on 2018-04-17.
 */
class DatabaseManager private constructor(context : Context) {
    private var mHelper: DbHelper? = null
    private var db: SQLiteDatabase? = null

    companion object {
        private var instance : DatabaseManager? = null

        fun getInstance(context : Context) : DatabaseManager {
            if (instance == null) {
                instance = DatabaseManager(context)
            }
            return instance!!
        }
    }

    init {
        mHelper = DbHelper(context)
        db = mHelper?.writableDatabase
    }

    /**
     * Customer 테이블에 데이터 삽입
     */
    fun insert(values: ContentValues, tableName: String): Long {
        val id = db!!.insert(tableName, "", values)
        return id
    }

    /**
     * Customer 테이블에 데이터 삭제
     */
    fun delete(selection: String, selectionArgs: Array<String>, tableName: String): Int {
        val count = db!!.delete(tableName, selection, selectionArgs)
        return count
    }

    /**
     * Customer 테이블에 데이터 갱신
     */
    fun update(values: ContentValues, selection: String, selectionArgs: Array<String>, tableName: String): Int {
        val count = db!!.update(tableName, values, selection, selectionArgs)
        return count
    }

    /**
     * Customer 테이블에 전체 데이터 조회
     */
    fun selectAll(tableName: String): Cursor {
        return db!!.rawQuery("SELECT * FROM ${tableName}", null)
    }

    /**
     * 테이블에서 ID에 해당한느 데이터를 조회한다.
     */
    fun select(tableName: String, id: Int): Cursor {
        return db!!.rawQuery("SELECT * FROM ${tableName} WHERE ${COLUMNS_ID}='${id}'",null)
    }

    /**
     * 테이블에서 원하는 날짜에 대한 데이터를 조회 한다.
     */
    fun selectDate(tableName: String, date: String): Cursor {
        return db!!.rawQuery("SELECT * FROM ${tableName} WHERE ${COLUMNS_DATE}='${date}'",null)
    }
}