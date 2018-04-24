package ubivelox.com.jumping.ui.base

/**
 * Created by UBIVELOX on 2018-04-16.
 */
abstract class BasePresenter<T : IBaseView> {
    abstract fun attachView(view : T);
    abstract fun detachView();
    abstract fun loadData();
}