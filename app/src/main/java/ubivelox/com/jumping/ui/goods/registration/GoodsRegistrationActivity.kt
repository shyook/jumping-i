package ubivelox.com.jumping.ui.goods.registration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import ubivelox.com.jumping.R
import ubivelox.com.jumping.ui.base.BaseActivity
import ubivelox.com.jumping.ui.data.CustomerData
import ubivelox.com.jumping.ui.data.GoodsData
import ubivelox.com.jumping.utils.AppConsts
import ubivelox.com.jumping.utils.TimeUtility
import java.util.*

/**
 * Created by UBIVELOX on 2018-04-20.
 */
class GoodsRegistrationActivity : BaseActivity(), IGoodsRegistrationContractView {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    private var mPresenter : GoodsRegistrationPresenter? = null

    private lateinit var mCamera : Button
    private lateinit var mGallery : Button
    private lateinit var mPhoto : ImageView
    private lateinit var mName : EditText
    private lateinit var mInputPrice : EditText
    private lateinit var mOutputPrice : EditText
    private lateinit var mMemo : EditText
    private lateinit var mRegistration : Button
    private lateinit var mType : Spinner

    private var mGoodsType: Int = -1

    /*******************************************************************************
     * Life Cycle.
     *******************************************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_registration)

        init()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPresenter != null) {
            mPresenter = null
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when(requestCode) {
            AppConsts.PICK_FROM_CAMERA -> mPresenter?.cropImageForCamera(this)
            AppConsts.PICK_FROM_GALLERY -> mPresenter?.cropImageForGallery(this, data)
            AppConsts.PICK_FROM_IMAGE -> saveImage(data)
        }
    }

    /*******************************************************************************
     * Interface Override.
     *******************************************************************************/
    override fun getActivity(): Activity {
        return this
    }

    override fun init() {
        mPresenter = GoodsRegistrationPresenter().apply {
            attachView(this@GoodsRegistrationActivity)
        }

        mCamera = findViewById(R.id.take_photo_bt) as Button
        mCamera.setOnClickListener(mClickListener)
        mGallery = findViewById(R.id.move_gallery_bt) as Button
        mGallery.setOnClickListener(mClickListener)
        mRegistration = findViewById(R.id.registration_bt) as Button
        mRegistration.setOnClickListener(mClickListener)

        mName = findViewById(R.id.registration_name_et) as EditText
        val intent = intent
        if (intent != null) {
            val name = intent.getStringExtra(AppConsts.EXTRA_CUSTOMER_NAME)
            if (!TextUtils.isEmpty(name)) {
                mName.setText(name)
            }
        }
        // 매입 금액
        (findViewById(R.id.registration_first_field_tv) as TextView).setText(R.string.input_price)
        mInputPrice = findViewById(R.id.registration_first_field_et) as EditText
        mInputPrice.hint = getText(R.string.sales_price_hint)
        mInputPrice.inputType = InputType.TYPE_CLASS_NUMBER
        // 판매 금액
        (findViewById(R.id.registration_second_field_tv) as TextView).setText(R.string.sales_price)
        mOutputPrice = findViewById(R.id.registration_second_field_et) as EditText
        mOutputPrice.hint = getText(R.string.sales_price_hint)
        mOutputPrice.inputType = InputType.TYPE_CLASS_NUMBER

        (findViewById(R.id.registration_third_field_tv) as TextView).visibility = View.VISIBLE
        mType = findViewById(R.id.registration_third_field_spinner) as Spinner
        mType.visibility = View.VISIBLE

        mMemo = findViewById(R.id.registration_add_memo_et) as EditText
        mPhoto = findViewById(R.id.items_image) as ImageView
        mPhoto.tag = ""
    }

    override fun initData() {
        // 상품 유형 셋팅
        requestGoodsType()
        //
        mPresenter?.loadData()
    }

    override fun setToast(toString: String) {
        Toast.makeText(this, toString, Toast.LENGTH_SHORT).show()
    }

    override fun clearAllField() {
        mPhoto.setImageURI(null)
        mPhoto.tag = ""
        mMemo.text = null
        mInputPrice.text = null
        mOutputPrice.text = null
        mName.text = null
        mType.setSelection(0)
    }

    /*******************************************************************************
     * Inner Method.
     *******************************************************************************/
    private fun requestGoodsType() {
        val oItems = resources.getStringArray(R.array.goods_type_items)
        val oAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, oItems)
        mType.adapter = oAdapter
        mType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.i("shyook", "onNothingSelected()")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedItem = p0?.getItemAtPosition(p2).toString()
                Log.i("shyook", "selectedItem : " + selectedItem + "position : " + p2)
                if (p2 > 0) {
                    Toast.makeText(this@GoodsRegistrationActivity, selectedItem, Toast.LENGTH_SHORT).show()
                    when(p2) {
                        1 -> mGoodsType = AppConsts.GOODS_TYPE_PARENT_DRINK
                        2 -> mGoodsType = AppConsts.GOODS_TYPE_DRINK
                        3 -> mGoodsType = AppConsts.GOODS_TYPE_COOKIE
                        4 -> mGoodsType = AppConsts.GOODS_TYPE_NOODLE
                        5 -> mGoodsType = AppConsts.GOODS_TYPE_ICE_CREAM
                        6 -> mGoodsType = AppConsts.GOODS_TYPE_FROZEN_FOOD
                        else -> IllegalArgumentException("Goods Type Select Error")
                    }
                }
            }
        }
    }

    private fun saveImage(data: Intent?) {
        Log.i("shyook", "saveImage() : " + data?.extras)
        mPhoto.setImageURI(null)
        mPhoto.setImageURI(data?.data)
        mPhoto.tag = data?.data.toString()
    }

    private var mClickListener = View.OnClickListener {
        when(it.id) {
            R.id.take_photo_bt -> mPresenter?.doTakePhotoAction(this)
            R.id.move_gallery_bt -> mPresenter?.getGalleryAction(this)
            R.id.registration_bt -> getAllFiledData()
        }
    }

    private fun getAllFiledData() {
        Log.v("shyook", "save goods data")
        val name = mName.text.toString()
        var inputPrice = 0
        var outputPrice = 0
        try {
            inputPrice = mInputPrice.text.toString().toIntOrNull()!!
            outputPrice = mOutputPrice.text.toString().toIntOrNull()!!
        } catch (e : Exception) {
            Log.e("shyook", "Price is Null")
        }
        // 이름은 필수 입력 사항으로 이름이 없는 경우 저장 하지 않음
        if (TextUtils.isEmpty(name)) {
            setToast(getText(R.string.input_name_please).toString())
            return
        }

        if (inputPrice == 0 || outputPrice == 0) {
            setToast(getText(R.string.input_price_please).toString())
            return
        }

        if (! (mGoodsType >= AppConsts.GOODS_TYPE_PARENT_DRINK && mGoodsType <= AppConsts.GOODS_TYPE_FROZEN_FOOD)) {
            setToast(getText(R.string.input_type_please).toString())
            return
        }

        // 컴포넌트에 있는 데이터를 읽어 온다.
        val goods = GoodsData()
        goods.name = name
        goods.inputPrice = inputPrice
        goods.outputPrice = outputPrice
        goods.memo = mMemo.text.toString()
        goods.imagePath = mPhoto.tag.toString()
        goods.goodsType = mGoodsType

        goods.date = TimeUtility.getCurrentTimeWithFormat(TimeUtility.DATE_FORMAT_YYYYMMDD)
        mPresenter?.registGoodsItem(goods)
    }
}