package nu.nldv.befolkning.actors

import java.io.File
import java.util.concurrent.TimeUnit

import akka.pattern.ask
import akka.testkit.TestActorRef
import akka.util.Timeout
import nu.nldv.befolkning.model.{Population, Result}
import org.specs2.mutable.Specification

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.xml.{XML, Elem}

class PopulationParseActorSpec extends Specification {
  implicit val system = akka.actor.ActorSystem("system")

  val actorRef = TestActorRef[PopulationParseActor]

  implicit val timeout = Timeout(2000L, TimeUnit.MILLISECONDS)

  lazy val populationFile: Elem = XML.loadFile(new File("src/test/resources/population.xml"))

  "PopulationParseActor" should {


    "parse the xml" in {
      val future: Future[Any] = actorRef ? populationFile
      val awaited = Await.result(future, Duration(2, TimeUnit.SECONDS))
      awaited must beAnInstanceOf[Result[List[Population]]]
      val result = awaited.asInstanceOf[Result[List[Population]]]
      val tenth = result.option.get(10)

      result.successful must beTrue
      tenth.id must beEqualTo(1604)
      tenth.areaCode must beEqualTo("SDO03")
      tenth.areaName must beEqualTo("Spånga-Tensta")
      tenth.ageCode must beEqualTo(35)
      tenth.ageName must beEqualTo("34 år")
      tenth.sexCode must beEqualTo(1)
      tenth.sexName must beEqualTo("män")
      tenth.populationNumber must beEqualTo(245)
      tenth.year must beEqualTo(2010)
    }
  }

}
