package hlserver.effect

import hlserver.util.{Color, OscService, Strip}
import com.github.nscala_time.time.Imports._

import scala.language.implicitConversions

trait Effect {
  def render(index: Int, strip: Strip, time: DateTime, osc: OscService): Color

  implicit def time2float(time: DateTime): Float = {
    time.getSecondOfDay.toFloat + (time.getMillisOfSecond.toFloat / 1000f)
  }
}