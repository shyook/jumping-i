package ubivelox.com.jumping.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by UBIVELOX on 2018-04-17.
 */
class DbHelper(context: Context) : SQLiteOpenHelper(context, DbHelper.DB_NAME, null, DbHelper.DB_VERSION) {
    var mContext : Context
    init {
        mContext = context
    }

    companion object {
        const val DB_NAME = "jumping.db"
        const val DB_VERSION = 1

        const val CUSTOMER_TABLE = "customer"
        const val CUSTOMER_ENTERANCE_TABLE = "customer_enterance"
        const val GOODS_TABLE = "goods"

        // Common Table
        const val COLUMNS_ID = "_id" // ID
        const val COLUMNS_NAME = "_name" // 이름
        const val COLUMNS_DATE = "_date" // 날짜
        const val COLUMNS_MEMO = "_memo" // 메모

        // Customer Table
        const val COLUMNS_TELEPHONE = "_phone" // 전화번호
        const val COLUMNS_PARENT_TELEPHONE = "_parent_phone" // 부모님 연락처
        const val COLUMNS_PHOTO = "_photo" // 사진

        // Enterance Table
        const val COLUMNS_CUSTOMER_ID = "_customer_id" // 고객 테이블 id
        const val COLUMNS_ENTERANCE_TIME = "_enterance_time" // 입장시간
        const val COLUMNS_LEAVE_TIME = "_leave_time" // 퇴장시간
        const val COLUMNS_PLAY_TIME = "_play_time" // 몇시간 인지
        const val COLUMNS_PARENT_ACCOMPANY_YN = "_accompany_parent" // 부모동반 여부
        const val COLUMNS_PARENT_TEA = "_parent_tea" // 부모님 티 혹은 입장료
        const val COLUMNS_ADD_GOODS = "_add_goods" // 추가 구매 물건

        // 상품 Table
        const val COLUMNS_GOODS_PHOTO = "_goods_image" // 상품 이미지
        const val COLUMNS_GOODS_INPUT_PRICE = "_input_price" // 매입 단가
        const val COLUMNS_GOODS_OUTPUT_PRICE = "_output_price" // 판매 단가

    }

    // DB 생성
    val DATABASE_CUSTOMER_CREATE = "CREATE TABLE if not exists " + CUSTOMER_TABLE + " (" +
            "${COLUMNS_ID} integer PRIMARY KEY autoincrement," +
            "${COLUMNS_NAME} TEXT," +
            "${COLUMNS_DATE} TEXT," +
            "${COLUMNS_TELEPHONE} TEXT," +
            "${COLUMNS_PARENT_TELEPHONE} TEXT," +
            "${COLUMNS_PHOTO} TEXT," +
            "${COLUMNS_MEMO} TEXT" +
            ")"

    val DATABASE_CUSTOMER_ENTERANCE_CREATE = "CREATE TABLE if not exists " + CUSTOMER_ENTERANCE_TABLE + " (" +
            "${COLUMNS_ID} INTEGER PRIMARY KEY autoincrement," +
            "${COLUMNS_NAME} TEXT," +
            "${COLUMNS_DATE} TEXT," +
            "${COLUMNS_CUSTOMER_ID} INTEGER," +
            "${COLUMNS_ENTERANCE_TIME} TEXT," +
            "${COLUMNS_LEAVE_TIME} TEXT," +
            "${COLUMNS_PLAY_TIME} TEXT," +
            "${COLUMNS_PARENT_ACCOMPANY_YN} TEXT," +
            "${COLUMNS_PARENT_TEA} TEXT," +
            "${COLUMNS_ADD_GOODS} TEXT," +
            "${COLUMNS_MEMO} TEXT" +
            ")"

    val DATABASE_GOODS_CREATE = "CREATE TABLE if not exists " + GOODS_TABLE + " (" +
            "${COLUMNS_ID} integer PRIMARY KEY autoincrement," +
            "${COLUMNS_NAME} TEXT," +
            "${COLUMNS_DATE} TEXT," +
            "${COLUMNS_GOODS_PHOTO} TEXT," +
            "${COLUMNS_GOODS_INPUT_PRICE} INTEGER," +
            "${COLUMNS_GOODS_OUTPUT_PRICE} INTEGER," +
            "${COLUMNS_MEMO} TEXT" +
            ")"

    // DB 삭제
    val DATABASE_CUSTOMER_DROP = "DROP TABLE IF EXISTS ${CUSTOMER_TABLE}"
    val DATABASE_CUSTOMER_ENTERANCE_DROP = "DROP TABLE IF EXISTS ${CUSTOMER_ENTERANCE_TABLE}"
    val DATABASE_GOODS_DROP = "DROP TABLE IF EXISTS ${GOODS_TABLE}"

    /*******************************************************************************
     * Interface.
     *******************************************************************************/
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(DATABASE_CUSTOMER_CREATE)
        p0?.execSQL(DATABASE_CUSTOMER_ENTERANCE_CREATE)
        p0?.execSQL(DATABASE_GOODS_CREATE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(DATABASE_CUSTOMER_DROP)
        p0?.execSQL(DATABASE_CUSTOMER_ENTERANCE_DROP)
        p0?.execSQL(DATABASE_GOODS_DROP)
        onCreate(p0)
    }
}