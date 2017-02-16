import AdvertiserActor.StartImport
import ErrorHandler.Analize
import RabbitChecker.{PingResponse, Ping}
import entities.Advertiser
import akka.pattern.ask
import akka.actor.{Props, Actor}
import scala.concurrent.duration._
import scala.concurrent.Await
import akka.util.Timeout

/**
  * Created by atomkevich on 2/16/17.
  */
class AdvertiserActor extends Actor with HttpPPASClient with Serializable {
  implicit val timeout = Timeout(20 seconds)

  val rabbitChecker = context.actorOf(Props[RabbitChecker], "rabitmqChecker")
  val errorHandler = context.actorOf(Props[ErrorHandler], "errorHandler")


  override def receive: Receive = {
    case StartImport => {
      AdvertiserSparkService.reimportAdv
        .sortBy(_.affiliatedAdvertiserId.getOrElse("")).foreach(importAdvertiserWithTrottling)
    }
  }


  def ping = Await.result((rabbitChecker ? Ping).mapTo[PingResponse], 50 seconds)

  def exponentialBackoff(r: Long): Long = scala.math.pow(2, r).round * 100

  def importAdvertiserWithTrottling(adv: Advertiser) = {
    var pingResponse = ping
    var retry = 0
    while (!pingResponse.valid) {
      retry += 1
      Thread sleep exponentialBackoff(retry)
      pingResponse = ping
    }
    val response = putPpasAdvertiser(adv)
    errorHandler ! Analize(response, adv)
  }

}

object AdvertiserActor {
  case object StartImport
}