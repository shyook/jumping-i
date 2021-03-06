package ubivelox.com.jumping.ui.menu

import android.content.Intent
import ubivelox.com.jumping.ui.base.BasePresenter
import ubivelox.com.jumping.ui.customer.enterance.CustomerEnteranceActivity
import ubivelox.com.jumping.ui.customer.enterance.list.CustomerEnteranceListActivity
import ubivelox.com.jumping.ui.customer.list.CustomerListActivity
import ubivelox.com.jumping.ui.customer.registration.CustomerRegistrationActivity
import ubivelox.com.jumping.ui.goods.list.GoodsListActivity
import ubivelox.com.jumping.ui.goods.registration.GoodsRegistrationActivity
import ubivelox.com.jumping.utils.AppConsts

/**
 * Created by UBIVELOX on 2018-04-16.
 */
class MainMenuPresenter : BasePresenter<IMainMenuContractView>() {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    var mView : IMainMenuContractView? = null

    /*******************************************************************************
     * Interface Override.
     *******************************************************************************/
    override fun attachView(view: IMainMenuContractView) {
        mView = view
    }

    override fun detachView() {
        mView = null;
    }

    fun loadData() {

    }

    /*******************************************************************************
     * public method.
     *******************************************************************************/
    fun moveCustomerMenu(position: Int) {
        var intent : Intent? = null
        when(position) {
            AppConsts.MOVE_PAGE_ENTERANCE -> intent = Intent(mView?.getActivity(), CustomerEnteranceActivity::class.java)
            AppConsts.MOVE_PAGE_REGISTRATION -> intent = Intent(mView?.getActivity(), CustomerRegistrationActivity::class.java)
            AppConsts.MOVE_PAGE_LIST -> intent = Intent(mView?.getActivity(), CustomerListActivity::class.java)
            AppConsts.MOVE_PAGE_ENTERANCE_LIST -> intent = Intent(mView?.getActivity(), CustomerEnteranceListActivity::class.java)
            AppConsts.MOVE_PAGE_GOODS_LIST -> intent = Intent(mView?.getActivity(), GoodsListActivity::class.java)
            AppConsts.MOVE_PAGE_GOODS_REGISTRATION -> intent = Intent(mView?.getActivity(), GoodsRegistrationActivity::class.java)

        }

        mView?.getActivity()?.startActivity(intent)
    }
}