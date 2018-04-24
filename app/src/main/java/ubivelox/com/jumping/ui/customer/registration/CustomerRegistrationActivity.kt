package ubivelox.com.jumping.ui.customer.registration

import android.app.Activity
import android.content.Intent
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
            AppConsts.PICK_FROM_CAMERA -> mPresenter?.cropImageForCamera()
            AppConsts.PICK_FROM_GALLERY -> mPresenter?.cropImageForGallery(data)
            AppConsts.PICK_FROM_IMAGE -> saveImage(data)
        }
    }
    private fun saveImage(data: Intent?) {
        Log.i("shyook", "saveImage() : " + data?.extras)
        mPhoto.setImageURI(null)
        mPhoto.setImageURI(data?.data)
        mPhoto.tag = data?.data.toString()
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
        mPhone = findViewById(R.id.registration_first_field_et) as EditText
        mParentsPhone = findViewById(R.id.registration_second_field_et) as EditText
        mMemo = findViewById(R.id.registration_add_memo_et) as EditText
        mPhoto = findViewById(R.id.items_image) as ImageView
    }

    override fun initData() {
        mPresenter?.loadData()
    }

    override fun setToast(text: String?) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    /*******************************************************************************
     * Inner Method.
     *******************************************************************************/
    private var mClickListener = View.OnClickListener {
        when(it.id) {
            R.id.take_photo_bt -> mPresenter?.doTakePhotoAction()
            R.id.move_gallery_bt -> mPresenter?.getGalleryAction()
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