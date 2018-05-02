package ubivelox.com.jumping.ui.customer.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import ubivelox.com.jumping.ui.data.CustomerData

/**
 * Created by UBIVELOX on 2018-04-25.
 */
class CustomerListAdapter (val context: Context) : RecyclerView.Adapter<CustomerListViewHolder>(), IAdapterContract.Model, IAdapterContract.View {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    private lateinit var mCustomerList: ArrayList<CustomerData>
    override var onClickFunc: ((Int) -> Unit)? = null
    /*******************************************************************************
     * ViewHolder Override.
     *******************************************************************************/
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomerListViewHolder {
        return CustomerListViewHolder(context, parent, onClickFunc)
    }

    override fun getItemCount(): Int {
        return mCustomerList.size
    }

    override fun onBindViewHolder(holder: CustomerListViewHolder?, position: Int) {
        mCustomerList[position].let {
            holder?.onBind(it, position)
        }
    }

    /*******************************************************************************
     * AdapterContract Override.
     *******************************************************************************/
    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun addItems(items: ArrayList<CustomerData>) {
        mCustomerList = items
    }

    override fun clearItem() {
        mCustomerList.clear()
    }

    override fun getItem(position: Int): CustomerData = mCustomerList.get(position)
}