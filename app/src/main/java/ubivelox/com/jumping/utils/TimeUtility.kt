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
        const val DATE_FORMAT_HHMM = "HH:mm"

        const val TIME_TO_MILLISECONT_30_MINUTE = 1000*60*30
        const val TIME_TO_MILLISECONT_1_HOUR = 1000*60*60
        const val TIME_TO_MILLISECONT_2_HOUR = 1000*60*60*2
        const val TIME_TO_MILLISECONT_3_HOUR = 1000*60*60*3
        const val TIME_TO_MILLISECONT_4_HOUR = 1000*60*60*4

        /**
         * 현재 시간을 yyyy-MM-dd HH:mm 형식으로 반환
         */
        fun getCurrentTime(currentMillis: Long) : String {
            val df = SimpleDateFormat(DATE_FORMAT_YYYYMMDDHHMM, Locale.KOREA)

            return df.format(currentMillis)
        }

        /**
         * 현재 시간 + 놀이 시간을 계산해서 종료 시간을 반환
         */
        fun getCalculateEndTime(currentMillis: Long, usingTime: Int): String {
            val df = SimpleDateFormat(DATE_FORMAT_YYYYMMDDHHMM, Locale.KOREA)

            return df.format(currentMillis + usingTime)
        }

        /**
         * 원하는 포멧 형식으로 시간을 반환
         */
        fun getCurrentTimeWithFormat(dataFormat: String) : String {
            val df = SimpleDateFormat(dataFormat, Locale.KOREA)

            return df.format(Calendar.getInstance().timeInMillis)
        }

        /**
         * 포멧팅된 시간을 Millisceond 형식으로 반환.
         */
        fun getDateToMilliseconds(date: String) : Long {
            val sdf = SimpleDateFormat(DATE_FORMAT_YYYYMMDDHHMM)
            val convertDate = sdf.parse(date)

            return convertDate.time
        }

        /**
         * milliseconds를 입력 받아 시간:분 형태로 반환 한다.
         */
        fun getMillisecondsToTimeDate(milliseconds: Long) : String {
            val df = SimpleDateFormat(DATE_FORMAT_HHMM, Locale.KOREA)

            return df.format(milliseconds)
        }

        /**
         * 이전 다음날을 반환 한다.
         */
        fun getNextOrPrevDate(date: String, isNext: Boolean) : String {
            val sdf = SimpleDateFormat(DATE_FORMAT_YYYYMMDD)
            val currentCalendar = Calendar.getInstance()
            currentCalendar.time = sdf.parse(date)

            if (isNext) {
                // 다음날
                currentCalendar.add(Calendar.DATE, 1)
            } else {
                // 전날
                currentCalendar.add(Calendar.DATE, -1)
            }
            return sdf.format(currentCalendar.time)
        }
    }
}