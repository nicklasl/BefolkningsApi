package nu.nldv.befolkning.model

case class Population(id: Int,
                      areaCode: String,
                      areaName: String,
                      ageCode: Int,
                      ageName: String,
                      sexCode: Int,
                      sexName: String,
                      populationNumber: Int,
                      year: Int) {

}
