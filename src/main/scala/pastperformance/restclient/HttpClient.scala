package pastperformance.restclient

import zio.{Has, ZIO, ZLayer}
import io.circe.parser._
import io.circe.generic.auto._
import pastperformance.config.Config
import pastperformance.operations.ApiClientService
import pastperformance.restclient.ResponseBodyModel.DREventResponse.DREventResponse
import pastperformance.restclient.ResponseBodyModel.DRSiteIntervalsResponse.DRSiteIntervalsResponse
import pastperformance.restclient.ResponseBodyModel.DRSitePerformanceResponse.DRSitePerformanceResponse
import pastperformance.restclient.RequestBodyModel.DREventRequest.DREventRequestBody
import pastperformance.restclient.RequestBodyModel.{DREventRequest, DRSiteIntervalsRequest, DRSitePerformanceRequest}
import pastperformance.restclient.RequestBodyModel.DRSiteIntervalsRequest.DRSiteIntervalsRequestBody
import pastperformance.restclient.RequestBodyModel.DRSitePerformanceRequest.DRSitePerformanceRequestBody
import sttp.client3._
import sttp.client3.asynchttpclient.zio.SttpClient
import sttp.model.{Header, MediaType}

object PastPerformanceClient {

  val live: ZLayer[Config with Has[SttpClient.Service], Nothing, ApiClientService] =
    ZLayer.fromServices[Config.APIConf, SttpClient.Service, PastPerformanceClient.Service] { (config, sttpClient) =>
      println(s"APIConfig: $config")
      println(s"SttpClient: $sttpClient")

      new Service {
        override def postDrEvent(
                                  body: DREventRequestBody
        ): ZIO[ApiClientService, Throwable, DREventResponse] =
          for {
            _        <- ZIO.effect(println(s"POST /dr_event for event_id=${body.mkString(",")}"))
            response <- basicRequest
                          .post(uri"${config.apiHost}/dr_event")
                          .body(DREventRequest.toJson(body))
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
                                  PastPerformanceAPIRequestException(errorMessage, response.code.code)
                                ),
                              json =>
                                ZIO.fromEither(
                                  decode[DREventResponse](json).left
                                    .map(circeError =>
                                      PastPerformanceAPIDeserializationException(circeError.getMessage)
                                    )
                                )
                            )
                          }
          } yield response

        override def postDrSitePerformance(
          payload: DRSitePerformanceRequestBody
        ): ZIO[Any, Throwable, DRSitePerformanceResponse] =
          for {
            _        <- ZIO.effect(println(s"POST /dr_site_performance for event_id=${payload.mkString(",")}"))
            response <- basicRequest
                          .post(uri"${config.apiHost}/dr_site_performance")
                          .body(DRSitePerformanceRequest.toJson(payload))
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
                                  PastPerformanceAPIRequestException(
                                    errorMessage,
                                    response.code.code
                                  )
                                ),
                              json =>
                                ZIO.fromEither(
                                  decode[DRSitePerformanceResponse](json).left
                                    .map(circeError =>
                                      PastPerformanceAPIDeserializationException(
                                        circeError.getMessage
                                      )
                                    )
                                )
                            )
                          }
          } yield response

        override def postDrSiteIntervals(
          payload: DRSiteIntervalsRequestBody
        ): ZIO[Any, Throwable, DRSiteIntervalsResponse] =
          for {
            _        <- ZIO.effect(println(s"POST /dr_site_intervals. Payload=${payload.mkString(",")}"))
            response <- basicRequest
                          .post(uri"${config.apiHost}/dr_site_intervals")
                          .body(DRSiteIntervalsRequest.toJson(payload))
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
                                  PastPerformanceAPIRequestException(
                                    errorMessage,
                                    response.code.code
                                  )
                                ),
                              json =>
                                ZIO.fromEither(
                                  decode[DRSiteIntervalsResponse](json).left
                                    .map(circeError =>
                                      PastPerformanceAPIDeserializationException(
                                        circeError.getMessage
                                      )
                                    )
                                )
                            )
                          }
          } yield response
      }
    }

  trait Service {

    def postDrEvent(
      traceablePayload: DREventRequestBody
    ): ZIO[ApiClientService, Throwable, DREventResponse]

    def postDrSitePerformance(
      payload: DRSitePerformanceRequestBody
    ): ZIO[ApiClientService, Throwable, DRSitePerformanceResponse]

    def postDrSiteIntervals(
      payload: DRSiteIntervalsRequestBody
    ): ZIO[ApiClientService, Throwable, DRSiteIntervalsResponse]

  }
}
