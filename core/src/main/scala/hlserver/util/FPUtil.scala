package hlserver.util

object FPUtil {
  def cap(value: Float): Float = {
    if (value < 0) return 0
    if (value > 1) return 1
    value
  }
}
