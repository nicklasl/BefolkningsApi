package nu.nldv.befolkning.actors

import java.io.File
import java.util.concurrent.TimeUnit

import akka.testkit.TestActorRef
import akka.pattern.ask
import akka.util.Timeout
import nu.nldv.befolkning.model.{Income, Result}
import org.specs2.mutable.Specification

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.xml.{Elem, XML}

class IncomeParseActorSpec extends Specification {

  implicit val system = akka.actor.ActorSystem("system")

  val actorRef = TestActorRef[IncomeParseActor]

  implicit val timeout = Timeout(2000L, TimeUnit.MILLISECONDS)

  lazy val incomeFile: Elem = XML.loadFile(new File("src/test/resources/income.xml"))

  "IncomeParseActor" should {


    "parse the xml" in {
      val future: Future[Any] = actorRef ? incomeFile

      val awaited = Await.result(future, Duration(2, TimeUnit.SECONDS))
      awaited must beAnInstanceOf[Result[List[Income]]]
      val result = awaited.asInstanceOf[Result[List[Income]]]
      val tenth = result.option.get(10)
      result.successful must beTrue
      tenth.id must beEqualTo(11)
      tenth.areaCode must beEqualTo("SDO01")
      tenth.areaName must beEqualTo("Rinkeby-Kista")
      tenth.ageGroupCode must beEqualTo(1)
      tenth.ageGroupName must beEqualTo("16-19 år")
      tenth.sexCode must beEqualTo(2)
      tenth.sexName must beEqualTo("kvinnor")
      tenth.zeroEarners must beEqualTo(642)
      tenth.earners must beEqualTo(1176)
      tenth.totalIncomeForArea must beEqualTo(17276550)
      tenth.year must beEqualTo(2007)
      tenth.averageIncome must beEqualTo(14690)
      tenth.averageIncomeWithoutZeroEarners must beEqualTo(26910)
    }
  }

}
