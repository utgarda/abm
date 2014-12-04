package com.github.utgarda.abm

import akka.actor._
import akka.actor.Actor.Receive

import akka.event.Logging
import akka.event.LoggingAdapter
import com.github.utgarda.abm.NodeFarm.{SetRate, RemoveNodes, AddNodes}

/**
 * Created by etsvigun on 04.12.14.
 */
class NodeFarm extends Actor {

  val log:LoggingAdapter = Logging.getLogger(context.system, this)
  var nextNodeId = 0
  var nodes:List[ActorRef] = List()

  override def preStart() = {
    log.info(s"Starting ${self.path.name}")
  }

  override def receive: Receive = {
    case AddNodes(n) =>
      val newNodes = (1 to n).map{ _ =>
        val node =  context.actorOf(Props[Node], s"node$nextNodeId")
        nextNodeId += 1
//        context.watch(node)
        node
      }
      nodes = nodes ++ newNodes
      broadcastNewNeighbours()

    case RemoveNodes(n) =>
      val (staying, leaving) = nodes.splitAt(Math.max(0, nodes.length - n))
      leaving.foreach{l => l ! Kill}
      nodes = staying
      broadcastNewNeighbours()

    case SetRate(newRate) =>
      nodes.foreach{l => l ! Node.SetRate(newRate)}

    case _ => log.info("msg")
  }

  private

  def broadcastNewNeighbours() = {
    nodes.foreach(n => n ! Node.SetNeighbours(nodes diff List(n)))
  }
}

object NodeFarm {
  case class AddNodes(n: Int)
  case class RemoveNodes(n: Int)
  case class SetRate(rate: Long)
}
