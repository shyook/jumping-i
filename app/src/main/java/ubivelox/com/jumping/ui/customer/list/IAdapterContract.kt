package ubivelox.com.jumping.ui.customer.list

import ubivelox.com.jumping.ui.data.CustomerData

/**
 * Created by UBIVELOX on 2018-04-27.
 */
interface IAdapterContract {
    interface View {
        // Higher order functions
        // var onClickFunc : ((Int) -> Unit)?
        fun notifyAdapter()
    }

    interface Model {
        fun addItems(items: ArrayList<CustomerData>)

        fun clearItem()

        fun getItem(position: Int) : CustomerData
    }
}