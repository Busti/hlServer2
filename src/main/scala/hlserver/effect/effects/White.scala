package hlserver.effect.effects

import com.github.nscala_time.time.Imports
import hlserver.effect.Effect
import hlserver.util.{Color, OscService, Strip}

/**
  * Created by gast1 on 30.09.17.
  */
class White(brightness: Float = 1f) extends Effect {
  override def render(index: Int, strip: Strip, time: Imports.DateTime, osc: OscService): Color = Color(1, 0, 1)//Color(1, 0.6f, 0.3f, 1)
}
