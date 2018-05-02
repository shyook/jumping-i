package ubivelox.com.jumping.ui.customer.list

import android.app.Activity
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import ubivelox.com.jumping.database.DatabaseManager
import ubivelox.com.jumping.database.DbHelper
import ubivelox.com.jumping.ui.base.BasePresenter
import ubivelox.com.jumping.ui.customer.registration.CustomerRegistrationActivity
import ubivelox.com.jumping.ui.data.CustomerData
import ubivelox.com.jumping.utils.AppConsts

/**
 * Created by UBIVELOX on 2018-04-25.
 */
class CustomerListPresenter : BasePresenter<ICustomerListContractView>() {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    private var mView : ICustomerListContractView? = null
    private lateinit var mActivity : Activity

    private var mCustomerList: ArrayList<CustomerData> = ArrayList() // 전체 고객 리스트

    private lateinit var mAdapterModel: IAdapterContract.Model // adapter에 이벤트를 바로 전달 하기 위한 인터페이스 변수
    private var mAdapterView: IAdapterContract.View? = null // adapter에 이벤트를 바로 전달 하기 위한 인터레이스 변수

    /*******************************************************************************
     * Interface Override.
     *******************************************************************************/
    override fun attachView(view: ICustomerListContractView) {
        mView = view
        mActivity = view.getActivity()
    }

    override fun detachView() {
        if (mView != null) {
            mView == null
        }
    }

    /*******************************************************************************
     * Public Method.
     *******************************************************************************/
    fun setAdapter(view: IAdapterContract.View, model: IAdapterContract.Model) {
        mAdapterModel = model
        mAdapterView = view
        mAdapterView?.onClickFunc = {
            onClickListener(it)
        }
    }

    fun loadData() = runBlocking<Unit> {
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

        mCustomerList.clear()
        jobs.join()
        Log.i("shyook", "Call Data Setting")
        mAdapterModel.addItems(mCustomerList)
        mAdapterView?.notifyAdapter()
        // mView?.setGoodsList(mCustomerList) // adapter에 데이터를 셋팅한다.
    }

    /**
     * 전체 리스트를 반환 한다.
     */
    fun getCustomerList() : ArrayList<CustomerData> {
        val list : ArrayList<CustomerData> = ArrayList()
        list.addAll(mCustomerList)
        return list
    }

    /**
     * 고객 상세 페이지로 이동한다.
     */
    fun goCustomerDetail(id: Int) {
        val intent = Intent(mActivity, CustomerRegistrationActivity::class.java)
        intent.putExtra(AppConsts.EXTRA_CUSTOMER_ID, id)
        mActivity?.startActivity(intent)
    }

    /*******************************************************************************
     * Inner Method.
     *******************************************************************************/
    private fun onClickListener(it: Int) {
        mAdapterModel.getItem(it).let {
            // 상세 정보로 이동
            mView?.askCustomerInfoModify(it.id)
        }
    }
}