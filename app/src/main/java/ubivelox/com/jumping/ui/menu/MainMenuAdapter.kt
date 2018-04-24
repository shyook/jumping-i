package ubivelox.com.jumping.ui.menu

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import ubivelox.com.jumping.ui.menu.fragment.AccountFragment
import ubivelox.com.jumping.ui.menu.fragment.CustomerFragment
import ubivelox.com.jumping.ui.menu.fragment.GoodsFragment
import ubivelox.com.jumping.utils.AppConsts

/**
 * Created by UBIVELOX on 2018-04-16.
 */
class MainMenuAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    /*******************************************************************************
     * Implements Method.
     *******************************************************************************/
    override fun getItem(position: Int): Fragment {
        when(position) {
            AppConsts.TAB_MENU_CUSTOMER -> return CustomerFragment()
            AppConsts.TAB_MENU_GOODS -> return GoodsFragment()
            AppConsts.TAB_MENU_ACCOUNT -> return AccountFragment()
            else -> throw IllegalArgumentException("Not Found Fragment") as Throwable
        }
    }

    override fun getCount() = AppConsts.VIEWPAGER_TOTAL_PAGE
}