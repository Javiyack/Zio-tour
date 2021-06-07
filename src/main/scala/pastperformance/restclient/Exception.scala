package pastperformance.restclient

final case class PastPerformanceAPIDeserializationException(errorMessage: String) extends Throwable
final case class PastPerformanceAPIRequestException(errorMessage: String, httpCode: Int) extends Throwable(errorMessage)
