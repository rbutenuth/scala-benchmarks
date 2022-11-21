package de.codecentric.vector

import org.openjdk.jmh.annotations._

import java.util.concurrent.TimeUnit;
import scala.util.Random
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
class Set:
  
  @Param(Array("1", "10", "100", "1000", "10000"))  
  var size: Int =_

  var preparedList: Vector[Int] = _
  var shuffle: Array[Int] = _

  @Setup
  def setup =
    preparedList = Vector.tabulate(size)(x => x + 1)
    shuffle = new Array[Int](size)
    for i <- 0 until size do 
      shuffle(i) = i
        
    val rnd = new Random(42)
    for i <- size to 1 by -1 do
      val j = rnd.nextInt(i)
      val tmp = shuffle(i-1)
      shuffle(i-1) = shuffle(j)
      shuffle(j) = tmp
  end setup  

  @Benchmark  
  def setAllSequential(sink: Blackhole) =
    for i <- 0 until size do
      sink.consume(preparedList.updated(i, -i));

  @Benchmark  
  def set10PercentSequential(sink: Blackhole) =
    for i <- 0 until size / 10 do
      sink.consume(preparedList.updated(10 * i, -i));

  @Benchmark  
  def setAllRandom(sink: Blackhole) =
    for i <- 0 until size do
      sink.consume(preparedList.updated(shuffle(i), -i));

  @Benchmark  
  def set10PercentRandom(sink: Blackhole) =
    for i <- 0 until size / 10 do
      sink.consume(preparedList.updated(shuffle(i), -i));

end Set
