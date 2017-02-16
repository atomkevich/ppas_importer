import Constants.{rabitQueueName, rabitMaxCount}
import RabbitChecker.{PingResponse, Ping}
import com.github.sstone.amqp.Amqp._
import com.github.sstone.amqp.{Consumer, Amqp, ConnectionOwner}
import com.rabbitmq.client.AMQP.Queue
import com.rabbitmq.client.ConnectionFactory
import scala.concurrent.Await
import akka.actor._
import akka.pattern.ask
import scala.concurrent.duration._
import akka.util.Timeout


/**
  * Created by atomkevich on 2/15/17.
  */
class RabbitChecker extends Actor {
  implicit val duration: Timeout = 20 seconds
  val factory = new ConnectionFactory()
  factory.setUri("amqp://localhost")
  val conn = context.actorOf(ConnectionOwner.props(factory, 5 second))

  val listener = context.actorOf(Props(new Actor {
    def receive = {
      case Delivery(consumerTag, envelope, properties, body) => {
        println("got a message: " + new String(body))
        sender ! Ack(envelope.getDeliveryTag)
      }
    }
  }))
  val consumer = ConnectionOwner
    .createChildActor(conn, Consumer.props(listener, channelParams = None, autoack = false))

  // wait till everyone is actually connected to the broker
  Amqp.waitForConnection(context, consumer).await()



  override def receive: Receive = {
    case Ping => {
      val Amqp.Ok(_, Some(result: Queue.DeclareOk)) = Await.result(
        (consumer ? DeclareQueue(QueueParameters(name = rabitQueueName, passive = true))).mapTo[Amqp.Ok],
        5 seconds
      )
      sender ! PingResponse(result.getMessageCount < rabitMaxCount)
    }
  }
}


object RabbitChecker {
  case object Ping
  case class PingResponse(valid: Boolean)
}