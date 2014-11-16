package nu.nldv.befolkning.actors

import akka.actor.Actor
import nu.nldv.befolkning.model.{Population, Result}

import scala.xml.{Elem, NodeSeq}

class PopulationParseActor extends Actor {

  override def receive: Receive = {
    case xml: Elem => {
      val mainSender = sender()

      val incomeSeq: NodeSeq = xml \\ "befolkning"
      val list = incomeSeq.map {
        node =>
          Population(
            id = (node \ "ID").text.toInt,
            areaCode = (node \ "AREA_CODE").text,
            areaName = (node \ "AREA_TEXT").text,
            ageCode = (node \ "ALD11K_CODE").text.toInt,
            ageName = (node \ "ALD11K_TEXT").text,
            sexCode = (node \ "KONK_CODE").text.toInt,
            sexName = (node \ "KONK_TEXT").text,
            populationNumber = (node \ "BEF_ANTAL").text.toInt,
            year = (node \ "YEAR").text.toInt
          )
      }.toList
      mainSender ! Result[List[Population]](Some(list))
    }
  }
}
