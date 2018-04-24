package ubivelox.com.jumping.ui.customer.entrance

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
import ubivelox.com.jumping.ui.customer.registration.CustomerRegistrationActivity
import ubivelox.com.jumping.ui.data.CustomerData
import ubivelox.com.jumping.ui.data.CustomerEnteranceData
import ubivelox.com.jumping.ui.data.GoodsData
import ubivelox.com.jumping.ui.goods.registration.GoodsRegistrationActivity
import ubivelox.com.jumping.utils.AppConsts
import ubivelox.com.jumping.utils.TextUtility

/**
 * Created by UBIVELOX on 2018-04-17.
 */
class CustomerEnterancePresenter : BasePresenter<ICustomerEnteranceContractView>() {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    var mView : ICustomerEnteranceContractView? = null
    lateinit var mActivity: Activity
    var mCustomerList: ArrayList<CustomerData> = ArrayList()
    var mGoodsList: ArrayList<GoodsData> = ArrayList()

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
     * DB 테이블에 저장된 모든 상품 항목을 읽어 온다.
     */
    fun loadData() = runBlocking<Unit> {
        // 데이터가 DB에 저장된 경우 DB에서 읽어 오고 Server에 저장된 경우 Server에서 읽어 온다
        val jobs = launch {
            val cursor = DatabaseManager.getInstance(mActivity).selectAll(DbHelper.CUSTOMER_TABLE)

            if (cursor.moveToFirst()) {
                val data = CustomerData()
                do {
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
                val data = GoodsData()
                do {
                    data.id = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_ID))
                    data.name = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_NAME))
                    data.date = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_DATE))
                    data.inputPrice = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_GOODS_INPUT_PRICE))
                    data.outputPrice = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_GOODS_OUTPUT_PRICE))
                    data.goodsType = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_GOODS_TYPE))
                    data.imagePath = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_PHOTO))
                    data.memo = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_MEMO))

                    mGoodsList.add(data)
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
        values.put(DbHelper.COLUMNS_PARENT_ACCOMPANY_YN, TextUtility.getStringToBoolean(data.parentAccompanyYN))
        values.put(DbHelper.COLUMNS_PARENT_TEA, data.parentTea)
        values.put(DbHelper.COLUMNS_ADD_GOODS, data.addGoods)
        values.put(DbHelper.COLUMNS_MEMO, data.memo)

        val db = DatabaseManager.getInstance(mActivity)
        var result = -1L
        if (data.id > 0) {
            // 추가 입력 (DB Update)
            val selectionArs = arrayOf(data.id.toString())
            result = db.update(values, DbHelper.UPDATE_SELECTION_ID, selectionArs, DbHelper.CUSTOMER_ENTERANCE_TABLE).toLong()
        } else {
            // 입장 (DB Insert)
            result = db.insert(values, DbHelper.CUSTOMER_ENTERANCE_TABLE)
        }

        if (result > 0) {
            mView?.setToast(mActivity.getText(R.string.customer_save_ok).toString())
            mView?.clearAllField()
        }
    }

    /*******************************************************************************
     * Inner method.
     *******************************************************************************/


}