import Constants._
import ErrorHandler.Analize
import entities.Advertiser
import akka.actor.Actor
import com.gingersoftware.csv.ObjectCSV
import org.apache.http.client.methods.CloseableHttpResponse

/**
  * Created by atomkevich on 2/16/17.
  */
class ErrorHandler extends Actor {
  var not_valid_advs = List[Advertiser]()
  override def receive: Receive = {
    case Analize(resp, adv) => {
      if (resp.getStatusLine.getStatusCode == 200) {
        not_valid_advs = not_valid_advs.::(adv)
      }
    }
  }
  override def postStop: Unit = {
    ObjectCSV().writeCSV(IndexedSeq(not_valid_advs), advertisersError)
  }
}

object ErrorHandler {
  case class Analize(response: CloseableHttpResponse, advertiser: Advertiser)
}