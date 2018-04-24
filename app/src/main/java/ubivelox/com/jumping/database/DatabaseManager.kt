package ubivelox.com.jumping.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

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
     * Customer 테이블에 데이터 조회
     */
    fun selectAll(tableName: String): Cursor {
        return db!!.rawQuery("SELECT * FROM ${tableName}", null)
    }
}