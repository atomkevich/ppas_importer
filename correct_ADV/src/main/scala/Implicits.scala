import entities.{CorrAdvertiser, Advertiser}
import org.apache.spark.sql.Row
import spray.json.{RootJsonFormat, DefaultJsonProtocol}

/**
  * Created by atomkevich on 2/15/17.
  */
object Implicits {
  object AdvertiserJsonProtocol extends DefaultJsonProtocol {
    implicit val advertiserFormat: RootJsonFormat[Advertiser] = jsonFormat7(Advertiser)
  }

  implicit class RowConverter(r: Row) {
     def toAdvertiser: CorrAdvertiser = {
      val name = r.getAs[String]("name")
      val vatId = r.getAs[String]("vatId")
      val affiliatedAdvertiserId = r.getAs[String]("affiliatedAdvertiserId")
      val id = r.getAs[Int]("id")
      val categoryName = r.getAs[String]("categoryName")
      val categoryId = r.getAs[Int]("categoryId")
      val active = r.getAs[Boolean]("active")
      val correctName = r.getAs[String]("correctName")
      CorrAdvertiser(name, correctName, Some(vatId), Some(affiliatedAdvertiserId), id, categoryId, categoryName, active)
    }
  }

}
