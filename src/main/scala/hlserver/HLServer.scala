package hlserver

import java.net.{DatagramPacket, DatagramSocket, InetAddress}

import com.github.nscala_time.time.Imports._
import hlserver.effect.Effects
import hlserver.util.Strip
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
    Strip(InetAddress.getByName("10.0.0.152"), 42, 4),
    Strip(InetAddress.getByName("10.0.0.203"), 39, 3),
    Strip(InetAddress.getByName("10.0.0.196"), 42, 4)
  )

  //A variable that holds the current effect
  var effect = Effects.create("off")

  //Start the effect selection service
  BlazeBuilder.bindHttp(8080, "0.0.0.0").mountService(HttpService {
    case GET -> Root / "effect" / name :? params => {
      effect = Effects.create(name)
      Ok(s"{\"name\":\"$name\",\"started\":true}")
    }
  }, "/").run

  //The main loop
  while (true) {
    val now = DateTime.now
    var map = Map[Strip, Array[Byte]]()
    for (strip <- strips) {
      val data = (0 until strip.length).flatMap(i => strip.color2array(effect.render(i, strip, now))).toArray
      map += strip -> data
    }

    for ((k, v) <- map) {
      write(k.address, v)
    }

    Thread.sleep(15)
  }
}
