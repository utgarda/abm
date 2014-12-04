abm
===

Akka messaging benchmark

#### Goals
provide means to measure maximum message rate per second for an Akka actor system on a single machine for variable number of nodes

#### Usage

* Run it  
  * sbt run  
* Enter commands ( single-line, submit with 'enter' ) to  
  * adjust nodes number: '+' or '-'  
  * adjust messages rate: enter an integer number, like '500', to send 500 messages from each node to each other node every 100ms  
