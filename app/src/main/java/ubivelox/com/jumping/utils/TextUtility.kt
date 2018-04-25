package ubivelox.com.jumping.utils

/**
 * Created by UBIVELOX on 2018-04-24.
 */
class TextUtility {
    companion object {
        fun getBooleanToString(boolean: Boolean) : String {
            return if(boolean) "Y" else "N"
        }

        fun getStringToBoolean(isYes: String) : Boolean {
            return if("Y".equals(isYes)) true else false
        }
    }
}