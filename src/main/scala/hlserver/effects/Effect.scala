package hlserver.effects

import hlserver.util.{Color, Strip}
import org.joda.time.DateTime

import scala.annotation.StaticAnnotation

import reflect.runtime.universe._

trait Effect {
  def render(index: Int, strip: Strip, time: DateTime): Color
}

object Effect {
  def discover() {
    typeOf[Effect].foreach(print(_))
  }
}

final class effect extends StaticAnnotation