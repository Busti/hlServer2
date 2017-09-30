package hlserver

import java.net.{DatagramPacket, DatagramSocket, InetAddress}

import com.github.nscala_time.time.Imports._
import hlserver.effect.Effects
import hlserver.util.{Color, Strip}
import org.http4s._
import org.http4s.dsl._
import org.http4s.server.blaze._

object HLServer extends App {
  println("Starting HLServer")

  //Setup sending UDP Socket
  val socket = new DatagramSocket()

  def write(address: InetAddress, data: Array[Byte]) {
    val packet = new DatagramPacket(data, data.length, address, 1234)
    socket.send(packet)
  }

  //Setup connected Strips
  val strips = List(
    Strip(InetAddress.getByName("10.0.0.204"), 45, 3)
  )

  //A variable that holds the current effect
  var effect = Effects.create("off")

  //Start the effect selection service
  BlazeBuilder.bindHttp(8080, "0.0.0.0").mountService(
    HttpService {
      case GET -> Root / "effect" / name => {
        effect = Effects.create(name)
        Ok(s"Effect $name started.")
      }
    }, "/"
  ).run

  //The main loop
  while (true) {
    val now = DateTime.now
    var map = Map[Strip, Array[Byte]]()
    for (strip <- strips) {
      val data = (0 until strip.length)
        .flatMap(i => {
          val (r, g, b) = Color.color2t_rgb(effect.render(i, strip, now).toRGB)
          Array[Byte](g, b, r)
        }).toArray
      map += strip -> data
    }

    for ((k, v) <- map) {
      write(k.address, v)
    }

    Thread.sleep(15)
  }
}
