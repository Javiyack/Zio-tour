package pastperformance.restclient

import org.apache.commons.lang3.builder.ToStringBuilder
import io.circe.syntax._
import io.circe.Printer
import io.circe.generic.auto._

import java.time.ZonedDateTime

object RequestBodyModel {

  object DREventRequest {
    type DREventRequestBody = List[DREvent]

    def toJson(body: DREventRequestBody): String =
      Printer.noSpaces.copy(dropNullValues = true).print(body.asJson)

    case class DREventRequest(body: DREventRequestBody)

    case class DREvent(
                             dr_source_event_id: String,
                             dr_event_name: String,
                             program_id: String,
                             program_name: String,
                             event_start_dttm: ZonedDateTime,
                             event_end_dttm: ZonedDateTime,
                             event_type: String,
                             notified_dttm: ZonedDateTime,
                             dispatched_dttm: ZonedDateTime,
                             version_dttm: ZonedDateTime
                           )
  }

  object DRSitePerformanceRequest {

    type DRSitePerformanceRequestBody = List[DRSitePerformance]
    case class DRSitePerformanceRequest(body: DRSitePerformanceRequestBody)

    case class DRSitePerformance(
                                       dr_source_event_id: String, //dr_source_site_id
                                       dr_source_site_id: String, //dr_source_event_id
                                       perf_good_dq: Boolean, //perf_good_dq
                                       perf_final: Boolean, //perf_final
                                       perf_enrollment_kw: Double, //perf_enrollment_kw
                                       perf_summary_kw: Option[Boolean],
                                       summary_only: Boolean //summary_only
                                     )

    override def toString: String = ToStringBuilder.reflectionToString(this)

    def toJson(body: DRSitePerformanceRequestBody): String =
      Printer.noSpaces.copy(dropNullValues = true).print(body.asJson)
  }

  object DRSiteIntervalsRequest {
    type DRSiteIntervalsRequestBody = List[DRSiteIntervals]
    case class DRSiteIntervalsRequests(body: DRSiteIntervalsRequestBody)

    case class DRSiteIntervals(
                                     dr_source_event_id: String,
                                     dr_source_site_id: String,
                                     interval_dttm: ZonedDateTime,
                                     demand_value: Double,
                                     baseline_value: Double,
                                     nomination_value: Double,
                                     dispatch_target_value: Double
                                   )

    override def toString: String = ToStringBuilder.reflectionToString(this)

    def toJson(body: DRSiteIntervalsRequestBody): String =
      Printer.noSpaces.copy(dropNullValues = true).print(body.asJson)
  }
}
