package nu.nldv.befolkning.model

case class Result[T](option: Option[T]) {
  def successful: Boolean = option.isDefined
}
