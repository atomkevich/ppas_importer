import com.typesafe.config.ConfigFactory

/**
  * Created by atomkevich on 2/15/17.
  */
object Constants {
  val conf = ConfigFactory.load

  val ppasAdvertisersUrl = conf.getString("ppas.advertisers")
  val ppasTicket = conf.getString("ppas.ticket")
  val advertiserFile = conf.getString("advertisers.file")
  val delay = conf.getInt("ppas.delay")
  val rabitMaxCount = conf.getInt("rabitmq.max_count")
  val rabitQueueName = conf.getString("rabitmq.queue_name")
  val advertisersError = conf.getString("advertisers.error")
}
