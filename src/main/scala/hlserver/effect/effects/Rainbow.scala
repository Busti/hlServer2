package hlserver.effect.effects

import hlserver.effect.Effect
import hlserver.util.{Color, ColorHSV, Strip}
import com.github.nscala_time.time.Imports._

class Rainbow extends Effect {
  override def render(index: Int, strip: Strip, time: DateTime): Color = {
    ColorHSV(index.toFloat / strip.length.toFloat, 1f, 1f)
  }
}