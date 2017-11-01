package hlserver.effect.effects

import hlserver.effect.Effect
import hlserver.util.{Color, ColorHSV, OscService, Strip}
import com.github.nscala_time.time.Imports._

class Rainbow extends Effect {
  override def render(index: Int, strip: Strip, time: DateTime, osc: OscService): Color = {
    ColorHSV((index.toFloat / strip.length.toFloat + time / 5) % 1, 1f, 1f)
  }
}