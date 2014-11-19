package nu.nldv.befolkning.actors

import java.net.URL

import akka.actor.{Props, Actor}
import nu.nldv.befolkning.model.Result
import play.api.libs.ws.WSResponse
import play.api.libs.ws.ning.NingWSClient
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.xml.Elem

class DownloadActor(client: NingWSClient) extends Actor {

  override def receive: Receive = {
    case url: URL => {
      val mainSender = sender()
      val eventualResponse: Future[WSResponse] = client.url(url.toString).get()
      eventualResponse.map { r =>
        r.
        mainSender ! Result[Elem](Some(r.xml))
      }
    }
  }
}

object DownloadActor {
  def props(client: NingWSClient) = Props(new DownloadActor(client))
}