package ubivelox.com.jumping.utils

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
        const val MOVE_PAGE_ADDITION = 13
        const val MOVE_PAGE_SEARCH = 14

        const val PICK_FROM_CAMERA = 21
        const val PICK_FROM_GALLERY = 22
        const val PICK_FROM_IMAGE = 23

        // TODO enum 사용 방법을 아직 모름...
        enum class movePage(val position: Int) {
            MOVE_PAGE_INPUT(0), MOVE_PAGE_REGISTRATION(1), MOVE_PAGE_ADDITION(2), MOVE_PAGE_SEARCH(3)
        }
    }
}