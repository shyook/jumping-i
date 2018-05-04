package ubivelox.com.jumping.ui.customer.enterance.list

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import ubivelox.com.jumping.database.DatabaseManager
import ubivelox.com.jumping.database.DbHelper
import ubivelox.com.jumping.ui.base.BasePresenter
import ubivelox.com.jumping.ui.common.IUtils
import ubivelox.com.jumping.ui.common.IAdapterContract
import ubivelox.com.jumping.ui.customer.enterance.CustomerEnteranceActivity
import ubivelox.com.jumping.ui.customer.registration.CustomerRegistrationActivity
import ubivelox.com.jumping.ui.data.CustomerData
import ubivelox.com.jumping.ui.data.CustomerEnteranceData
import ubivelox.com.jumping.utils.AppConsts
import ubivelox.com.jumping.utils.TextUtility
import ubivelox.com.jumping.utils.TimeUtility
import java.text.SimpleDateFormat
import java.util.*

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
     * 10개씩 로드할지에 대해 결정해야 함.
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
                    data.totalPayments = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_TOTAL_PAYMENTS))
                    data.customerImagePath = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_CUSTOMER_PHOTO))

                    mEnteranceList.add(data)
                } while (cursor.moveToNext())
            }
        }

        mEnteranceList.clear()
        jobs.join()
        Log.i("shyook", "Call Data Setting")
        mAdapterModel.addItems(mEnteranceList)
        mAdapterView?.notifyAdapter()
    }

    fun getDate() {
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            Log.i("shyook", "New Date : " + SimpleDateFormat(TimeUtility.DATE_FORMAT_YYYYMMDD).format(cal.time))
            mView?.setNewDate(SimpleDateFormat(TimeUtility.DATE_FORMAT_YYYYMMDD).format(cal.time))
        }

        DatePickerDialog(mActivity, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()

        Log.i("shyook", "Enter Date : " + SimpleDateFormat("yyyyMMdd").format(cal.time))
    }

    fun goCustomerEnteranceDetail(id: Int) {
        val intent = Intent(mActivity, CustomerEnteranceActivity::class.java)
        intent.putExtra(AppConsts.EXTRA_CUSTOMER_ENTERANCE_ID, id)
        mActivity?.startActivity(intent)
    }

    /*******************************************************************************
     * Inner Method.
     *******************************************************************************/
    private fun onClickListener(it: Int) {
        mAdapterModel.getItem(it).let {
            // 상세 정보로 이동
            mView?.askCustomerEnteranceInfoModify(it.id)
        }
    }

}