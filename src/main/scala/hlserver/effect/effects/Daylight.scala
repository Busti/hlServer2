package hlserver.effect.effects

import com.github.nscala_time.time.Imports
import hlserver.effect.Effect
import hlserver.util.{Color, Strip}

/**
  * Created by gast1 on 30.09.17.
  */
class Daylight(brightness: Float = 1f) extends Effect {
  override def render(index: Int, strip: Strip, time: Imports.DateTime): Color = Color(0, 0, 0, 1)
}
