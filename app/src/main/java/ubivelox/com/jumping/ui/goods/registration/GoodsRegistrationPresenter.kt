package ubivelox.com.jumping.ui.goods.registration

import android.app.Activity
import android.content.ContentValues
import android.net.Uri
import kotlinx.coroutines.experimental.launch
import ubivelox.com.jumping.R
import ubivelox.com.jumping.database.DatabaseManager
import ubivelox.com.jumping.database.DbHelper
import ubivelox.com.jumping.ui.base.BasePresenter
import ubivelox.com.jumping.ui.common.IRegistrationPresenter
import ubivelox.com.jumping.ui.data.GoodsData
import ubivelox.com.jumping.utils.TextUtility

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
    private var mLoadID : Int = -1

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

        mLoadID = -1
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
    fun loadData(id: Int) {
        val data = GoodsData()
        val jobs = launch {
            val cursor = DatabaseManager.getInstance(mActivity).select(DbHelper.GOODS_TABLE, id)
            if (cursor.moveToFirst()) {
                data.id = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_ID))
                data.name = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_NAME))
                data.date = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_DATE))
                data.inputPrice = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_GOODS_INPUT_PRICE))
                data.outputPrice = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_GOODS_OUTPUT_PRICE))
                data.imagePath = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_GOODS_PHOTO))
                data.memo = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_MEMO))
                data.goodsType = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_GOODS_TYPE))
                data.isSale = TextUtility.getStringToBoolean(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_GOODS_ON_SALE_YN)))

                mLoadID = data.id
                mView?.clearAllField()
                mView?.setGoodsInfo(data)
            }
        }

        // jobs.join()
    }

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

    fun getLoadGoodsID() : Int {
        return mLoadID
    }

    fun doDeleteAction(loadGoodsID: Int) {
        
    }

    fun doChangeSaleAction(tag: Boolean) {

    }
}