import nu.nldv.befolkning.Api
import nu.nldv.befolkning.model.Income

import scala.concurrent.Future
import scala.io.Source

object Application extends App {

  implicit val apikey: String = Source.fromFile("src/main/resources/apikey").mkString

  implicit val actorSystem = akka.actor.ActorSystem("ActorSystem")
  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

  Api.getPopulation map {
    x =>
      x.sortBy(_.populationNumber).reverse.foreach(println(_))
      actorSystem.shutdown()
  }

//    private val eventualIncomes: Future[List[Income]] = Api.getIncomeData
//
//    eventualIncomes map {
//      x =>
//        x.sortBy(f => f.averageIncomeWithoutZeroEarners).takeRight(10).foreach(println(_))
//        actorSystem.shutdown()
//    }

}
