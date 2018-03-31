package com.szatmary.peter

import org.junit.Assert
import org.junit.Test

class ClassicJunitTest {
  @Test def run(): Unit = Assert.assertTrue("Hello Junit test".substring(0, 5) == "Hello")
}