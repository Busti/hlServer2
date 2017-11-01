package hlserver.effect.effects

import com.github.nscala_time.time.Imports
import hlserver.effect.Effect
import hlserver.util.{Color, OscService, Strip}

class RandomBlink extends Effect {
  override def render(index: Int, strip: Strip, time: Imports.DateTime, osc: OscService): Color = {
    Color(0, 0, 0, if (Math.random() < 0.1) 1f else 0f)
  }
}
