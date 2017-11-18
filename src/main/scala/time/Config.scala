package time

import java.time.Duration

object Config {
  val Workday: Duration = Duration.ofHours(8)
  val Tolerance: Duration = Duration.ofMinutes(10)
  val OvertimeInterval: Duration = Duration.ofMinutes(30)
}
