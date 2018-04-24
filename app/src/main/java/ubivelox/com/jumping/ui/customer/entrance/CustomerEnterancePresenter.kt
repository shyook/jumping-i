package ubivelox.com.jumping.ui.customer.entrance

import android.content.Context
import ubivelox.com.jumping.ui.base.BasePresenter

/**
 * Created by UBIVELOX on 2018-04-17.
 */
class CustomerEnterancePresenter : BasePresenter<ICustomerEnteranceContractView>() {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    var mView : ICustomerEnteranceContractView? = null
    lateinit var mContext : Context

    /*******************************************************************************
     * Interface Override.
     *******************************************************************************/
    override fun attachView(view: ICustomerEnteranceContractView) {
        mView = view;
        mContext = view.getActivity()
    }

    override fun detachView() {
        if (mView != null) {
            mView = null
        }
    }

    /**
     * DB 테이블에 저장된 모든 상품 항목을 읽어 온다.
     */
    override fun loadData() {

    }

    /*******************************************************************************
     * public method.
     *******************************************************************************/

}