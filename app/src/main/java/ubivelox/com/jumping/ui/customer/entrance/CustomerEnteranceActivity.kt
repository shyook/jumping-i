package ubivelox.com.jumping.ui.customer.entrance

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import ubivelox.com.jumping.R
import ubivelox.com.jumping.ui.base.BaseActivity
import ubivelox.com.jumping.utils.TimeUtility
import java.util.*

/**
 * Created by UBIVELOX on 2018-04-17.
 */
class CustomerEnteranceActivity : BaseActivity(), ICustomerEnteranceContractView {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    lateinit var mSearch : Button
    lateinit var mInputName : EditText
    lateinit var mUsingTime : Spinner
    var mPresenter: CustomerEnterancePresenter? = null
    lateinit var mTeaItems : List<String>

    lateinit var mStartTime : EditText
    lateinit var mEndTime : EditText

    lateinit var mCheckedParent : RadioGroup
    lateinit var mParentDrink : Spinner
    lateinit var mParentOrderDetail : TextView

    /*******************************************************************************
     * Life Cycle.
     *******************************************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_entrance)

        init()
        initData();
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
    override fun getActivity(): Activity = this

    override fun init() {
//        val db = DatabaseManager.getInstance(this)
//        Log.i("shyook", "db : " + db)
        // presenter 셋팅
        mPresenter = CustomerEnterancePresenter().apply {
            attachView(this@CustomerEnteranceActivity)
        }
        // 조회 버튼
        mSearch = findViewById(R.id.customer_search_bt) as Button
        //
        mInputName = findViewById(R.id.customer_name_et) as EditText
        mUsingTime = findViewById(R.id.customer_using_time_spinner) as Spinner
        // 이용시간 영역
        mStartTime = findViewById(R.id.customer_start_time_et) as EditText
        mEndTime = findViewById(R.id.customer_end_time_et) as EditText

        // 부모 주문 영역
        mCheckedParent = findViewById(R.id.customer_parent_accompany_yn_radio) as RadioGroup
        mParentDrink = findViewById(R.id.customer_parent_tea_spinner) as Spinner
        mParentOrderDetail = findViewById(R.id.customer_parent_detail_tv) as TextView

    }

    override fun initData() {
        // 이용 시간 셋팅
        requestUsingTimeData()
        // 전체 물건에 대한 list를 db로 부터 얻어 온다. (음료, 과자, 아이스크림, 커피, 라면 등)
        mPresenter?.loadData()
        // 전체 물건 중 음료에 대한 list를 구해서 부모 음료 스피너 리스트에 넣는다 (입장료 지불 추가)
    }

    /*******************************************************************************
     * Inner method.
     *******************************************************************************/
    private fun requestUsingTimeData() {
        val oItems = resources.getStringArray(R.array.customer_using_time_items)
        val oAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, oItems)
        mUsingTime.adapter = oAdapter
        mUsingTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.i("shyook", "onNothingSelected()")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedItem = p0?.getItemAtPosition(p2).toString()
                Log.i("shyook", "selectedItem : " + selectedItem + "position : " + p2)
                if (p2 > 0) {
                    Toast.makeText(this@CustomerEnteranceActivity, selectedItem, Toast.LENGTH_SHORT).show()
                    val currentTime = Calendar.getInstance().timeInMillis
                    mStartTime.setText(TimeUtility.getCurrentTime(currentTime))
                    when(p2) {
                        1 -> mEndTime.setText(TimeUtility.getCalculateEndTime(currentTime, TimeUtility.TIME_TO_MILLISECONT_30_MINUTE))
                        2 -> mEndTime.setText(TimeUtility.getCalculateEndTime(currentTime, TimeUtility.TIME_TO_MILLISECONT_1_HOUR))
                        3 -> mEndTime.setText(TimeUtility.getCalculateEndTime(currentTime, TimeUtility.TIME_TO_MILLISECONT_2_HOUR))
                        4 -> mEndTime.setText(TimeUtility.getCalculateEndTime(currentTime, TimeUtility.TIME_TO_MILLISECONT_3_HOUR))
                        5 -> mEndTime.setText(TimeUtility.getCalculateEndTime(currentTime, TimeUtility.TIME_TO_MILLISECONT_4_HOUR))
                        else -> IllegalArgumentException("Using Time Select Error")
                    }

                }

                val checked = mCheckedParent.checkedRadioButtonId
            }

        }
    }

}