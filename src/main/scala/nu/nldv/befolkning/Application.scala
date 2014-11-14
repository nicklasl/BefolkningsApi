package nu.nldv.befolkning

import scala.io.Source

object Application extends App {

  private val apikey: String = Source.fromFile("src/main/resources/apikey").mkString

  implicit val actorSystem = akka.actor.ActorSystem("ActorSystem")
  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

  Api.getIncomeData(apikey) map {
    x =>
      x.sortBy(f=> f.averageIncomeWithoutZeroEarners).takeRight(2).foreach(println(_))
      actorSystem.shutdown()
  }

}
