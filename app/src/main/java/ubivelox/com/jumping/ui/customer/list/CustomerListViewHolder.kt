package ubivelox.com.jumping.ui.customer.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ubivelox.com.jumping.R
import ubivelox.com.jumping.ui.data.CustomerData
import ubivelox.com.jumping.utils.ImageAsync

/**
 * Created by UBIVELOX on 2018-04-25.
 */
class CustomerListViewHolder (val context: Context, parent: ViewGroup?, val listenerFunc: ((Int) -> Unit)?)
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
    fun onBind(item : CustomerData, position: Int) {
        if (! TextUtils.isEmpty(item.imagePath)) {
            ImageAsync(context, imageView).execute(item.imagePath)
        }
        name.text = context.getString(R.string.display_name, item.name)
        if (! TextUtils.isEmpty(item.phoneNumber)) {
            if (! TextUtils.isEmpty(item.parentPhoneNumber)) {
                detail.text = context.getString(R.string.display_phone_with_parent, item.phoneNumber, item.parentPhoneNumber)
            } else {
                detail.text = context.getString(R.string.display_phone, item.phoneNumber)
            }
        }
        itemView.setOnClickListener{
            listenerFunc?.invoke(position)
        }
    }
}