package ubivelox.com.jumping.ui.customer.registration

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import ubivelox.com.jumping.R
import ubivelox.com.jumping.ui.base.BaseActivity
import ubivelox.com.jumping.ui.data.CustomerData
import ubivelox.com.jumping.utils.AppConsts
import ubivelox.com.jumping.utils.TimeUtility


/**
 * Created by UBIVELOX on 2018-04-17.
 */
class CustomerRegistrationActivity : BaseActivity(), ICustomerRegistrationContractView {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    private var mPresenter : CustomerRegistrationPresenter? = null

    private lateinit var mCamera : Button
    private lateinit var mGallery : Button
    private lateinit var mPhoto : ImageView
    private lateinit var mName : EditText
    private lateinit var mPhone : EditText
    private lateinit var mParentsPhone : EditText
    private lateinit var mMemo : EditText
    private lateinit var mRegistration : Button

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
        // presenter 셋팅
        mPresenter = CustomerRegistrationPresenter().apply {
            attachView(this@CustomerRegistrationActivity)
        }

        mCamera = findViewById(R.id.take_photo_bt) as Button
        mCamera.setOnClickListener(mClickListener)
        mGallery = findViewById(R.id.move_gallery_bt) as Button
        mGallery.setOnClickListener(mClickListener)
        mRegistration = findViewById(R.id.registration_bt) as Button
        mRegistration.setOnClickListener(mClickListener)
        mName = findViewById(R.id.registration_name_et) as EditText
        val intent = intent
        var id = -1
        if (intent != null) {
            val name = intent.getStringExtra(AppConsts.EXTRA_CUSTOMER_NAME)
            if (!TextUtils.isEmpty(name)) {
                mName.setText(name)
            }

            // id가 존재하면 리스트에서 상세로 수정을 위해 이동한 케이스임.
            id = intent.getIntExtra(AppConsts.EXTRA_CUSTOMER_ID, -1)
            if (id != -1) {
                mRegistration.setText(R.string.modify)
            }
        }
        mPhone = findViewById(R.id.registration_first_field_et) as EditText
        mParentsPhone = findViewById(R.id.registration_second_field_et) as EditText
        mMemo = findViewById(R.id.registration_add_memo_et) as EditText
        mPhoto = findViewById(R.id.items_image) as ImageView
        mPhoto.tag = ""

        if (id > 0) {
            initData(id)
        }
    }

    fun initData(id: Int) {
        mPresenter?.loadData(id)
    }

    override fun setToast(text: String?) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    /**
     * 등록 필드의 모든 내용을 지운다.
     */
    override fun clearAllField() {
        mPhoto.setImageURI(null)
        mPhoto.tag = ""
        mMemo.text = null
        mParentsPhone.text = null
        mPhone.text = null
        mName.text = null

    }

    override fun setCustomerInfo(data: CustomerData) {
        mPhoto.setImageURI(Uri.parse(data.imagePath))
        mPhoto.tag = data.imagePath
        mMemo.setText(data.memo)
        mParentsPhone.setText(data.parentPhoneNumber)
        mPhone.setText(data.phoneNumber)
        mName.setText(data.name)
    }

    /*******************************************************************************
     * Inner Method.
     *******************************************************************************/
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
        Log.v("shyook", "save customer data")
        val name = mName.text.toString()
        // 이름은 필수 입력 사항으로 이름이 없는 경우 저장 하지 않음
        if (TextUtils.isEmpty(name)) {
            setToast(getText(R.string.input_name_please).toString())
            return
        }

        // 컴포넌트에 있는 데이터를 읽어 온다.
        val customer = CustomerData()
        customer.name = name
        customer.phoneNumber = mPhone.text.toString()
        customer.parentPhoneNumber = mParentsPhone.text.toString()
        customer.memo = mMemo.text.toString()
        customer.imagePath = mPhoto.tag.toString()

        customer.date = TimeUtility.getCurrentTimeWithFormat(TimeUtility.DATE_FORMAT_YYYYMMDD)
        mPresenter?.registCustomer(customer)
    }
}