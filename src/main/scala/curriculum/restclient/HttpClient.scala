package curriculum.restclient

import curriculum.config.Config
import curriculum.operations.ApiClientService
import curriculum.restclient.RequestModel.UserRequest
import curriculum.restclient.RequestModel.UserRequest.UserRequestBody
import zio.{Has, ZIO, ZLayer}
import io.circe.parser._
import io.circe.generic.auto._
import curriculum.restclient.ResponseBodyModel.GetUsersResponse.{GetUsersResponse, GetUsersResponseBody}
import curriculum.restclient.ResponseBodyModel.PostUserResponse.PostUserResponse
import sttp.client3._
import sttp.client3.asynchttpclient.zio.SttpClient
import sttp.model.{Header, MediaType}
import zio.console.{Console, putStrLn}

object HttpClient {

  val live: ZLayer[Config with Has[SttpClient.Service], Nothing, ApiClientService] =
    ZLayer.fromServices[Config.APIConf, SttpClient.Service, HttpClient.Service] { (config, sttpClient) =>
      new Service {
        override def getUsers(): ZIO[ApiClientService, Throwable, GetUsersResponse] =

          for {
            _ <- ZIO.effect(println("-GET /users"))
            uri = uri"${config.apiHost}/users"
            response <- basicRequest
              .get(uri"${config.apiHost}/users")
              .headers(
                Header("enoc_session", config.sessionToken),
                Header.contentType(MediaType.ApplicationJson)
              )
              .response(asString)
              .send(sttpClient)
              .flatMap { response =>
                response.body.fold(
                  errorMessage =>
                    ZIO.fail(
                      APIRequestException(response.headers.map(e => s"${e.name} -> ${e.value}")
                        .appended(s"body is left: ${response.body.isLeft.toString}")
                        .appended(response.body.left.getOrElse("No left"))
                        .appended(s"body is right: ${response.body.isRight.toString}")
                        .appended(response.body.right.getOrElse("No right"))
                        .appended(s"$uri ${response.statusText}").mkString(","), response.code.code)
                    ),
                  json =>
                    ZIO.fromEither(
                      decode[GetUsersResponse](json).left
                        .map(circeError =>
                          APIDeserializationException(s"${circeError.getMessage} Failed to decode $json\n")
                        )
                    )
                )
              }
          } yield response

        override def postUser(
                               payload: UserRequestBody
                             ): ZIO[ApiClientService, Throwable, PostUserResponse] =
          for {
            _ <- ZIO.effectTotal(println(s"POST /user=${payload}"))
            uri = uri"${config.apiHost}/users"
            response <- basicRequest
              .post(uri)
              .body(UserRequest.toJson(payload))
              .headers(
                Header("enoc_session", config.sessionToken),
                Header.contentType(MediaType.ApplicationJson)
              )
              .response(asString)
              .send(sttpClient)
              .flatMap { response =>
                response.body.fold(
                  errorMessage =>
                    ZIO.fail(
                      APIRequestException(response.headers.map(e => s"${e.name} -> ${e.value}")
                        .appended(s"body is left: ${response.body.isLeft.toString}")
                        .appended(response.body.left.getOrElse("No left"))
                        .appended(s"body is right: ${response.body.isRight.toString}")
                        .appended(response.body.right.getOrElse("No right"))
                        .appended(s"$uri ${response.statusText}").mkString(","), response.code.code)
                    ),
                  json =>
                    ZIO.fromEither(
                      decode[PostUserResponse](json).left
                        .map(circeError =>
                          APIDeserializationException(s"${circeError.getMessage} Failed to decode $json\n")
                        )
                    )
                )
              }
          } yield response


        override def deleteUser(
                                 id: String
                               ): ZIO[ApiClientService, Throwable, PostUserResponse] =
          for {
            _ <- ZIO.effectTotal(println(s"DELETE /users/id/$id"))
            uri = uri"${config.apiHost}/users/id/$id"
            response <- basicRequest
              .delete(uri)
              .headers(
                Header("enoc_session", config.sessionToken),
                Header.contentType(MediaType.ApplicationJson)
              )
              .response(asString)
              .send(sttpClient)
              .flatMap { response =>
                response.body.fold(
                  errorMessage =>
                    ZIO.fail(
                      APIRequestException(response.headers.map(e => s"${e.name} -> ${e.value}")
                        .appended(s"body is left: ${response.body.isLeft.toString}")
                        .appended(response.body.left.getOrElse("No left"))
                        .appended(s"body is right: ${response.body.isRight.toString}")
                        .appended(response.body.right.getOrElse("No right"))
                        .appended(s"$uri ${response.statusText}").mkString(","), response.code.code)
                    ),
                  json =>
                    ZIO.fromEither(
                      decode[PostUserResponse](json).left
                        .map(circeError =>
                          APIDeserializationException(s"${circeError.getMessage} Failed to decode $json\n")
                        )
                    )
                )
              }
          } yield response

      }
    }

  trait Service {

    def getUsers(): ZIO[ApiClientService, Throwable, GetUsersResponse]

    def postUser(userPayload: UserRequestBody): ZIO[ApiClientService, Throwable, PostUserResponse]

    def deleteUser(id: String): ZIO[ApiClientService, Throwable, PostUserResponse]

  }
}
