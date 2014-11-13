package nu.nldv.befolkning

import java.util.concurrent.TimeUnit

import akka.util.Timeout
import com.ning.http.client.AsyncHttpClientConfig
import nu.nldv.befolkning.actors.DownloadActor
import nu.nldv.befolkning.model.ApiKey
import play.api.libs.ws.ning.NingWSClient
import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.Source

object Application extends App {

  private val apikey: String = Source.fromFile("src/main/resources/apikey").mkString

  val actorSystem = akka.actor.ActorSystem("ActorSystem")
  implicit val timeout = Timeout(2, TimeUnit.MINUTES)
  val client = new NingWSClient(new AsyncHttpClientConfig.Builder().build())
  val downloadActor = actorSystem.actorOf(DownloadActor.props(client), name = s"DownloadActor")

  downloadActor ? ApiKey(apikey) map {
    res => {
      println(res)
      client.close()
      actorSystem.shutdown()
    }
  }

}
