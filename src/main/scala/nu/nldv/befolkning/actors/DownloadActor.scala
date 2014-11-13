package nu.nldv.befolkning.actors

import java.util.concurrent.TimeUnit

import akka.actor.{Props, Actor}
import nu.nldv.befolkning.model.ApiKey
import play.api.libs.ws.{WSResponse, WS}
import play.api.libs.ws.ning.NingWSClient
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class DownloadActor(client: NingWSClient) extends Actor {

  val baseUri = "http://data.stockholm.se/set/Befolkning/Medelinkomst/"

  def uri(apiKey: String) = s"$baseUri?apikey=$apiKey"



  override def receive: Receive = {
    case apiKey: ApiKey => {
      val mainSender = sender()
      val eventualResponse: Future[WSResponse] = client.url(uri(apiKey.key)).get()
      eventualResponse.map { r =>

        println(r.body)
        sender() ! "done"
      }
      Await.ready(eventualResponse, Duration(10, TimeUnit.SECONDS))
    }
  }
}

object DownloadActor {
  def props(client: NingWSClient) = Props(new DownloadActor(client))
}