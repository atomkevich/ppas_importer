/**
  * Created by atomkevich on 2/15/17.
  */
object entities {
  case class Advertiser(name: String, vatId: Option[String], affiliatedAdvertiserId: Option[String], id: Int, categoryId: Int, categoryName: String, active: Boolean)

  case class CorrAdvertiser(name: String, correctName: String, vatId: Option[String], affiliatedAdvertiserId: Option[String], id: Int, categoryId: Int, categoryName: String, active: Boolean) {
    def toAdv = Advertiser(name, vatId, affiliatedAdvertiserId, id, categoryId, categoryName, active)
  }
}
