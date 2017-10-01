package hlserver.effect

import hlserver.effect.effects.{Daylight, Rainbow, RandomBlink}
import hlserver.util.{Color, Strip}
import org.joda.time.DateTime

object Effects {
  def create(s: String): Effect = s match {
    case "rainbow" => new Rainbow
    case "daylight" => new Daylight
    case "randomblink" => new RandomBlink
    case _ => (_: Int, _: Strip, _: DateTime) => Color(0, 0, 0)
  }
}
