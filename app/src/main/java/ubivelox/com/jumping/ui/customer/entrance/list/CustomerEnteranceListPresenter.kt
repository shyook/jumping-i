package ubivelox.com.jumping.ui.customer.entrance.list

import android.app.Activity
import kotlinx.coroutines.experimental.launch
import ubivelox.com.jumping.database.DatabaseManager
import ubivelox.com.jumping.database.DbHelper
import ubivelox.com.jumping.ui.base.BasePresenter
import ubivelox.com.jumping.ui.common.IUtils
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
    /**
     * 고객 입장 ID를 전달 받아 입장고객에 대한 정보를 읽어 온다.
     */
    fun loadData(date: String) {
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

                    //mCustomerList.add(data)
                } while (cursor.moveToNext())
            }
        }
    }
}