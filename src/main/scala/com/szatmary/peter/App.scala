package com.szatmary.peter

import scala.util.Random

object App {
  def main(args: Array[String]): Unit = {
    // is not important here now
  }
}

class App {
  def oldWay: Long = {
    val data = testData
    var res = 0L
    var ii = 0
    while (ii < data.size) {
      res = res + data(ii)
        ii += 1
    }
    res
  }

  def newWay: Long = testData.toStream.sum

  private def testData = {
    var result = List[Int]()
    val rand = new Random()
    var ii = 0
    while (ii < 100000) {
      result = rand.nextInt(100000) :: result
      ii += 1
    }
    result
  }
}