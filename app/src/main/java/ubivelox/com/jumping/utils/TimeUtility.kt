package ubivelox.com.jumping.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by UBIVELOX on 2018-04-19.
 */
class TimeUtility {
    companion object {
        const val DATE_FORMAT_YYYYMMDDHHMM = "yyyy-MM-dd HH:mm"
        const val DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd"
        const val DATE_FORMAT_YYYYMMDDHHMMSS = "yyyyMMddkkmmss"

        const val TIME_TO_MILLISECONT_30_MINUTE = 1000*60*30
        const val TIME_TO_MILLISECONT_1_HOUR = 1000*60*60
        const val TIME_TO_MILLISECONT_2_HOUR = 1000*60*60*2
        const val TIME_TO_MILLISECONT_3_HOUR = 1000*60*60*3
        const val TIME_TO_MILLISECONT_4_HOUR = 1000*60*60*4

        fun getCurrentTime(currentMillis: Long) : String {
            val df = SimpleDateFormat(DATE_FORMAT_YYYYMMDDHHMM, Locale.KOREA)

            return df.format(currentMillis)
        }

        fun getCalculateEndTime(currentMillis: Long, usingTime: Int): String {
            val df = SimpleDateFormat(DATE_FORMAT_YYYYMMDDHHMM, Locale.KOREA)

            return df.format(currentMillis + usingTime)
        }

        fun getCurrentTimeWithFormat(dataFormat: String) : String {
            val df = SimpleDateFormat(dataFormat, Locale.KOREA)

            return df.format(Calendar.getInstance().timeInMillis)
        }
    }
}