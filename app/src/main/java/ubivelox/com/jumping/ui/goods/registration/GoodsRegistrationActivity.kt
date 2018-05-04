package ubivelox.com.jumping.ui.goods.registration

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
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
import ubivelox.com.jumping.utils.TextUtility
import ubivelox.com.jumping.utils.TimeUtility
import java.util.*

/**
 * Created by UBIVELOX on 2018-04-20.
 */
class GoodsRegistrationActivity : BaseActivity(), IGoodsRegistrationContractView {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    private var mPresenter: GoodsRegistrationPresenter? = null

    private lateinit var mCamera: Button
    private lateinit var mGallery: Button
    private lateinit var mPhoto: ImageView
    private lateinit var mName: EditText
    private lateinit var mInputPrice: EditText
    private lateinit var mOutputPrice: EditText
    private lateinit var mMemo: EditText
    private lateinit var mRegistration: Button
    private lateinit var mDelete: Button
    private lateinit var mIsSale: Button
    private lateinit var mType: Spinner

    private var mGoodsType: Int = -1
    private var mIsOnSale = false // 현재 판매중 여부

    /*******************************************************************************
     * Life Cycle.
     *******************************************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_registration)

        init()
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

        when (requestCode) {
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

    /**
     * UI를 초기화 한다.
     */
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

        mIsSale = findViewById(R.id.on_sale_bt) as Button
        mIsOnSale = false

        val intent = intent
        var id = -1
        if (intent != null) {
            val name = intent.getStringExtra(AppConsts.EXTRA_CUSTOMER_NAME)
            if (!TextUtils.isEmpty(name)) {
                mName.setText(name)
            }

            // id가 존재하면 리스트에서 상세로 수정을 위해 이동한 케이스임.
            id = intent.getIntExtra(AppConsts.EXTRA_GOODS_ID, -1)
            if (id != -1) {
                mRegistration.setText(R.string.modify)

                mDelete = findViewById(R.id.delete_bt) as Button
                mDelete.visibility = View.VISIBLE
                mDelete.setOnClickListener(mClickListener)

                mIsSale.visibility = View.VISIBLE
                mIsSale.setOnClickListener(mClickListener)
            }
        }

        // 상품 유형 셋팅
        requestGoodsType()
        if (id > 0) {
            initData(id)
        }

    }

    /**
     * 수정 / 삭제를 위해 들어온 경우 id에 해당하는 데이터를 요청한다.
     */
    fun initData(id: Int) {
        // 데이터를 불러온다
        mPresenter?.loadData(id)
    }

    /**
     * Toast팝업 디스플레이
     */
    override fun setToast(toString: String) {
        Toast.makeText(this, toString, Toast.LENGTH_SHORT).show()
    }

    /**
     * 모든 위젯의 내용을 초기화 한다.
     */
    override fun clearAllField() {
        mPhoto.setImageURI(null)
        mPhoto.tag = ""
        mMemo.text = null
        mInputPrice.text = null
        mOutputPrice.text = null
        mName.text = null
        mType.setSelection(0)
        mIsOnSale = false
    }

    /**
     * 각 필드의 값을 채운다.
     */
    override fun setGoodsInfo(data: GoodsData) {
        mInputPrice.setText(data.inputPrice.toString())
        mOutputPrice.setText(data.outputPrice.toString())
        mName.setText(data.name)
        mType.setSelection(AppConsts.GOODS_TYPE_TO_LIST.get(data.goodsType)!!)
        mPhoto.setImageURI(Uri.parse(data.imagePath))
        mPhoto.tag = data.imagePath
        mMemo.setText(data.memo)

        // 판매중인 상품인경우 판매중지 이름의 버튼을 디스플레이 한다
        if (data.isSale) {
            mIsSale.setText(R.string.not_sales)
            mIsOnSale = true
        }

    }

    /**
     * 판매 여부에 따라 전체 필드를 enable / disable 처리 한다.
     */
    override fun changeAllFieldStatus() {
        mPhoto.isEnabled = mIsOnSale
        mMemo.isEnabled = mIsOnSale
        mInputPrice.isEnabled = mIsOnSale
        mOutputPrice.isEnabled = mIsOnSale
        mName.isEnabled = mIsOnSale
        mType.isEnabled = mIsOnSale
        mCamera.isEnabled = mIsOnSale
        mGallery.isEnabled = mIsOnSale
        mRegistration.isEnabled = mIsOnSale

        if (mIsOnSale) {
            // 판매중
            mIsSale.setText(R.string.not_sales)
        } else {
            // Dim처리
            mIsSale.setText(R.string.on_sales)
        }
    }

    /*******************************************************************************
     * Inner Method.
     *******************************************************************************/
    /**
     * 상품 타입을 spinner에 셋팅 한다.
     * 상품 목록이 늘어나면 AppConst의 내용도 변경 필요
     */
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
                    when (p2) {
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

    /**
     * 이미지를 디스플레이 및 저장을 위한 태그값을 셋팅한다.
     */
    private fun saveImage(data: Intent?) {
        Log.i("shyook", "saveImage() : " + data?.extras)
        mPhoto.setImageURI(null)
        mPhoto.setImageURI(data?.data)
        mPhoto.tag = data?.data.toString()
    }

    /**
     * 클릭 이벤트를 처리한다.
     */
    private var mClickListener = View.OnClickListener {
        when (it.id) {
            R.id.take_photo_bt -> mPresenter?.doTakePhotoAction(this)
            R.id.move_gallery_bt -> mPresenter?.getGalleryAction(this)
            R.id.registration_bt -> getAllFiledData()
            R.id.delete_bt -> askGoodsInfoDelete(mPresenter?.getLoadGoodsID()!!)
            R.id.on_sale_bt -> mPresenter?.doChangeSaleAction(changeSaleTag())
        }
    }

    /**
     * 판매중 / 판매중지에 대한 버튼 토글
     */
    private fun changeSaleTag(): String {
        mIsOnSale = !mIsOnSale

        return TextUtility.getBooleanToString(mIsOnSale)
    }

    /**
     * 해당 상품을 삭제 할지 팝업으로 알린다.
     */
    private fun askGoodsInfoDelete(id: Int) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(R.string.dialog_title)
        dialog.setPositiveButton(R.string.dialog_yes, DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
            if (i == AlertDialog.BUTTON_POSITIVE) {
                mPresenter?.doDeleteAction(id)
            }
        })
        dialog.setNegativeButton(R.string.dialog_no, DialogInterface.OnClickListener { dialogInterface: DialogInterface?, i: Int ->

        })
        dialog.setMessage(R.string.is_delete_info)
        dialog.create().show()
    }

    /**
     * data 저장을 위해 각 필드의 값을 읽어 온다
     */
    private fun getAllFiledData() {
        Log.v("shyook", "save goods data")
        val name = mName.text.toString()
        var inputPrice = 0
        var outputPrice = 0
        try {
            inputPrice = mInputPrice.text.toString().toIntOrNull()!!
            outputPrice = mOutputPrice.text.toString().toIntOrNull()!!
        } catch (e: Exception) {
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

        if (!(mGoodsType >= AppConsts.GOODS_TYPE_PARENT_DRINK && mGoodsType <= AppConsts.GOODS_TYPE_FROZEN_FOOD)) {
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