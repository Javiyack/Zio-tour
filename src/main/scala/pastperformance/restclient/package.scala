package pastperformance

import pastperformance.restclient.PastPerformanceClient
import pastperformance.restclient.ResponseBodyModel.DREventResponse.DREventResponse
import pastperformance.restclient.ResponseBodyModel.DRSiteIntervalsResponse.DRSiteIntervalsResponse
import pastperformance.restclient.ResponseBodyModel.DRSitePerformanceResponse.DRSitePerformanceResponse
import pastperformance.restclient.RequestBodyModel.DREventRequest.DREventRequestBody
import pastperformance.restclient.RequestBodyModel.DRSiteIntervalsRequest.DRSiteIntervalsRequestBody
import pastperformance.restclient.RequestBodyModel.DRSitePerformanceRequest.DRSitePerformanceRequestBody
import zio.{Has, ZIO}

package object operations {
  type ApiClientService = Has[PastPerformanceClient.Service]

  def postDrEvent(
    payload: DREventRequestBody
  ): ZIO[ApiClientService, Throwable, DREventResponse] =
    ZIO.accessM(_.get.postDrEvent(payload))

  def postDrSitePerformance(
    payload: DRSitePerformanceRequestBody
  ): ZIO[ApiClientService, Throwable, DRSitePerformanceResponse] =
    ZIO.accessM(_.get.postDrSitePerformance(payload))

  def postDrSiteIntervals(
    payload: DRSiteIntervalsRequestBody
  ): ZIO[ApiClientService, Throwable, DRSiteIntervalsResponse] =
    ZIO.accessM(_.get.postDrSiteIntervals(payload))

}
