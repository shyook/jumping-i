package ubivelox.com.jumping.ui.goods.registration

import android.app.Activity
import android.content.ContentValues
import android.net.Uri
import ubivelox.com.jumping.R
import ubivelox.com.jumping.database.DatabaseManager
import ubivelox.com.jumping.database.DbHelper
import ubivelox.com.jumping.ui.base.BasePresenter
import ubivelox.com.jumping.ui.common.IRegistrationPresenter
import ubivelox.com.jumping.ui.data.GoodsData

/**
 * Created by UBIVELOX on 2018-04-20.
 */
class GoodsRegistrationPresenter : BasePresenter<IGoodsRegistrationContractView>(), IRegistrationPresenter {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    private var mView : IGoodsRegistrationContractView? = null
    private lateinit var mActivity : Activity
    private var mImagePathUri : Uri? = null

    /*******************************************************************************
     * Interface Override.
     *******************************************************************************/
    override fun attachView(view: IGoodsRegistrationContractView) {
        mView = view
        mActivity = view.getActivity()
    }

    override fun detachView() {
        if (mView != null) {
            mView = null
        }
    }

    fun loadData() {

    }

    override fun getImagePathUri(): Uri? {
        return mImagePathUri
    }

    override fun setImagePathUri(uri: Uri?) {
        mImagePathUri = uri
    }

    /*******************************************************************************
     * public method.
     *******************************************************************************/
    fun registGoodsItem(data: GoodsData) {
        // DB에 저장 한다.
        val values = ContentValues()
        values.put(DbHelper.COLUMNS_NAME, data.name)
        values.put(DbHelper.COLUMNS_DATE, data.date)
        values.put(DbHelper.COLUMNS_MEMO, data.memo)
        values.put(DbHelper.COLUMNS_GOODS_INPUT_PRICE, data.inputPrice)
        values.put(DbHelper.COLUMNS_GOODS_OUTPUT_PRICE, data.outputPrice)
        values.put(DbHelper.COLUMNS_GOODS_TYPE, data.goodsType)
        values.put(DbHelper.COLUMNS_GOODS_PHOTO, data.imagePath)
        // 판매를 위해 등록 하므로 현재 판매중에 Y값을 저장 한다.
        values.put(DbHelper.COLUMNS_GOODS_ON_SALE_YN, "Y")

        val result = DatabaseManager.getInstance(mActivity).insert(values, DbHelper.GOODS_TABLE)

        if (result > 0) {
            mView?.setToast(mActivity.getText(R.string.customer_save_ok).toString())
            setImagePathUri(null)
            mView?.clearAllField()
        }
    }
}