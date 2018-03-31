package com.szatmary.peter

import org.junit.Assert
import org.junit.Test
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.results.BenchmarkResult
import org.openjdk.jmh.results.RunResult
import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.RunnerException
import org.openjdk.jmh.runner.options.Options
import org.openjdk.jmh.runner.options.OptionsBuilder
import org.openjdk.jmh.runner.options.TimeValue
import org.openjdk.jmh.runner.options.VerboseMode
import java.util
import java.util.concurrent.TimeUnit

import com.szatmary.peter.SampleBenchmarkTest.St
import com.szatmary.peter.SampleBenchmarkTest.St.AVERAGE_EXPECTED_TIME

/**
  * It is recommended to run JMH not from tests but directly from different main method code.
  * Unit-tests and other IDE interferes with the measurements.
  *
  * If your measurements will be in second / minutes or longer than it running nechmarks from tests
  * will not affect your benchmark results.
  *
  * If your measurements will be in  miliseconds, microseconds , nanoseconds ... run your
  * benchmarks rather not from tests bud from main code to have better measurements.
  */
object SampleBenchmarkTest {

  @State(Scope.Benchmark) object St {
    private[peter] val AVERAGE_EXPECTED_TIME = 100 // expected max 100 milliseconds
    val app = new App

  }

}

class SampleBenchmarkTest {
  /**
    * Benchmark run with Junit
    *
    * @throws Exception
    */
    @Test
    @throws[Exception]
    def runTest(): Unit = {
      val opt = initBench
      val results = runBench(opt)
      assertOutputs(results)
    }

  /**
    * JMH benchmark
    *
    */
  @Benchmark
  def oldWay(st: St.type): Unit = st.app.oldWay

  @Benchmark
  def newWay(st: St.type): Unit = st.app.newWay

  /**
    * Runner options that runs all benchmarks in this test class
    * namely benchmark oldWay and newWay.
    *
    * @return
    */
  private def initBench: Options = {
    System.out.println("running" + classOf[SampleBenchmarkTest].getSimpleName + ".*")
    return new OptionsBuilder()
      .include(classOf[SampleBenchmarkTest].getSimpleName + ".*")
      .mode(Mode.AverageTime)
      .verbosity(VerboseMode.EXTRA)
      .timeUnit(TimeUnit.MILLISECONDS)
      .warmupTime(TimeValue.seconds(1))
      .measurementTime(TimeValue.milliseconds(1))
      .measurementIterations(2).
      threads(4)
      .warmupIterations(2)
      .shouldFailOnError(true)
      .shouldDoGC(true)
      .forks(1)
      .build
  }

  /**
    *
    * @param opt
    * @return
    * @throws RunnerException
    */
  @throws[RunnerException]
  private def runBench(opt: Options) = new Runner(opt).run

  /**
    * Assert benchmark results that are interesting for us
    * Asserting test mode and average test time
    *
    * @param results
    */
  private def assertOutputs(results: util.Collection[RunResult]) = {
    import scala.collection.JavaConversions._
    for (r <- results) {
      import scala.collection.JavaConversions._
      for (rr <- r.getBenchmarkResults) {
        val mode = rr.getParams.getMode
        val score = rr.getPrimaryResult.getScore
        val methodName = rr.getPrimaryResult.getLabel
        Assert.assertEquals("Test mode is not average mode. Method = " + methodName, Mode.AverageTime, mode)
        Assert.assertTrue("Benchmark score = " + score + " is higher than " + AVERAGE_EXPECTED_TIME + " " + rr.getScoreUnit + ". Too slow performance !", score < AVERAGE_EXPECTED_TIME)
      }
    }
  }
}