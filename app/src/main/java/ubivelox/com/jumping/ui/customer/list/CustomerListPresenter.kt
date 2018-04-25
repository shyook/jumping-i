package ubivelox.com.jumping.ui.customer.list

import android.app.Activity
import ubivelox.com.jumping.ui.base.BasePresenter

/**
 * Created by UBIVELOX on 2018-04-25.
 */
class CustomerListPresenter : BasePresenter<ICustomerListContractView>() {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    private var mView : ICustomerListContractView? = null
    private lateinit var mActivity : Activity

    /*******************************************************************************
     * Interface Override.
     *******************************************************************************/
    override fun attachView(view: ICustomerListContractView) {
        mView = view
    }

    override fun detachView() {
        if (mView != null) {
            mView == null
        }
    }

    fun loadData() {

    }
}