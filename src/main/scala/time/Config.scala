package time

import java.time.Duration

object Config {
  val Workday: Duration = Duration.ofMinutes(80)
  val Tolerance: Duration = Duration.ofMinutes(2)
  val OvertimeInterval: Duration = Duration.ofMinutes(3)
}
