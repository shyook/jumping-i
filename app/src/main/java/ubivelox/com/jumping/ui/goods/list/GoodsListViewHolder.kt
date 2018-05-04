package ubivelox.com.jumping.ui.goods.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ubivelox.com.jumping.R
import ubivelox.com.jumping.ui.data.GoodsData
import ubivelox.com.jumping.utils.ImageAsync

class GoodsListViewHolder(val context: Context, parent: ViewGroup?, val listenerFunc: ((Int) -> Unit)?)
    : RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_customer_list, parent, false)) {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    val imageView by lazy {
        itemView.findViewById(R.id.items_photo) as ImageView
    }

    val name by lazy {
        itemView.findViewById(R.id.items_name) as TextView
    }

    val detail by lazy {
        itemView.findViewById(R.id.itmes_detail) as TextView
    }

    /*******************************************************************************
     * Method.
     *******************************************************************************/
    fun onBind(item: GoodsData, position: Int) {
        if (!TextUtils.isEmpty(item.imagePath)) {
            ImageAsync(context, imageView).execute(item.imagePath)
        }
        name.text = context.getString(R.string.display_goods_name, item.name)
        detail.text = context.getString(R.string.display_price, item.inputPrice, item.outputPrice)
        itemView.setOnClickListener {
            listenerFunc?.invoke(position)
        }
    }
}