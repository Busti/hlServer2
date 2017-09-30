package hlserver.effect.effects

import hlserver.effect.Effect
import hlserver.util.{Color, ColorHSV, Strip}
import org.joda.time.DateTime

class Rainbow extends Effect {
  override def render(index: Int, strip: Strip, time: DateTime): Color = {
    ColorHSV(index / strip.length.toFloat, 1f, 1f)
  }
}