package curriculum

import zio._

import java.util.UUID

package object config {
  type Config = Has[Config.APIConf]

  object Config {
    val live: ZLayer[Any, Nothing, Config] = ZLayer.succeed(
      Config.APIConf(
        apiHost = "http://localhost:4000/POSTGRE",
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
