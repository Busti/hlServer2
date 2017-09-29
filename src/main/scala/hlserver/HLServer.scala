package hlserver

import java.net.{ DatagramSocket, DatagramPacket, InetAddress }
import java.lang.Thread

import com.github.nscala_time.time.Imports._

import org.http4s._, org.http4s.dsl._
import org.http4s.server.blaze._
import org.http4s.server.syntax._

import hlserver.util.Strip
import hlserver.util.Color

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
        .flatMap(effect.render(_, strip, now))
        .toArray
      map += strip -> data
    }

    for ((k, v) <- map) {
      write(k.address, v)
    }

    Thread.sleep(15)
  }
}
