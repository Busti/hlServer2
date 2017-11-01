package hlserver.effect.effects

import com.github.nscala_time.time.Imports
import hlserver.effect.Effect
import hlserver.util.{Color, OscService, Strip}

class Osc extends Effect {
  override def render(index: Int, strip: Strip, time: Imports.DateTime, osc: OscService): Color = {
    val r = osc("/2/fader1").asInstanceOf[Vector[Float]](0)
    val g = osc("/2/fader2").asInstanceOf[Vector[Float]](0)
    val b = osc("/2/fader3").asInstanceOf[Vector[Float]](0)
    val w = osc("/2/fader4").asInstanceOf[Vector[Float]](0)
    Color(r, g, b, w)
  }
}
