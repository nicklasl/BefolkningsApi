Scala STHLM BefolkningsAPI
-----------

This is a Scala API for the Stockholm Open Data - Befolkningsdatabasen web API.
The Web API requires a generated API key, to generate that key:
- Visit: http://data.stockholm.se/ApiKey
- Generate a new key for "Tjänst: Befolkningsdatabasen".

Use the API like so:
```
implicit val apikey: String = "MY SECRET API KEY"

implicit val actorSystem = akka.actor.ActorSystem("ActorSystem")
implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

val eventualIncomes: Future[List[Income]] = Api.getIncomeData

eventualIncomes map {
  incomes =>
    val richPeople: List[Income] = incomes.sortBy(income => income.averageIncomeWithoutZeroEarners).reverse
}
```
