package hlserver.util

import java.net.InetAddress

case class Strip(address: InetAddress, length: Int, bytes: Int) {
  def color2array(color: Color): Array[Byte] = {
    if (bytes == 3) {
      color.toArrayGRB
    } else {
      color.toArrayGRBW
    }
  }
}