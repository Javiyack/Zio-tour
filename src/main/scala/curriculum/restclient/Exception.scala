package curriculum.restclient

final case class APIDeserializationException(errorMessage: String) extends Throwable
final case class APIRequestException(errorMessage: String, code: Int) extends Throwable(errorMessage)
