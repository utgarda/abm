package com.github.utgarda.abm

/**
 * Created by etsvigun on 04.12.14.
 */
object Main {
  def main(args: Array[String]): Unit = {

    akka.Main.main(Array(classOf[Benchmark].getName))
  }
}
