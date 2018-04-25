package ubivelox.com.jumping.ui.goods.registration

import ubivelox.com.jumping.ui.base.IBaseView

/**
 * Created by UBIVELOX on 2018-04-20.
 */
interface IGoodsRegistrationContractView : IBaseView {
    fun setToast(toString: String)
    fun clearAllField()
}