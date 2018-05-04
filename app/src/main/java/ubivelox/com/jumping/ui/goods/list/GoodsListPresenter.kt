package ubivelox.com.jumping.ui.goods.list

import android.app.Activity
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import ubivelox.com.jumping.database.DatabaseManager
import ubivelox.com.jumping.database.DbHelper
import ubivelox.com.jumping.ui.base.BasePresenter
import ubivelox.com.jumping.ui.common.IAdapterContract
import ubivelox.com.jumping.ui.data.GoodsData
import ubivelox.com.jumping.ui.goods.registration.GoodsRegistrationActivity
import ubivelox.com.jumping.utils.AppConsts
import ubivelox.com.jumping.utils.TextUtility

/**
 * Created by UBIVELOX on 2018-04-20.
 */
class GoodsListPresenter : BasePresenter<IGoodsListContractView>() {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    private var mView : IGoodsListContractView? = null
    private lateinit var mActivity : Activity

    private var mGoodsList: ArrayList<GoodsData> = ArrayList() // 전체 물건 리스트

    private lateinit var mAdapterModel: IAdapterContract.Model<GoodsData> // adapter에 이벤트를 바로 전달 하기 위한 인터페이스 변수
    private var mAdapterView: IAdapterContract.View? = null // adapter에 이벤트를 바로 전달 하기 위한 인터레이스 변수

    /*******************************************************************************
     * Interface Override.
     *******************************************************************************/
    override fun attachView(view: IGoodsListContractView) {
        mView = view
        mActivity = view.getActivity()
    }

    override fun detachView() {
        if (mView != null) {
            mView == null
        }
    }

    /*******************************************************************************
     * Public Method.
     *******************************************************************************/
    fun setAdapter(view: IAdapterContract.View, model: IAdapterContract.Model<GoodsData>) {
        mAdapterModel = model
        mAdapterView = view
        mAdapterView?.onClickFunc = {
            onClickListener(it)
        }
    }

    fun loadData() = runBlocking<Unit> {
        val jobs = launch {
            val cursor = DatabaseManager.getInstance(mActivity).selectAll(DbHelper.GOODS_TABLE)

            if (cursor.moveToFirst()) {
                do {
                    val data = GoodsData()
                    data.id = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_ID))
                    data.name = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_NAME))
                    data.date = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_DATE))
                    data.inputPrice = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_GOODS_INPUT_PRICE))
                    data.outputPrice = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_GOODS_OUTPUT_PRICE))
                    data.imagePath = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_GOODS_PHOTO))
                    data.memo = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_MEMO))
                    data.goodsType = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMNS_GOODS_TYPE))
                    data.isSale = TextUtility.getStringToBoolean(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMNS_GOODS_ON_SALE_YN)))

                    mGoodsList.add(data)
                } while (cursor.moveToNext())
            }
        }

        mGoodsList.clear()
        jobs.join()
        Log.i("shyook", "Call Data Setting")
        mAdapterModel.addItems(mGoodsList)
        mAdapterView?.notifyAdapter()
        // mView?.setGoodsList(mGoodsList) // adapter에 데이터를 셋팅한다.
    }

    /**
     * 전체 리스트를 반환 한다.
     */
    fun getGoodsList() : ArrayList<GoodsData> {
        val list : ArrayList<GoodsData> = ArrayList()
        list.addAll(mGoodsList)
        return list
    }

    /**
     * 고객 상세 페이지로 이동한다.
     */
    fun goGoodsDetail(id: Int) {
        val intent = Intent(mActivity, GoodsRegistrationActivity::class.java)
        intent.putExtra(AppConsts.EXTRA_GOODS_ID, id)
        mActivity?.startActivity(intent)
    }

    /*******************************************************************************
     * Inner Method.
     *******************************************************************************/
    private fun onClickListener(it: Int) {
        mAdapterModel.getItem(it).let {
            // 상세 정보로 이동
            mView?.askGoodsInfoModify(it.id)
        }
    }
}