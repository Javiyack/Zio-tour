package pastperformance
import zio._

import java.util.UUID

package object config {
  type Config = Has[Config.APIConf]

  object Config {
    val live: ZLayer[Any, Nothing, Config] = ZLayer.succeed(
      Config.APIConf(
        apiHost = "API_HOST",
        sessionToken = "sessionToken",
        xEnelxTraceId = UUID.randomUUID().toString
      )
    )

    final case class APIConf(
      apiHost: String,
      sessionToken: String,
      xEnelxTraceId: String
    )
  }

}
