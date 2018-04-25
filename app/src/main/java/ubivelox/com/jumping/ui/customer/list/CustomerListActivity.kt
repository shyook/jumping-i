package ubivelox.com.jumping.ui.customer.list

import android.app.Activity
import android.os.Bundle
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

    /*******************************************************************************
     * Life Cycle.
     *******************************************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_list)
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

    }

    override fun initData() {
        mPresenter?.loadData()
    }
}