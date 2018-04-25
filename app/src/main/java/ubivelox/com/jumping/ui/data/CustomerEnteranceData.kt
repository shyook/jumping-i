package ubivelox.com.jumping.ui.data

/**
 * Created by UBIVELOX on 2018-04-24.
 */
class CustomerEnteranceData : BaseData() {
    var customerID: Int = 0
    var enteranceTime: String = ""
    var leaveTime: String = ""
    var playTime: String = ""
    var parentAccompanyYN: Boolean = false // 부모님 동반 여부
    var parentTea: String = "" // 부모님 음료 혹은 입장료 (동반 여부에 따라 값이 들어감)
    // var addGoods : ArrayList<String> = ArrayList() // 추가 구매 항목
    var addGoodsID: ArrayList<Int> = ArrayList() // 추가 구매 항목 ID
    var memo : String = ""
}