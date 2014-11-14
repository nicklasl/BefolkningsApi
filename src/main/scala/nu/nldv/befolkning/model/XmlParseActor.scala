package nu.nldv.befolkning.model

import akka.actor.Actor
import akka.actor.Actor.Receive

import scala.xml.{NodeSeq, Elem}

class XmlParseActor extends Actor {
  override def receive: Receive = {
    case xml: Elem => {
      val mainSender = sender()

      val incomeSeq: NodeSeq = xml \\ "medelinkomst"
      val list = incomeSeq.map {
        node =>
          Income(
            (node \ "ID").text.toInt,
            (node \ "AREA_CODE").text,
            (node \ "AREA_TEXT").text,
            (node \ "ALDI4K_CODE").text.toInt,
            (node \ "ALDI4K_TEXT").text,
            (node \ "KONK_CODE").text.toInt,
            (node \ "KONK_TEXT").text,
            (node \ "INKOM2_ANTAL").text.toInt,
            (node \ "INKOM_ANTAL").text.toInt,
            BigInt((node \ "INKOMST").text),
            (node \ "YEAR").text.toInt
          )
      }.toList
      mainSender ! Result[List[Income]](Some(list))
    }
  }
}
