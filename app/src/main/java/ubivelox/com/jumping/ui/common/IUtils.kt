package ubivelox.com.jumping.ui.common

import android.text.TextUtils
import android.util.Log

/**
 * Created by UBIVELOX on 2018-04-27.
 */
interface IUtils {

    /*******************************************************************************
     * common method.
     *******************************************************************************/
    /**
     * 리스트에 저장된 ID를 구분자를 넣고 (,) String 형식으로 만듬
     * Deprecate 사용 안함 joinToString사용
     */
    fun getIDToString(addGoodsID: ArrayList<Int>): String? {
        val join = addGoodsID.joinToString() // List를 구분자를 넣어 String으로 만들어줌 (디폴트 ,)
        val list = join.split(",") // 구분자로 분리 후 List<String>으로 반환

        val saveId = StringBuilder()
        for(id in addGoodsID) {
            saveId.append(id)
            saveId.append(",")
        }
        Log.i("shyook", "Saved Data : " + saveId)
        val removeResult = saveId.subSequence(0, saveId.lastIndex)
        Log.i("shyook", ", removed Data : " + saveId)
        return removeResult.toString()
    }

    /**
     * String 형식의 ID를 List 형식으로 변환 후 반환
     */
    fun getStringToID(id: String) : ArrayList<Int> {
        val intList: ArrayList<Int> = ArrayList()
        if (TextUtils.isEmpty(id)) {
            return intList
        }

        val result = id.split(",")
        for(strID in result) {
            val trimID = strID.trim()
            intList.add(trimID.toInt())
        }
        return intList

    }
}