import Constants.{advertisersError, delay, ppasTicket, ppasAdvertisersUrl}
import Implicits.AdvertiserJsonProtocol
import com.gingersoftware.csv.ObjectCSV
import entities.Advertiser
import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet, HttpPut}
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import spray.json._
import DefaultJsonProtocol._

/**
  * Created by atomkevich on 2/15/17.
  */
trait HttpPPASClient {
  def putPpasAdvertiser(adv: Advertiser) = {
    import AdvertiserJsonProtocol._

    val put = new HttpPut(ppasAdvertisersUrl)
    put.setHeader("Content-type", "application/json")
    put.setHeader("ticket", ppasTicket)
    val jsonAdv = adv.toJson.prettyPrint
    println("PUT:   " + jsonAdv)
    put.setEntity(new StringEntity(jsonAdv))
    val response = HttpClientBuilder.create().build().execute(put)
    Thread sleep delay
    response
  }

  def getRestContent(start: Int): List[Advertiser] = {

    val httpClient = HttpClientBuilder.create().build()
    val get = new HttpGet(ppasAdvertisersUrl + "?offset="  + start)
    get.addHeader("ticket", ppasTicket)
    val httpResponse = httpClient.execute(get)
    val entity = httpResponse.getEntity
    val total_advertisers = if (entity != null) {
      val inputStream = entity.getContent
      val content = scala.io.Source.fromInputStream(inputStream).getLines
      val advertisers: List[Advertiser] = content.flatMap(str => {
        import AdvertiserJsonProtocol._

        val jsonStr = str.parseJson
        jsonStr.convertTo[List[Advertiser]]
      }).toList
      inputStream.close()
      advertisers
    } else {
      List.empty
    }
    httpClient.close()
    total_advertisers
  }

  def getAllAdvertisersFromPPas: List[Advertiser] = {
    var i = 0
    var adv_total = getRestContent(i)
    var receivedSize = adv_total.size
    while(receivedSize > 0) {
      println(i)
      val received = getRestContent(i)
      adv_total = adv_total ++ received
      receivedSize = received.size
      i += 1000
    }
    adv_total
  }
}
