package ubivelox.com.jumping.ui.customer.list

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import ubivelox.com.jumping.R
import ubivelox.com.jumping.ui.base.BaseActivity

/**
 * Created by UBIVELOX on 2018-04-25.
 */
class CustomerListActivity : BaseActivity(), ICustomerListContractView {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    private var mPresenter : CustomerListPresenter? = null
    private lateinit var mCustomerListAdapter : CustomerListAdapter
    private lateinit var mRecyclerView : RecyclerView

    /*******************************************************************************
     * Life Cycle.
     *******************************************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_list)

        init()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPresenter != null) {
            mPresenter = null
        }
    }

    /*******************************************************************************
     * Interface Override.
     *******************************************************************************/
    override fun getActivity(): Activity {
        return this
    }

    override fun init() {
        mCustomerListAdapter = CustomerListAdapter(this)
        mPresenter = CustomerListPresenter().apply {
            attachView(this@CustomerListActivity)
            setAdapter(mCustomerListAdapter, mCustomerListAdapter)
        }

        mRecyclerView = findViewById(R.id.customer_list_recycler_view) as RecyclerView
        mRecyclerView.adapter = mCustomerListAdapter
    }

    override fun initData() {
        mPresenter?.loadData()
    }

    /*******************************************************************************
     * Inner Method.
     *******************************************************************************/

}