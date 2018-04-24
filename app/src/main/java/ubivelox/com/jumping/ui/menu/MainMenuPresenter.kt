package ubivelox.com.jumping.ui.menu

import android.content.Intent
import ubivelox.com.jumping.ui.base.BasePresenter
import ubivelox.com.jumping.ui.customer.entrance.CustomerEnteranceActivity
import ubivelox.com.jumping.ui.customer.registration.CustomerRegistrationActivity
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
            AppConsts.MOVE_PAGE_ADDITION -> intent = Intent(mView?.getActivity(), CustomerEnteranceActivity::class.java)
            AppConsts.MOVE_PAGE_SEARCH -> intent = Intent(mView?.getActivity(), CustomerEnteranceActivity::class.java)

        }

        mView?.getActivity()?.startActivity(intent)
    }
}