package pastperformance.restclient

import pastperformance.restclient.RequestBodyModel.DRSiteIntervalsRequest.DRSiteIntervals
import java.time.ZonedDateTime

object ResponseBodyModel {

  object DREventResponse {
    final case class DREventResponse(code: Int, message: String, data: Data)

    final case class Data(
      statistics: Statistics,
      rejected: List[Rejected],
      processed: List[DREventModel]
    )

    final case class Statistics(accepted_nbr: Int, rejected_nbr: Int)

    final case class Rejected(
                               json: DREventModel,
                               reason: String
    )

    final case class DREventModel(
      created_by: String,
      notified_dttm: ZonedDateTime,
      last_updated_dttm: ZonedDateTime,
      program_id: String,
      dispatched_dttm: ZonedDateTime,
      event_start_dttm: ZonedDateTime,
      event_name: String,
      event_type: String,
      last_updated_by: String,
      event_end_dttm: ZonedDateTime,
      program_name: String,
      created_dttm: ZonedDateTime,
      event_id: String,
      version_dttm: ZonedDateTime
    )
  }

  object DRSitePerformanceResponse {

    final case class DRSitePerformanceResponse(code: Int, message: String, data: Data)

    final case class Data(
      statistics: Statistics,
      rejected: List[Rejected],
      processed: List[DRSitePerformanceModel]
    )

    final case class Statistics(accepted_nbr: Int, rejected_nbr: Int)

    final case class Rejected(
                               json: DRSitePerformanceModel,
                               reason: String
    )

    final case class DRSitePerformanceModel(
      event_enrollment_id: String, // ID of the Site Enrollment in the Dispatch Event in the Past Performance System
      dr_source_event_id: String,  //    ID of the Dispatch Event in the originating system
      event_id: String,            //
      site_id: String,             //    ID of the Site in Atlas
      dr_source_site_id: String,   //    ID of the Site in the originating system
      perf_good_dq: Boolean,       //
      perf_final: Boolean,         //
      performance_kw: Double,      //
      enrollment_kw: Double,       //
      summary_only: Boolean        //
    )
  }

  object DRSiteIntervalsResponse {

    final case class DRSiteIntervalsResponse(code: Int, message: String, data: Data)

    final case class Data(
      statistics: Statistics,
      rejected: List[Rejected],
      processed: List[DRSiteIntervalModel]
    )

    final case class Statistics(accepted_nbr: Int, rejected_nbr: Int)

    final case class Rejected(
                               json: DRSiteIntervals,
                               reason: String
    )

    final case class DRSiteIntervalModel(
      created_by: String,
      notified_dttm: ZonedDateTime,
      last_updated_dttm: ZonedDateTime,
      program_id: String,
      dispatched_dttm: ZonedDateTime,
      event_start_dttm: ZonedDateTime,
      event_name: String,
      event_type: String,
      last_updated_by: String,
      event_end_dttm: ZonedDateTime,
      program_name: String,
      created_dttm: ZonedDateTime,
      event_id: String,
      version_dttm: ZonedDateTime
    )
  }
}
