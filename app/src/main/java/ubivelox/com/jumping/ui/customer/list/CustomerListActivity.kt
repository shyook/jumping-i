package ubivelox.com.jumping.ui.customer.list

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
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
        // 구분선
        val divDecoration = DividerItemDecoration(this, LinearLayoutManager(this).orientation)
        mRecyclerView.addItemDecoration(divDecoration)
    }

    override fun initData() {
        mPresenter?.loadData()
    }

    override fun askCustomerInfoModify(id: Int) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(R.string.dialog_title)
        dialog.setPositiveButton(R.string.dialog_yes, DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
            if (i == AlertDialog.BUTTON_POSITIVE) {
                mPresenter?.goCustomerDetail(id)
            }
        })
        dialog.setNegativeButton(R.string.dialog_no, DialogInterface.OnClickListener{ dialogInterface: DialogInterface?, i: Int ->

        })
        dialog.setMessage(R.string.is_modify_info)
        dialog.create().show()
    }

    /*******************************************************************************
     * Inner Method.
     *******************************************************************************/

}