package ubivelox.com.jumping.ui.common

/**
 * Created by UBIVELOX on 2018-04-27.
 */
interface IAdapterContract {
    interface View {
        // Higher order functions
        var onClickFunc : ((Int) -> Unit)?
        fun notifyAdapter()
    }

    interface Model<T> {
        fun addItems(items: ArrayList<T>)

        fun clearItem()

        fun getItem(position: Int) : T
    }
}