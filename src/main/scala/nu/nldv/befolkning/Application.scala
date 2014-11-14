package nu.nldv.befolkning

import scala.io.Source

object Application extends App {

  private val apikey: String = Source.fromFile("src/main/resources/apikey").mkString

  implicit val actorSystem = akka.actor.ActorSystem("ActorSystem")
  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

  Api.getIncomeData(apikey) map {
    x =>
      x.sortBy(f=> f.inkomst).takeRight(10).foreach(i => println(i.areaText + " "+i.inkomst))
      actorSystem.shutdown()
  }

}
