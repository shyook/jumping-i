package ubivelox.com.jumping.utils

/**
 * Created by UBIVELOX on 2018-04-24.
 */
class TextUtility {
    companion object {
        fun getStringToBoolean(boolean: Boolean) : String {
            return if(boolean) "Y" else "N"
        }
    }
}