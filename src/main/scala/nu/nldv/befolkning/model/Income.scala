package nu.nldv.befolkning.model

case class Income(id: Int,
                  areaCode: String,
                  areaText: String,
                  aldi4kCode: Int,
                  aldi4kText: String,
                  konkCode: Int,
                  konkText: String,
                  inkom2Antal: Int,
                  inkomAntal: Int,
                  inkomst: BigInt,
                  year: Int)
