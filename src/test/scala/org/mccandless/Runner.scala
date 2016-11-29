package org.mccandless

import org.junit.runner.Description
import org.junit.runner.notification.{Failure, RunNotifier}
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.FrameworkMethod

/**
  * Created by tomas.mccandless on 11/28/16.
  */
class Runner(val clazz: Class[_]) extends BlockJUnit4ClassRunner(clazz) {

  /**
    * Simulates running 3 tests: A, B, and C. C is a failure.
    * Notification events are interleaved such that teamcity counts this as two executions of A and one execution of B.
    * Gradle, JUnit, and IntelliJ all correctly interpret this as 3 distinct tests being executed.
    *
    * @param method
    * @param notifier
    */
  override protected def runChild(method: FrameworkMethod, notifier: RunNotifier): Unit = {
    val className: String = method.getDeclaringClass.getCanonicalName

    val testA: Description = Description.createTestDescription(className, "testA")
    val testB: Description = Description.createTestDescription(className, "testB")
    val testC: Description = Description.createTestDescription(className, "testC")
    val testCFailed: Failure = new Failure(testC, new RuntimeException("testC failed"))

    notifier.fireTestStarted(testA)
    notifier.fireTestStarted(testB)
    notifier.fireTestFinished(testA)
    notifier.fireTestStarted(testC)
    notifier.fireTestFinished(testB)
    // test C failed
    notifier.fireTestFailure(testCFailed)
    notifier.fireTestFinished(testC)
  }
}
