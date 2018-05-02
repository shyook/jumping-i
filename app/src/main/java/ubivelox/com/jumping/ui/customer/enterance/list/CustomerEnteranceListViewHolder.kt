package ubivelox.com.jumping.ui.customer.enterance.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ubivelox.com.jumping.R
import ubivelox.com.jumping.ui.data.CustomerEnteranceData

/**
 * Created by UBIVELOX on 2018-05-02.
 */
class CustomerEnteranceListViewHolder(val context: Context, parent: ViewGroup?, val listenerFunc: ((Int) -> Unit)?)
    : RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_customer_enterance_list, parent, false)) {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    val imageView by lazy {
        itemView.findViewById(R.id.items_enterance_photo) as ImageView
    }

    val name by lazy {
        itemView.findViewById(R.id.items_enterance_name) as TextView
    }

    val detail by lazy {
        itemView.findViewById(R.id.itmes_enterance_detail) as TextView
    }

    val total by lazy {
        itemView.findViewById(R.id.itmes_enterance_total) as TextView
    }

    /*******************************************************************************
     * Method.
     *******************************************************************************/
    fun onBind(item : CustomerEnteranceData, position: Int) {
        // ImageAsync(context, imageView).execute(item.imagePath)
        name.text = context.getString(R.string.display_name, item.name)

        itemView.setOnClickListener{
            listenerFunc?.invoke(position)
        }
    }
}