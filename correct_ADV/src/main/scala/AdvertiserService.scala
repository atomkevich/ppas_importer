import entities.CorrAdvertiser

/**
  * Created by atomkevich on 2/15/17.
  */
trait AdvertiserService {
  def findAdvertisersToDeactivate(advs: Iterable[CorrAdvertiser]) =
    advs.filter(x => x.correctName != x.name).map(_.copy(active = false)).toList

  def correctInvalidNames(advs: Iterable[CorrAdvertiser]) = {
    val correctAdv = advs.toSet.find(x => x.correctName == x.name)
    val filtered = findAdvertisersToDeactivate(advs.toSet)
    correctAdv match {
      case Some(adv) => filtered.::(adv.copy(active = true)).map(_.toAdv)
      case None => {
        val correctedAdv = filtered.head.copy(name = filtered.head.correctName, active = true)
        filtered.tail.::(correctedAdv).map(_.toAdv)
      }
    }
  }

}
