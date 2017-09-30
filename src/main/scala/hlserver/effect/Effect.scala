package hlserver.effect

import hlserver.util.{Color, Strip}
import org.joda.time.DateTime

trait Effect {
  def render(index: Int, strip: Strip, time: DateTime): Color
}