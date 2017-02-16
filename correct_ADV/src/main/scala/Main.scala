/**
  * Created by atomkevich on 2/14/17.
  */

import AdvertiserActor.StartImport
import akka.actor.{Props, ActorSystem}
object Main {
  def main(args: Array[String]) = {
    val system = ActorSystem("HelloSystem")
    val advActor = system.actorOf(Props[AdvertiserActor])
    advActor ! StartImport
  }
}



