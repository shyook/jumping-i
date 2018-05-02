package ubivelox.com.jumping.ui.customer.registration

import ubivelox.com.jumping.ui.base.IBaseView
import ubivelox.com.jumping.ui.data.CustomerData

/**
 * Created by UBIVELOX on 2018-04-20.
 */
interface ICustomerRegistrationContractView : IBaseView {
    fun setToast(text: String?)
    fun clearAllField()
    fun setCustomerInfo(data: CustomerData)
}