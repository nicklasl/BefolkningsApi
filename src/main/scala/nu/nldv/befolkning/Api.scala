package nu.nldv.befolkning

import java.net.URL
import java.util.concurrent.TimeUnit

import akka.actor.{Props, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import com.ning.http.client.AsyncHttpClientConfig
import nu.nldv.befolkning.actors.{PopulationParseActor, IncomeParseActor, DownloadActor}
import nu.nldv.befolkning.model.{Population, Result, Income}
import play.api.libs.ws.ning.NingWSClient

import scala.concurrent.{ExecutionContext, Future}
import scala.xml.Elem

object Api {

  private val baseUri = "http://data.stockholm.se/set/Befolkning"
  private val incomeUri = s"$baseUri/Medelinkomst/"
  private val populationUri = s"$baseUri/Befolkning/"

  implicit val timeout = Timeout(5, TimeUnit.SECONDS)
  val client = new NingWSClient(new AsyncHttpClientConfig.Builder().build())


  def getIncomeData(apiKey: String)(implicit actorSystem: ActorSystem, executionContext: ExecutionContext): Future[List[Income]] = {
    val downloadActor = actorSystem.actorOf(DownloadActor.props(client), name = s"DownloadActor-income-${System.currentTimeMillis()}")
    val parseActor = actorSystem.actorOf(Props[IncomeParseActor], name = s"ParseActor-income-${System.currentTimeMillis()}")
    val url = new URL(s"$incomeUri?apikey=$apiKey")

    downloadActor ? url flatMap {
      case result: Result[Elem] => {
        client.close()
        if (result.successful) parseActor ? result.option.get flatMap {
          case result: Result[List[Income]] => Future.successful(result.option.get)
          case _ => Future.failed(new Exception("Could not parse data."))
        }
        else Future.failed(new Exception("Could not download data."))
      }
      case _ => Future.failed(new Exception("Unknown error."))
    }
  }


  def getPopulation(apiKey: String)(implicit actorSystem: ActorSystem, executionContext: ExecutionContext): Future[List[Population]] = {
    val downloadActor = actorSystem.actorOf(DownloadActor.props(client), name = s"DownloadActor-population-${System.currentTimeMillis()}")
    val parseActor = actorSystem.actorOf(Props[PopulationParseActor], name = s"ParseActor-population-${System.currentTimeMillis()}")
    val url = new URL(s"$populationUri?apikey=$apiKey")

    downloadActor ? url flatMap {
      case result: Result[Elem] => {
        client.close()
        if (result.successful) parseActor ? result.option.get flatMap {
          case result: Result[List[Population]] => Future.successful(result.option.get)
          case _ => Future.failed(new Exception("Could not parse data."))
        }
        else Future.failed(new Exception("Could not download data."))
      }
      case _ => Future.failed(new Exception("Unknown error."))
    }
  }
}
