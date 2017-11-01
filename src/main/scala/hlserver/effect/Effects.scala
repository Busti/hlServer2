package hlserver.effect

import hlserver.effect.effects.{Osc, Rainbow, RandomBlink, White}
import hlserver.util.Color

object Effects {
  def create(s: String): Effect = s match {
    case "rainbow" => new Rainbow
    case "white" => new White
    case "randomblink" => new RandomBlink
    case "osc" => new Osc
    case _ => (_, _, _, _) => Color(0, 0, 0)
  }
}
