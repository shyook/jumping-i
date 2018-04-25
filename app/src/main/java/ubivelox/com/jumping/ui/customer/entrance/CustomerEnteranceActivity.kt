package ubivelox.com.jumping.ui.customer.entrance

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import ubivelox.com.jumping.R
import ubivelox.com.jumping.ui.base.BaseActivity
import ubivelox.com.jumping.ui.data.CustomerData
import ubivelox.com.jumping.ui.data.CustomerEnteranceData
import ubivelox.com.jumping.ui.data.GoodsData
import ubivelox.com.jumping.utils.AppConsts
import ubivelox.com.jumping.utils.TimeUtility
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by UBIVELOX on 2018-04-17.
 */
class CustomerEnteranceActivity : BaseActivity(), ICustomerEnteranceContractView {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    private val IS_ENTERANCE_FROM_LIST = 100
    // by lazy를 통해 읽기 전용 변수를 늦게 초기화 가능하다.
    private val mSearch : Button by lazy {
        // 조회 버튼
        findViewById(R.id.customer_search_bt) as Button
    }
    private val mCustomerPhoto: ImageView by lazy {
        findViewById(R.id.customer_image) as ImageView
    }

    lateinit var mSearchName : EditText
    lateinit var mInputName : EditText
    lateinit var mUsingTime : Spinner
    var mPresenter: CustomerEnterancePresenter? = null
    var mParentsTeaItems : HashMap<Int, String> = HashMap()
    var mGoodsItems : HashMap<Int, String> = HashMap()
    var isPlaying : Boolean = false

    lateinit var mStartTime : EditText
    lateinit var mEndTime : EditText

    lateinit var mCheckedParent : RadioGroup
    lateinit var mParentDrink : Spinner
    lateinit var mParentOrderDetail : TextView

    lateinit var mAddGoods : Spinner
    lateinit var mAddGoodsDetail : TextView

    lateinit var mMemo : EditText

    lateinit var mEnterance : Button

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
        //
        mSearch.setOnClickListener(mClickListener)

