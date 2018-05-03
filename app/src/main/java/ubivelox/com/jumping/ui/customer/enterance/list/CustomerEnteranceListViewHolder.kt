package ubivelox.com.jumping.ui.customer.enterance.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ubivelox.com.jumping.R
import ubivelox.com.jumping.ui.data.CustomerEnteranceData
import ubivelox.com.jumping.utils.ImageAsync
import ubivelox.com.jumping.utils.TimeUtility

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
        if (! TextUtils.isEmpty(item.customerImagePath)) {
            ImageAsync(context, imageView).execute(item.customerImagePath)
        }
        name.text = context.getString(R.string.display_name, item.name)
        detail.text = context.getString(R.string.play_time
                , TimeUtility.getMillisecondsToTimeDate(TimeUtility.getDateToMilliseconds(item.enteranceTime))
                , TimeUtility.getMillisecondsToTimeDate(TimeUtility.getDateToMilliseconds(item.leaveTime)))
        total.text = context.getString(R.string.total_payment, item.totalPayments)

        itemView.setOnClickListener{
            listenerFunc?.invoke(position)
        }
    }
}