package ubivelox.com.jumping.ui.data

/**
 * 상품 데이터 클래스.
 *
 * Created by UBIVELOX on 2018-04-20.
 */
class GoodsData : BaseData() {
    var imagePath: String = ""
    var inputPrice: Int = 0
    var outputPrice: Int = 0
    var goodsType: Int = -1
    var memo: String = ""
    var isSale: Boolean = true
}