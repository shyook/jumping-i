package ubivelox.com.jumping.ui.base

import android.support.v7.app.AppCompatActivity

/**
 * Created by UBIVELOX on 2018-04-16.
 */

public abstract class BaseActivity : AppCompatActivity(), IBaseView {
    protected abstract fun init();
    protected open fun initData() {}
}