        mSearchName = findViewById(R.id.customer_search_et) as EditText
        mSearchName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (TextUtils.isEmpty(p0)) {
                    mCustomerPhoto.setImageURI(null)
                    mInputName.text = null
                }
            }
        })
        mInputName = findViewById(R.id.customer_name_et) as EditText
        mUsingTime = findViewById(R.id.customer_using_time_spinner) as Spinner
        // 이용시간 영역
        mStartTime = findViewById(R.id.customer_start_time_et) as EditText
        mEndTime = findViewById(R.id.customer_end_time_et) as EditText

        // 부모 주문 영역
        mCheckedParent = findViewById(R.id.customer_parent_accompany_yn_radio) as RadioGroup
        mParentDrink = findViewById(R.id.customer_parent_tea_spinner) as Spinner
        mParentOrderDetail = findViewById(R.id.customer_parent_detail_tv) as TextView

        mEnterance = findViewById(R.id.customer_add_bt) as Button
        mEnterance.setOnClickListener(mClickListener)
        // 추가 구매로 들어온 경우 ID를 체크한다.
        val intent = intent
        if (intent != null) {
            isPlaying = intent.getBooleanExtra(AppConsts.EXTRA_IS_PLAY_TIME, false)
            if (isPlaying) {
                // 버튼명 변경
                mEnterance.setText(R.string.customer_enterance_add_items)
            }
        }

        mAddGoods = findViewById(R.id.customer_add_goods_spinner) as Spinner
        mAddGoodsDetail = findViewById(R.id.customer_add_goods_detail_tv) as TextView

        mMemo = findViewById(R.id.customer_add_memo_et) as EditText
    }

    override fun initData() {
        // 이용 시간 셋팅
        requestUsingTimeData()
        // 전체 물건에 대한 list를 db로 부터 얻어 온다. (음료, 과자, 아이스크림, 커피, 라면 등)
        mPresenter?.loadData()
        // 전체 물건 중 음료에 대한 list를 구해서 부모 음료 스피너 리스트에 넣는다 (입장료 지불 추가)
    }

    override fun displayCustomerData(oData: CustomerData) {
        // 이미지가 등록된 경우만 디스플레이
        val imagePath = oData.imagePath
        if (! TextUtils.isEmpty(imagePath)) {
            mCustomerPhoto.visibility = View.VISIBLE
            mCustomerPhoto.setImageURI(Uri.parse(imagePath))
        }

        mInputName.setText(oData.name)
    }

    override fun notFoundCustomer(name: String) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(R.string.dialog_title)
        dialog.setPositiveButton(R.string.dialog_yes, DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
            if (i == AlertDialog.BUTTON_POSITIVE) {
                mPresenter?.registCustomer(name)
            }
        })
        dialog.setNegativeButton(R.string.dialog_no, DialogInterface.OnClickListener{ dialogInterface: DialogInterface?, i: Int ->

        })
        dialog.setMessage(R.string.dialog_not_found_customer)
        dialog.create().show()

    }

    override fun setToast(toString: String) {
        Toast.makeText(this, toString, Toast.LENGTH_SHORT).show()
    }

    override fun clearAllField() {
        mSearchName.text = null
        mInputName.text = null
        mStartTime.text = null
        mEndTime.text = null
        mParentDrink.setSelection(0)
        mParentOrderDetail.text = null
        mAddGoods.setSelection(0)
        mAddGoodsDetail.text = null
        mMemo.text = null

    }

    override fun setGoodsList(goodsList: ArrayList<GoodsData>) {
        for (items in goodsList) {
            if (items.goodsType == AppConsts.GOODS_TYPE_PARENT_DRINK) {
                mParentsTeaItems.apply { put(items.id, items.name) }
            }

            mGoodsItems.apply {
                put(items.id, items.name)
            }
        }

        // 0번 항목에 선택을 삽입 한다.
        mParentsTeaItems.put(0, getText(R.string.select).toString())
        mGoodsItems.put(0, getText(R.string.select).toString())

        setParentsDrink(mParentsTeaItems)
        setAddGoods(mGoodsItems)
    }

    /*******************************************************************************
     * Inner method.
     *******************************************************************************/
    private fun setAddGoods(map: HashMap<Int, String>) {
        mAddGoods.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ArrayList<String>(map.values))
        mAddGoods.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 > 0) {
                    val selectedItem = p0?.getItemAtPosition(p2).toString()
                    val data = mPresenter?.getGoodsItem(p2)!!
                    Log.i("shyook", "selectedItem : " + selectedItem + "position : " + p2)
                    mAddGoodsDetail.text = mAddGoodsDetail.text.toString() + getString(R.string.goods_name_price, map.get(p2), data.outputPrice)
                    mPresenter?.setSaleItem(data.id)
                }
            }
        }
    }

    private fun setParentsDrink(map: HashMap<Int, String>) {
        mParentDrink.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ArrayList<String>(map.values))
        mParentDrink.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 > 0) {
                    val selectedItem = p0?.getItemAtPosition(p2).toString()
                    val data = mPresenter?.getGoodsItem(p2)!!
                    Log.i("shyook", "selectedItem : " + selectedItem + "position : " + p2)
                    mParentOrderDetail.text = mParentOrderDetail.text.toString() + getString(R.string.goods_name_price, map.get(p2), data.outputPrice)
                    mPresenter?.setSaleItem(data.id)
                }
            }
        }
    }

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
            }
        }
    }

    private val mClickListener = View.OnClickListener {
        Log.i("shyook", "OnClickListener()")
        when(it.id) {
            R.id.customer_search_bt -> mPresenter?.searchCustomer(mSearchName.text.toString())
            R.id.customer_add_bt -> getAllFiledData()
        }
    }

    private fun getAllFiledData() {
        // 데이터를 필드로 부터 얻어 온다
        val data = CustomerEnteranceData()
        if (mCheckedParent.checkedRadioButtonId == R.id.accompany_yes) {
            data.parentAccompanyYN = true
        }

        // presenter에 데이터를 저장 요청 한다.
        mPresenter?.registEnteranceCustomer(data)
    }

}

