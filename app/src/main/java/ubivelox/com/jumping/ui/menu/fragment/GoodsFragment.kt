package ubivelox.com.jumping.ui.menu.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import ubivelox.com.jumping.MainActivity
import ubivelox.com.jumping.R
import ubivelox.com.jumping.utils.AppConsts

/**
 * Created by UBIVELOX on 2018-04-16.
 */
class GoodsFragment : Fragment{
    /*******************************************************************************
     * Constructor.
     *******************************************************************************/
    constructor() {

    }

    /*******************************************************************************
     * Life Cycle.
     *******************************************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_goods, container, false) as LinearLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView(view)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
    }

    /*******************************************************************************
     * Inner Method.
     *******************************************************************************/
    private fun initView(view: View?) {
        (view!!.findViewById(R.id.goods_list) as Button).apply {
            tag = AppConsts.MOVE_PAGE_GOODS_LIST
            setOnClickListener(mClickButton)
        }

        (view!!.findViewById(R.id.goods_registration) as Button).apply {
            tag = AppConsts.MOVE_PAGE_GOODS_REGISTRATION
            setOnClickListener(mClickButton)
        }
    }

    private val mClickButton = View.OnClickListener {
        val tag = it.tag as Int
        when(it.id) {
            R.id.goods_list,
            R.id.goods_registration -> (activity as MainActivity).moveCustomerMenu(tag)

        }
    }
}