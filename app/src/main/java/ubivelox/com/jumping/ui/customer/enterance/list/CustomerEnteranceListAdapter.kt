package ubivelox.com.jumping.ui.customer.enterance.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import ubivelox.com.jumping.ui.common.IAdapterContract
import ubivelox.com.jumping.ui.customer.list.CustomerListViewHolder
import ubivelox.com.jumping.ui.data.CustomerData
import ubivelox.com.jumping.ui.data.CustomerEnteranceData

/**
 * Created by UBIVELOX on 2018-05-02.
 */
class CustomerEnteranceListAdapter(val context: Context) : RecyclerView.Adapter<CustomerEnteranceListViewHolder>(), IAdapterContract.View, IAdapterContract.Model<CustomerEnteranceData> {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    private lateinit var mEnteranceList: ArrayList<CustomerEnteranceData>
    override var onClickFunc: ((Int) -> Unit)? = null
    /*******************************************************************************
     * ViewHolder Override.
     *******************************************************************************/
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomerEnteranceListViewHolder {
        return CustomerEnteranceListViewHolder(context, parent, onClickFunc)
    }

    override fun getItemCount(): Int {
        return mEnteranceList.size
    }

    override fun onBindViewHolder(holder: CustomerEnteranceListViewHolder?, position: Int) {
        mEnteranceList[position].let {
            holder?.onBind(it, position)
        }
    }

    /*******************************************************************************
     * AdapterContract Override.
     *******************************************************************************/
    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun addItems(items: ArrayList<CustomerEnteranceData>) {
        mEnteranceList = items
    }

    override fun clearItem() {
        mEnteranceList.clear()
    }

    override fun getItem(position: Int): CustomerEnteranceData = mEnteranceList.get(position)
}