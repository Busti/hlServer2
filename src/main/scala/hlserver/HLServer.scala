package hlserver

import java.net.{DatagramPacket, DatagramSocket, InetAddress}

import com.github.nscala_time.time.Imports._
import hlserver.effect.Effects
import hlserver.util.{OscService, Strip}
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
    //Strip(InetAddress.getByName("10.0.0.255"), 42, 4),
    Strip(InetAddress.getByName("10.0.0.160"), 42, 4),
    Strip(InetAddress.getByName("10.0.0.236"), 42, 4),
    Strip(InetAddress.getByName("10.0.0.104"), 42, 4),
    Strip(InetAddress.getByName("10.0.0.143"), 42, 4),
    Strip(InetAddress.getByName("10.0.0.230"), 42, 4),
    Strip(InetAddress.getByName("10.0.0.154"), 42, 4),
    Strip(InetAddress.getByName("10.0.0.121"), 42, 4),
    Strip(InetAddress.getByName("10.0.0.231"), 42, 4)
  )

  //A variable that holds the current effect
  var effect = Effects.create("rainbow")

  //Start the effect selection service
  BlazeBuilder.bindHttp(8080, "0.0.0.0").mountService(HttpService {
    case GET -> Root / "effect" / name :? params => {
      effect = Effects.create(name)
      Ok(s"Effect $name started.")
    }
  }, "/").run

  //Instantiate the osc service
  var osc = new OscService

  //The main loop
  var seq: Byte = 0
  while (true) {
    val in = scala.io.StdIn.readLine()
    val split = in.split(" ").map(_.toInt)

    val strip = strips(split(0))
    var data = Array[Byte](seq, 0, 0, 0)
    for (i <- 1 to strip.length) {
      data :+= split(2).toByte
      data :+= split(1).toByte
      data :+= split(3).toByte
      data :+= split(4).toByte
    }

    write(strip.address, data)

//    val now = DateTime.now
//    var map = Map[Strip, Array[Byte]]()
//    for (strip <- strips) {
//      val data = (0 until strip.length).flatMap(i => strip.color2array(effect.render(i, strip, now, osc))).toArray
//      map += strip -> data
//    }
//
//    for ((k, v) <- map) {
//      write(k.address, seq +: v)
//    }
//
    seq += 1
    Thread.sleep(50)
  }

  implicit def toByte(x: Int): Byte = x.toByte
}
