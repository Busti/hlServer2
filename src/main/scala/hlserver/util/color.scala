package hlserver.util

import scala.language.implicitConversions

trait Color {
  def toRGB:  ColorRGB
  def toRGBW: ColorRGBW
  def toHSV:  ColorHSV
  def toArrayGBR: Array[Byte] = {
    val rgb = toRGB
    val (r, g, b) = ((rgb.red * 255).toByte, (rgb.green * 255).toByte, (rgb.blue * 255).toByte)
    Array[Byte](g, r, b)
  }

  def toArrayGBRW: Array[Byte] = {
    val rgb = toRGBW
    val (r, g, b, w) = ((rgb.red * 255).toByte, (rgb.green * 255).toByte, (rgb.blue * 255).toByte, (rgb.white * 255).toByte)
    Array[Byte](g, r, b, w)
  }
}

object Color {
  def apply(red: Float, green: Float, blue: Float): ColorRGB = ColorRGB(red, green, blue)
  def apply(red: Float, green: Float, blue: Float, white: Float): ColorRGBW = ColorRGBW(red, green, blue, white)

  implicit def color2rgb(color: Color): ColorRGB = color.toRGB
  implicit def color2rgbw(color: Color): ColorRGBW = color.toRGBW
  implicit def color2hsl(color: Color): ColorHSV = color.toHSV
}

case class ColorRGB(red: Float, green: Float, blue: Float) extends Color {
  require(0f <= red   && red   <= 1f, "Red component is invalid with value: " + red)
  require(0f <= green && green <= 1f, "Green component is invalid with value: " + green)
  require(0f <= blue  && blue  <= 1f, "Blue component is invalid with value: " + blue)

  override def toHSV = {

    val max = Set(red, green, blue).max
    val min = Set(red, green, blue).min

    val d = max - min
    val s = if (max == 0) 0 else d / max
    val v = max

    val h = if (max == min) {
      // achromatic
      0f
    } else {
      val h = max match {
        case `red`   => (green - blue)  / d + (if (green < blue) 6 else 0)
        case `green` => (blue  - red)   / d + 2;
        case `blue`  => (red   - green) / d + 4;
      }
      h / 6f
    }

    ColorHSV(FPUtil.cap(h), FPUtil.cap(s), FPUtil.cap(v))
  }

  override def toRGBW = ColorRGBW(red, green, blue, 0f)

  override def toRGB = this
}

case class ColorRGBW(red: Float, green: Float, blue: Float, white: Float) extends Color {
  require(0f <= red   && red   <= 1f, "Red component is invalid with value: " + red)
  require(0f <= green && green <= 1f, "Green component is invalid with value: " + green)
  require(0f <= blue  && blue  <= 1f, "Blue component is invalid with value: " + blue)
  require(0f <= white && white <= 1f, "White component is invalid with value: " + white)

  override def toHSV = {

    val max = Set(red, green, blue).max
    val min = Set(red, green, blue).min

    val d = max - min
    val s = Set(if (max == 0) 0 else d / max, 1f - white).min
    val v = max

    val h = if (max == min) {
      // achromatic
      0f
    } else {
      val h = max match {
        case `red`   => (green - blue)  / d + (if (green < blue) 6 else 0)
        case `green` => (blue  - red)   / d + 2;
        case `blue`  => (red   - green) / d + 4;
      }
      h / 6f
    }

    ColorHSV(FPUtil.cap(h), FPUtil.cap(s), FPUtil.cap(v))
  }

  override def toRGB = ColorRGB(red, green, blue)

  override def toRGBW = this
}

case class ColorHSV(hue: Float, saturation: Float, value: Float) extends Color {
  require(0f <= hue        && hue        <= 1f, "Hue component is invalid with value: " + hue)
  require(0f <= saturation && saturation <= 1f, "Saturation component is invalid with value: " + saturation)
  require(0f <= value      && value      <= 1f, "Value component is invalid  with value: " + value)

  override def toHSV = this

  override def toRGB = {
    val h = hue
    val s = saturation
    val v = value

    val i: Float = Math.floor(h * 6f).toFloat
    val f: Float = h * 6f - i
    val p: Float = v * (1 -           s)
    val q: Float = v * (1 -      f  * s)
    val t: Float = v * (1 - (1 - f) * s)

    val (r, g, b) = i % 6 match {
      case 0 => (v, t, p)
      case 1 => (q, v, p)
      case 2 => (p, v, t)
      case 3 => (p, q, v)
      case 4 => (t, p, v)
      case 5 => (v, p, q)
      case _ => throw new RuntimeException(s"Cannot convert from HSV to RGB ($this)")
    }

    ColorRGB(FPUtil.cap(r), FPUtil.cap(g), FPUtil.cap(b))
  }

  override def toRGBW = {
    val rgb = toRGB
    ColorRGBW(rgb.red, rgb.green, rgb.blue, 0)
  }
}