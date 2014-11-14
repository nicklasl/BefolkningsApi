package nu.nldv.befolkning.actors

import akka.actor.Actor
import nu.nldv.befolkning.model.{Income, Result}

import scala.xml.{Elem, NodeSeq}

class IncomeParseActor extends Actor {
  override def receive: Receive = {
    case xml: Elem => {
      val mainSender = sender()

      val incomeSeq: NodeSeq = xml \\ "medelinkomst"
      val list = incomeSeq.map {
        node =>
          Income(
            id = (node \ "ID").text.toInt,
            areaCode = (node \ "AREA_CODE").text,
            areaName = (node \ "AREA_TEXT").text,
            ageGroupCode = (node \ "ALDI4K_CODE").text.toInt,
            ageGroupName = (node \ "ALDI4K_TEXT").text,
            sexCode = (node \ "KONK_CODE").text.toInt,
            sexName = (node \ "KONK_TEXT").text,
            zeroEarners = (node \ "INKOM2_ANTAL").text.toInt,
            earners = (node \ "INKOM_ANTAL").text.toInt,
            totalIncomeForArea = BigInt((node \ "INKOMST").text),
            year = (node \ "YEAR").text.toInt
          )
      }.toList
      mainSender ! Result[List[Income]](Some(list))
    }
  }
}
