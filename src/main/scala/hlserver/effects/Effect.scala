package hlserver.effects

import hlserver.util.{Color, Strip}
import org.joda.time.DateTime

trait Effect {
  def render(index: Int, strip: Strip, time: DateTime): Color
}

object Effect {
  def discover() {

  }
}

final class effect extends StaticAnnotation