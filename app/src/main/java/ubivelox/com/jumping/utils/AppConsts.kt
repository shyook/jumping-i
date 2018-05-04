package ubivelox.com.jumping.utils

import ubivelox.com.jumping.R

/**
 * Created by UBIVELOX on 2018-04-16.
 */
class AppConsts {
    companion object {
        /*******************************************************************************
         * Variable.
         *******************************************************************************/
        const val VIEWPAGER_TOTAL_PAGE : Int = 3

        const val TAB_MENU_CUSTOMER = 0
        const val TAB_MENU_GOODS = 1
        const val TAB_MENU_ACCOUNT = 2

        const val MOVE_PAGE_ENTERANCE = 11
        const val MOVE_PAGE_REGISTRATION = 12
        const val MOVE_PAGE_LIST = 13
        const val MOVE_PAGE_ENTERANCE_LIST = 14
        const val MOVE_PAGE_GOODS_LIST = 15
        const val MOVE_PAGE_GOODS_REGISTRATION = 16

        const val PICK_FROM_CAMERA = 21
        const val PICK_FROM_GALLERY = 22
        const val PICK_FROM_IMAGE = 23

        const val GOODS_TYPE_PARENT_DRINK = 30 // 부모 음료 혹은 입장권
        const val GOODS_TYPE_DRINK = 31 // 음료
        const val GOODS_TYPE_COOKIE = 32 // 과자
        const val GOODS_TYPE_NOODLE = 33 // 라면
        const val GOODS_TYPE_ICE_CREAM = 34 // 아이스 크림
        const val GOODS_TYPE_FROZEN_FOOD = 35 // 냉동 식품

        const val EXTRA_CUSTOMER_NAME = "EXTRA_CUSTOMER_NAME"
        // const val EXTRA_IS_PLAY_TIME = "EXTRA_IS_PLAY_TIME"
        const val EXTRA_CUSTOMER_ID =  "EXTRA_CUSTOMER_ID"
        const val EXTRA_GOODS_ID = "EXTRA_GOODS_ID"
        const val EXTRA_CUSTOMER_ENTERANCE_ID = "EXTRA_CUSTOMER_ENTERANCE_ID"

        val TIME_TO_PRICE = hashMapOf(
                R.string.customer_using_play_30_minute to 1500
                , R.string.customer_using_play_one_hour to 3000
                , R.string.customer_using_play_two_hour to 5000
                , R.string.customer_using_play_three_hour to 7000
                , R.string.customer_using_play_four_hour to 9000
        )

        val GOODS_TYPE_TO_LIST = hashMapOf(
                GOODS_TYPE_PARENT_DRINK to 1
                , GOODS_TYPE_DRINK to 2
                , GOODS_TYPE_COOKIE to 3
                , GOODS_TYPE_NOODLE to 4
                , GOODS_TYPE_ICE_CREAM to 5
                , GOODS_TYPE_FROZEN_FOOD to 6

        )

        // TODO enum 사용 방법을 아직 모름...
        enum class movePage(val position: Int) {
            MOVE_PAGE_INPUT(0), MOVE_PAGE_REGISTRATION(1), MOVE_PAGE_ADDITION(2), MOVE_PAGE_SEARCH(3)
        }
    }
}