package ubivelox.com.jumping.ui.customer.entrance

import ubivelox.com.jumping.ui.base.IBaseView
import ubivelox.com.jumping.ui.data.CustomerData
import ubivelox.com.jumping.ui.data.CustomerEnteranceData
import ubivelox.com.jumping.ui.data.GoodsData

/**
 * Created by UBIVELOX on 2018-04-17.
 */
interface ICustomerEnteranceContractView : IBaseView {
    fun displayCustomerData(oData: CustomerData)
    fun notFoundCustomer(name: String)
    fun setToast(toString: String)
    fun clearAllField()
    fun setGoodsList(goodsList: ArrayList<GoodsData>)
    fun setCustomerData(data: CustomerEnteranceData)
}