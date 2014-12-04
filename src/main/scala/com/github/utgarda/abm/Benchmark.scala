package com.github.utgarda.abm

import akka.actor.{ActorRef, Actor, Props}

import scala.concurrent.duration._


/**
 * Created by etsvigun on 04.12.14.
 */
class Benchmark extends Actor {

  var nodeFarm: ActorRef = null

  override def preStart(): Unit = {
    nodeFarm = context.actorOf(Props[NodeFarm], "nodes")
    nodeFarm ! NodeFarm.AddNodes(5)
    nodeFarm ! NodeFarm.RemoveNodes(2)

    import context.dispatcher
    val tick =
      context.system.scheduler.schedule(100 millis, 100 millis , self, "tick")

  }
    def receive = {
      case "tick" =>
        val ln = scala.io.StdIn.readLine()
        ln match {
          case "q" => context.stop(self)
          case "+" =>
            println("adding 10 nodes")
            nodeFarm ! NodeFarm.AddNodes(10)
          case "-" =>
            println("removing 10 nodes")
            nodeFarm ! NodeFarm.RemoveNodes(10)
          case i if i.matches("[+-]?\\d+") =>
            val rate = Integer.parseInt(i)
            println(s"setting new rate: $rate")
            nodeFarm ! NodeFarm.SetRate(rate)
        }

      case _ =>
    }
}
