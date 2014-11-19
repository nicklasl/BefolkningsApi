package nu.nldv.befolkning.actors

import java.net.URL
import java.util.concurrent.TimeUnit

import akka.pattern.ask
import akka.testkit.TestActorRef
import akka.util.Timeout
import com.ning.http.client.AsyncHttpClientConfig
import nu.nldv.befolkning.model.Result
import org.specs2.mutable.Specification
import play.api.libs.ws.ning.NingWSClient

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.xml.Elem

class DownloadActorSpec extends Specification {

  implicit val system = akka.actor.ActorSystem("system")
  implicit val timeout = Timeout(2000L, TimeUnit.MILLISECONDS)

  val client: NingWSClient = new NingWSClient(new AsyncHttpClientConfig.Builder().build())
  val actorRef = TestActorRef(new DownloadActor(client))
  val testUrl = "http://www.w3schools.com/xml/note.xml"
  val correct = "<note><to>Tove</to><from>Jani</from><heading>Reminder</heading><body>Don't forget me this weekend!</body></note>"
  "DownloadActor" should {

    "Download a XML file" in {
      val future: Future[Any] = actorRef ? new URL(testUrl)
      val awaited = Await.result(future, Duration(2, TimeUnit.SECONDS))
      awaited must beAnInstanceOf[Result[Elem]]
      val result = awaited.asInstanceOf[Result[Elem]]
      val str = result.option.get.toString().replaceAll("\n", "").replaceAll("\t", "")
      str must beEqualTo(correct)
    }
  }

}
