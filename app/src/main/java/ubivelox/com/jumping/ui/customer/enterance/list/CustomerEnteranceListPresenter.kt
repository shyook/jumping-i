package ubivelox.com.jumping.ui.customer.enterance.list

import android.app.Activity
import android.util.Log
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import ubivelox.com.jumping.database.DatabaseManager
import ubivelox.com.jumping.database.DbHelper
import ubivelox.com.jumping.ui.base.BasePresenter
import ubivelox.com.jumping.ui.common.IUtils
import ubivelox.com.jumping.ui.common.IAdapterContract
import ubivelox.com.jumping.ui.data.CustomerData
import ubivelox.com.jumping.ui.data.CustomerEnteranceData
import ubivelox.com.jumping.utils.TextUtility

/**
 * Created by UBIVELOX on 2018-04-27.
 */
class CustomerEnteranceListPresenter : BasePresenter<ICustomerEnteranceListContractView>(), IUtils {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    private var mView : ICustomerEnteranceListContractView? = null
    private lateinit var mActivity: Activity
    private var mEnteranceList: ArrayList<CustomerEnteranceData> = ArrayList() // 리스트
    private lateinit var mAdapterModel: IAdapterContract.Model<CustomerEnteranceData> // adapter에 이벤트를 바로 전달 하기 위한 인터페이스 변수
    private var mAdapterView: IAdapterContract.View? = null
    /*******************************************************************************
     * Interface Override.
     *******************************************************************************/
    override fun attachView(view: ICustomerEnteranceListContractView) {
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
    fun setAdapter(view: IAdapterContract.View, model: IAdapterContract.Model<CustomerEnteranceData>) {
        mAdapterModel = model
        mAdapterView = view
        mAdapterView?.onClickFunc = {
            onClickListener(it)
        }
    }

    /**
     * 고객 입장 ID를 전달 받아 입장고객에 대한 정보를 읽어 온다.
     */
    fun loadData(date: String) = runBlocking<Unit> {
        val jobs = launch {
            val cursor = DatabaseManager.getInstance(mActivity).selectDate(DbHelper.CUSTOMER_ENTERANCE_TABLE, date)

            if (cursor.moveToFirst()) {
                do {
                    val data = CustomerEnteranceData()
                    data.id = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_ID))
                    data.name = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_NAME))
                    data.date = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_DATE))
                    data.customerID = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_CUSTOMER_ID))
                    data.addGoodsID = getStringToID(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_ADD_GOODS_ID)))
                    data.enteranceTime = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_ENTERANCE_TIME))
                    data.leaveTime = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_LEAVE_TIME))
                    data.playTime = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_PLAY_TIME))
                    data.parentAccompanyYN = TextUtility.getStringToBoolean(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_PLAY_TIME)))
                    data.parentTea = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_PARENT_TEA))
                    data.memo = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_MEMO))

                    mEnteranceList.add(data)
                } while (cursor.moveToNext())
            }
        }

        val jobs1 = launch {
            val cursor = DatabaseManager.getInstance(mActivity).selectAll(DbHelper.CUSTOMER_ENTERANCE_TABLE)
            val list = ArrayList<CustomerEnteranceData>()

            if (cursor.moveToFirst()) {
                do {
                    val data = CustomerEnteranceData()
                    data.id = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_ID))
                    data.name = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_NAME))
                    data.date = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_DATE))
                    data.customerID = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_CUSTOMER_ID))
                    data.addGoodsID = getStringToID(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_ADD_GOODS_ID)))
                    data.enteranceTime = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_ENTERANCE_TIME))
                    data.leaveTime = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_LEAVE_TIME))
                    data.playTime = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_PLAY_TIME))
                    data.parentAccompanyYN = TextUtility.getStringToBoolean(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_PLAY_TIME)))
                    data.parentTea = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_PARENT_TEA))
                    data.memo = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_MEMO))

                    list.add(data)
                } while (cursor.moveToNext())
            }
        }

        mEnteranceList.clear()
        jobs.join()
        Log.i("shyook", "Call Data Setting")
        mAdapterModel.addItems(mEnteranceList)
        mAdapterView?.notifyAdapter()
    }

    /*******************************************************************************
     * Inner Method.
     *******************************************************************************/
    private fun onClickListener(it: Int) {
        mAdapterModel.getItem(it).let {
            // 상세 정보로 이동
        }
    }
}