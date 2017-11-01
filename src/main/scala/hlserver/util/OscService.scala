package hlserver.util

import de.sciss.osc.{Message, UDP}
import scala.collection.mutable

class OscService {
  private val data = mutable.Map[String, Seq[Any]]()
  private var listeners = Seq[(String, Seq[Any] => Unit)]()


  private val transmitter = try {
    Some(
      UDP.Transmitter({
        val config = UDP.Config()
        config.localPort = 9000
        config
      })
    )
  } catch {
    case e: Exception => None
  }

  private val reciever = UDP.Receiver({
    val config = UDP.Config()
    config.localPort = 8000
    config
  })

  reciever.action = {
    case (Message("/ping"), from) =>
      transmitter.foreach(_.send(Message("/pong"), from))
    case (Message(name, args@_*), from) =>
      if (data.put(name, args).isDefined)
        println(name + " -> " + args)
      listeners.filter(l => l._1 == name).foreach(l => l._2(args))
  }

  transmitter.foreach(_.connect())
  reciever.connect()

  def apply(name: String): Seq[Any] = data.getOrElse(name, Vector(0f))

  def listen(name: String, f: Seq[Any] => Unit) {
    listeners +:= (name, f)
  }
}