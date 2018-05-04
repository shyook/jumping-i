package ubivelox.com.jumping.ui.customer.enterance

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import ubivelox.com.jumping.R
import ubivelox.com.jumping.database.DatabaseManager
import ubivelox.com.jumping.database.DbHelper
import ubivelox.com.jumping.ui.base.BasePresenter
import ubivelox.com.jumping.ui.common.IUtils
import ubivelox.com.jumping.ui.customer.registration.CustomerRegistrationActivity
import ubivelox.com.jumping.ui.data.CustomerData
import ubivelox.com.jumping.ui.data.CustomerEnteranceData
import ubivelox.com.jumping.ui.data.GoodsData
import ubivelox.com.jumping.utils.AppConsts
import ubivelox.com.jumping.utils.TextUtility

/**
 * Created by UBIVELOX on 2018-04-17.
 */
class CustomerEnterancePresenter : BasePresenter<ICustomerEnteranceContractView>(), IUtils {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    private var mView : ICustomerEnteranceContractView? = null
    private lateinit var mActivity: Activity
    private var mCustomerList: ArrayList<CustomerData> = ArrayList() // 진입시 고객 데이터를 읽어와 저장
    private var mGoodsList: ArrayList<GoodsData> = ArrayList() // 진입시 상품 데이터를 읽어와 저장

    private var mAllSaleItems: ArrayList<Int> = ArrayList() // 아이 입장료 이외의 구매 물건의 ID 저장

    /*******************************************************************************
     * Interface Override.
     *******************************************************************************/
    override fun attachView(view: ICustomerEnteranceContractView) {
        mView = view;
        mActivity = view.getActivity()
    }

    override fun detachView() {
        if (mView != null) {
            mView = null
        }
    }
    /*******************************************************************************
     * public method.
     *******************************************************************************/
    /**
     * 저장된 상품 리스트를 반환 한다.
     */
    fun getGoodsList() : ArrayList<GoodsData> {
        val list : ArrayList<GoodsData> = ArrayList()
        list.addAll(mGoodsList)
        return list
    }

    /**
     * 저장된 상품 리스트중 특정 아이템을 반환 한다.
     */
    fun getGoodsItem(pos: Int) : GoodsData {
        val data : GoodsData = mGoodsList.get(pos)
        return data
    }

    fun setSaleItem(id: Int) {
        mAllSaleItems.add(id)
    }

    fun getSaleItem(): ArrayList<Int> {
        return mAllSaleItems
    }

    /**
     * DB 테이블에 저장된 모든 상품 항목을 읽어 온다.
     */
    fun loadData() = runBlocking<Unit> {
        // 데이터가 DB에 저장된 경우 DB에서 읽어 오고 Server에 저장된 경우 Server에서 읽어 온다
        val jobs = launch {
            val cursor = DatabaseManager.getInstance(mActivity).selectAll(DbHelper.CUSTOMER_TABLE)

            if (cursor.moveToFirst()) {
                do {
                    val data = CustomerData()
                    data.id = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_ID))
                    data.name = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_NAME))
                    data.date = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_DATE))
                    data.phoneNumber = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_TELEPHONE))
                    data.parentPhoneNumber = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_PARENT_TELEPHONE))
                    data.imagePath = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_PHOTO))
                    data.memo = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_MEMO))

                    mCustomerList.add(data)
                } while (cursor.moveToNext())
            }
        }

        val jobsItem = launch {
            val cursor = DatabaseManager.getInstance(mActivity).selectAll(DbHelper.GOODS_TABLE)

            if (cursor.moveToFirst()) {
                do {
                    val data = GoodsData()
                    data.id = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_ID))
                    data.name = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_NAME))
                    data.date = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_DATE))
                    data.inputPrice = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_GOODS_INPUT_PRICE))
                    data.outputPrice = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_GOODS_OUTPUT_PRICE))
                    data.goodsType = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_GOODS_TYPE))
                    data.imagePath = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_GOODS_PHOTO))
                    data.isSale = TextUtility.getStringToBoolean(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_GOODS_ON_SALE_YN)))
                    data.memo = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_MEMO))

                    // 판매중인 데이터만 저장 한다.
                    if (data.isSale) {
                        mGoodsList.add(data)
                    }
                } while (cursor.moveToNext())
            }
        }

        mCustomerList.clear()
        mGoodsList.clear()
        jobsItem.join()
        mView?.setGoodsList(mGoodsList)
    }

