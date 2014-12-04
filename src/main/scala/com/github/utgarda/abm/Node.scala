package com.github.utgarda.abm

import akka.actor.{ActorRef, Actor}
import akka.actor.Actor.Receive
import akka.event.{Logging, LoggingAdapter}
import com.github.utgarda.abm.Node.{SetRate, SetNeighbours}
import scala.concurrent.duration._

/**
 * Created by etsvigun on 04.12.14.
 */
class Node extends Actor{
  var neighbours: Seq[ActorRef] = Vector()
  val log:LoggingAdapter = Logging.getLogger(context.system, this)
  var rate:Long = 10L
  var msgNum: BigInt = 0

  import context.dispatcher
  val tick =
    context.system.scheduler.schedule(100 millis, 100 millis, self, "tick")

  override def preStart() = {
    log.info(s"${self.path.name} online")
  }

  override def postStop() = {
    log.info(s"${self.path.name} sleeps with the fishes")
  }

  override def receive: Receive = {
    case SetNeighbours(newNeighbours) =>
      neighbours = newNeighbours
      neighbours.foreach{n => n ! s"hi from ${self.path.name}"}
    case SetRate(newRate) =>
      rate = newRate
    case "tick" =>
      for(i <- 1L to rate; n <- neighbours) {
        n ! s"from ${self.path.name} msg$msgNum"
        msgNum += 1
      }
    case msg => log.info(s"${self.path.name} msg: $msg")
  }
}

object Node{
  case class SetNeighbours(neighbours: Seq[ActorRef])
  case class SetRate(rate: Long)
}
