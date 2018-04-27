package ubivelox.com.jumping.ui.customer.entrance.list

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import ubivelox.com.jumping.R
import ubivelox.com.jumping.ui.base.BaseActivity
import ubivelox.com.jumping.ui.customer.list.CustomerListAdapter
import ubivelox.com.jumping.utils.TimeUtility

/**
 * Created by UBIVELOX on 2018-04-27.
 */
class CustomerEnteranceListActivity : BaseActivity(), ICustomerEnteranceListContractView {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    private var mPresenter : CustomerEnteranceListPresenter? = null
    private lateinit var mCustomerListAdapter : CustomerListAdapter
    private lateinit var mRecyclerView : RecyclerView
    private lateinit var mPrevButton : Button
    private lateinit var mNextButton: Button
    private lateinit var mDateTextView : TextView

    private var mSelectedDate : String = ""

    /*******************************************************************************
     * Life Cycle.
     *******************************************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_enterance_list)

        init()
        initData(mSelectedDate)
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
        mPresenter = CustomerEnteranceListPresenter().apply {
            attachView(this@CustomerEnteranceListActivity)
        }

        mRecyclerView = findViewById(R.id.customer_enterance_list_recycler_view) as RecyclerView
        mCustomerListAdapter = CustomerListAdapter(this)
        mRecyclerView.adapter = mCustomerListAdapter

        mPrevButton = findViewById(R.id.prev_date_button) as Button
        mPrevButton.setOnClickListener(mClickListener)

        mNextButton = findViewById(R.id.next_date_button) as Button
        mNextButton.setOnClickListener(mClickListener)

        mDateTextView = (findViewById(R.id.date_display_textview) as TextView).apply {
            mSelectedDate = TimeUtility.getCurrentTimeWithFormat(TimeUtility.DATE_FORMAT_YYYYMMDD)
            setText(mSelectedDate)
            setOnClickListener(mClickListener)
        }
    }

    fun initData(date: String) {
        mPresenter?.loadData(date)
    }

    /*******************************************************************************
     * Inner Method.
     *******************************************************************************/
    private var mClickListener = View.OnClickListener {
        when(it.id) {
            R.id.next_date_button -> {
                mSelectedDate = TimeUtility.getNextOrPrevDate(mSelectedDate, true)
                mDateTextView.setText(mSelectedDate)
                mPresenter?.loadData(mSelectedDate)
            }
            R.id.prev_date_button -> {
                mSelectedDate = TimeUtility.getNextOrPrevDate(mSelectedDate, false)
                mDateTextView.setText(mSelectedDate)
                mPresenter?.loadData(mSelectedDate)
            }
        }
    }

}