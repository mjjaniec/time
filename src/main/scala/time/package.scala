import java.time.Duration

package object time {

  implicit class DurationEx(val self: Duration) extends AnyVal {
    def isShorter(other: Duration): Boolean = self.minus(other).isNegative
    def isLonger(other: Duration): Boolean = other.minus(self).isNegative
    def isEqual(other: Duration, tolerance: Duration): Boolean =
      self.abs().minus(other.abs()).abs().minus(tolerance.abs()).isNegative
  }
}