//    fun launchTest() = runBlocking<Unit> {
//        // 데이터가 DB에 저장된 경우 DB에서 읽어 오고 Server에 저장된 경우 Server에서 읽어 온다
//        val jobs = launch {
//            for (j in 1..10) {
//                Log.i("shyook", " launch start")
//                delay(1000L)
//                Log.i("shyook", " launch end")
//            }
//        }
//
//        for(i in 1..10) {
//            Log.i("shyook", "loadData start")
//            jobs.join()
//            Log.i("shyook", "loadData end")
//        }
//
//    }

    fun searchCustomer(name: String) {
        var found = false
        for (nameList in mCustomerList) {
            if (nameList.name.equals(name)) {
                Log.i("shyook", "Found Data : " + nameList)
                mView?.displayCustomerData(nameList)
                found = true
                break;
            }
        }

        // 못찾은 경우 등록 화면으로 이동여부를 묻는 팝업 디스플레이
        if (! found) {
            // 사용자 등록 화면으로 이동 (이름을 번들로 넘김)
            mView?.notFoundCustomer(name)
        }
    }

    fun registCustomer(name: String) {
        val intent = Intent(mActivity, CustomerRegistrationActivity::class.java)
        intent.putExtra(AppConsts.EXTRA_CUSTOMER_NAME, name)
        mActivity?.startActivity(intent)
    }

    fun registEnteranceCustomer(data: CustomerEnteranceData) {
        val values = ContentValues()
        values.put(DbHelper.COLUMNS_NAME, data.name)
        values.put(DbHelper.COLUMNS_DATE, data.date)
        values.put(DbHelper.COLUMNS_CUSTOMER_ID, data.customerID)
        values.put(DbHelper.COLUMNS_ENTERANCE_TIME, data.enteranceTime)
        values.put(DbHelper.COLUMNS_LEAVE_TIME, data.leaveTime)
        values.put(DbHelper.COLUMNS_PLAY_TIME, data.playTime)
        values.put(DbHelper.COLUMNS_PARENT_ACCOMPANY_YN, TextUtility.getBooleanToString(data.parentAccompanyYN))
        values.put(DbHelper.COLUMNS_PARENT_TEA, data.parentTea)
        values.put(DbHelper.COLUMNS_ADD_GOODS_ID, data.addGoodsID.joinToString())
        values.put(DbHelper.COLUMNS_CUSTOMER_PHOTO, data.customerImagePath)
        values.put(DbHelper.COLUMNS_TOTAL_PAYMENTS, calTotalPayments(data.playTime, data.addGoodsID))
        values.put(DbHelper.COLUMNS_MEMO, data.memo)

        val db = DatabaseManager.getInstance(mActivity)
        var result = -1L
        if (data.id > 0) {
            // 추가 입력 (DB Update)
            val selectionArs = arrayOf(data.id.toString())
            result = db.update(values, DbHelper.SELECTION_ID, selectionArs, DbHelper.CUSTOMER_ENTERANCE_TABLE).toLong()
        } else {
            // 입장 (DB Insert)
            result = db.insert(values, DbHelper.CUSTOMER_ENTERANCE_TABLE)
        }

        if (result > 0) {
            mView?.setToast(mActivity.getText(R.string.customer_save_ok).toString())
            mView?.clearAllField()
        }
    }

    /**
     * 고객 입장 ID를 전달 받아 입장고객에 대한 정보를 읽어 온다.
     */
    fun loadCustomerEnteranceData(id: Int) {
        val cursor = DatabaseManager.getInstance(mActivity).select(DbHelper.CUSTOMER_ENTERANCE_TABLE, id)

        if (cursor.moveToFirst()) {
            val data = CustomerEnteranceData()
            data.id = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_ID))
            data.name = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_NAME))
            data.date = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_DATE))
            data.customerID = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_CUSTOMER_ID))
            data.customerImagePath = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_CUSTOMER_PHOTO))
            data.addGoodsID = getStringToID(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_ADD_GOODS_ID)))
            data.enteranceTime = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_ENTERANCE_TIME))
            data.leaveTime = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_LEAVE_TIME))
            data.playTime = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_PLAY_TIME))
            data.parentTea = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_PARENT_TEA))
            data.parentAccompanyYN = TextUtility.getStringToBoolean(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_PARENT_ACCOMPANY_YN)))
            data.totalPayments = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_TOTAL_PAYMENTS))
            data.memo = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_MEMO))

            // 데이터 획득에 성공 했으면 UI 셋팅을 위해 view 호출
            mView?.clearAllField()
            mView?.setCustomerData(data)
        }
    }

    /*******************************************************************************
     * Inner method.
     *******************************************************************************/
    private fun calTotalPayments(time: String, goods: ArrayList<Int>) : Int {
        var totalPrice = getTimeToPrice(time)

        totalPrice = totalPrice + getGoodsToPrice(goods)

        return totalPrice
    }

    private fun getGoodsToPrice(goods: ArrayList<Int>): Int {
        var price = 0
        for (item in goods) {
            Log.i("shyook", mGoodsList.get(item - 1).name)
            price = price + mGoodsList.get(item - 1).outputPrice
        }
        return price
    }

    private fun getTimeToPrice(time: String) : Int {
        when(time) {
            mActivity.getString(R.string.customer_using_play_30_minute) -> return AppConsts.TIME_TO_PRICE.getValue(R.string.customer_using_play_30_minute)
            mActivity.getString(R.string.customer_using_play_one_hour) -> return AppConsts.TIME_TO_PRICE.getValue(R.string.customer_using_play_one_hour)
            mActivity.getString(R.string.customer_using_play_two_hour) -> return AppConsts.TIME_TO_PRICE.getValue(R.string.customer_using_play_two_hour)
            mActivity.getString(R.string.customer_using_play_three_hour) -> return AppConsts.TIME_TO_PRICE.getValue(R.string.customer_using_play_three_hour)
            mActivity.getString(R.string.customer_using_play_four_hour) -> return AppConsts.TIME_TO_PRICE.getValue(R.string.customer_using_play_four_hour)
            else -> return 0
        }
    }

}