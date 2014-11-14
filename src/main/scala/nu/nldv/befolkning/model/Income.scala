package nu.nldv.befolkning.model

case class Income(id: Int,
                  areaCode: String,
                  areaName: String,
                  ageGroupCode: Int,
                  ageGroupName: String,
                  sexCode: Int,
                  sexName: String,
                  zeroEarners: Int,
                  earners: Int,
                  totalIncomeForArea: BigInt,
                  year: Int) {
  val averageIncome = totalIncomeForArea / earners
  val averageIncomeWithoutZeroEarners = totalIncomeForArea / zeroEarners
}
