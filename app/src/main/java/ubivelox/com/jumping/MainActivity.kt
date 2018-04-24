package ubivelox.com.jumping

import android.Manifest
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import ubivelox.com.jumping.ui.base.BaseActivity
import ubivelox.com.jumping.ui.menu.IMainMenuContractView
import ubivelox.com.jumping.ui.menu.MainMenuAdapter
import ubivelox.com.jumping.ui.menu.MainMenuPresenter
import ubivelox.com.jumping.utils.AppConsts
import ubivelox.com.jumping.utils.CircleAnimIndicator
import java.util.*


class MainActivity : BaseActivity(), IMainMenuContractView {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    var mPresenter : MainMenuPresenter? = null
    lateinit var mViewPager : ViewPager
    lateinit var mButtonLayout : LinearLayout
    lateinit var mAnimation : CircleAnimIndicator

    /*******************************************************************************
     * Life Cycle.
     *******************************************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()
        init()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPresenter != null) {
            mPresenter?.detachView()
            mPresenter = null
        }
    }

    /*******************************************************************************
     * Interface Override.
     *******************************************************************************/
    override fun getActivity() = this

    override fun init() {
        // presenter setting
        mPresenter = MainMenuPresenter().apply {
            attachView(this@MainActivity)
        }
        // view init
        mViewPager = (findViewById(R.id.display_viewpager) as ViewPager).apply {
            adapter = MainMenuAdapter(supportFragmentManager)
            currentItem = AppConsts.TAB_MENU_CUSTOMER
            offscreenPageLimit = 2
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    var i = AppConsts.TAB_MENU_CUSTOMER

                    while(i < AppConsts.VIEWPAGER_TOTAL_PAGE) {
                        if (position == i) {
                            (mButtonLayout.findViewWithTag(i) as Button).isSelected = true
                        } else {
                            (mButtonLayout.findViewWithTag(i) as Button).isSelected = false
                        }

                        i++
                    }
                    mAnimation.selectDot(position)
                }

            })
        }
        mButtonLayout = findViewById(R.id.menu_button_layout) as LinearLayout
        mAnimation = (findViewById(R.id.animation_indicator) as CircleAnimIndicator).apply {
            setItemMargin(10)
            setAnimationDuration(300)
            createDotPanel(AppConsts.VIEWPAGER_TOTAL_PAGE, R.drawable.circle_default, R.drawable.circle_select)
        }

        val oCustomerInput = (findViewById(R.id.tab_customer_button) as Button).apply {
            setOnClickListener(mClickListener)
            tag = AppConsts.TAB_MENU_CUSTOMER
            isSelected = true
        }
        val oCustomerRegister = (findViewById(R.id.tab_goods_button) as Button).apply {
            setOnClickListener(mClickListener)
            tag = AppConsts.TAB_MENU_GOODS
        }
        val oGoodsRegister = (findViewById(R.id.tab_account_button) as Button).apply {
            setOnClickListener(mClickListener)
            tag = AppConsts.TAB_MENU_ACCOUNT
        }
    }

    override fun initData() {
        mPresenter?.loadData()
    }

    /*******************************************************************************
     * Inner Method.
     *******************************************************************************/
    private val mClickListener = View.OnClickListener {
        var i = AppConsts.TAB_MENU_CUSTOMER
        val tag = it.tag as Int
        while(i < AppConsts.VIEWPAGER_TOTAL_PAGE) {
            if (tag == i) {
                (mButtonLayout.findViewWithTag(i) as Button).isSelected = true
            } else {
                (mButtonLayout.findViewWithTag(i) as Button).isSelected = false
            }

            i++
        }
        mViewPager?.currentItem = tag as Int
    }

    private val permissionlistener = object : PermissionListener {
        override fun onPermissionGranted() {
            Log.i("shyook", "퍼미션 획득")
            Toast.makeText(getActivity(), getText(R.string.permission_granted), Toast.LENGTH_SHORT).show();
        }

        override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
            Log.i("shyook", "퍼미션 획득 실패")
            Toast.makeText(getActivity(), getText(R.string.permission_denied).toString() + "\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    fun checkPermission() {
        Log.i("shyook", "checkPermission()")
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage(getText(R.string.permission_storage))
                .setDeniedMessage(getText(R.string.permission_setting))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check()

    }

    /*******************************************************************************
     * public Method.
     *******************************************************************************/
    public fun moveCustomerMenu(position: Int) {
        mPresenter?.moveCustomerMenu(position)
    }

}
