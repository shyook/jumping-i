package ubivelox.com.jumping.ui.goods.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import ubivelox.com.jumping.ui.common.IAdapterContract
import ubivelox.com.jumping.ui.data.GoodsData

class GoodsListAdapter (val context: Context) : RecyclerView.Adapter<GoodsListViewHolder>(), IAdapterContract.Model<GoodsData>, IAdapterContract.View {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    private lateinit var mGoodsList: ArrayList<GoodsData>
    override var onClickFunc: ((Int) -> Unit)? = null
    /*******************************************************************************
     * ViewHolder Override.
     *******************************************************************************/
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GoodsListViewHolder {
        return GoodsListViewHolder(context, parent, onClickFunc)
    }

    override fun getItemCount(): Int {
        return mGoodsList.size
    }

    override fun onBindViewHolder(holder: GoodsListViewHolder?, position: Int) {
        mGoodsList[position].let {
            holder?.onBind(it, position)
        }
    }

    /*******************************************************************************
     * AdapterContract Override.
     *******************************************************************************/
    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun addItems(items: ArrayList<GoodsData>) {
        mGoodsList = items
    }

    override fun clearItem() {
        mGoodsList.clear()
    }

    override fun getItem(position: Int): GoodsData = mGoodsList.get(position)
}