package com.edu.neu.coe.test

import org.scalatest._
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.sql.SQLContext

class TestBasicSpec extends FlatSpec with BeforeAndAfterAll with Matchers {
  private val master = "local"
  private val appName = "Rossmann Sales Testing"
  var sc: SparkContext = _
  var sqlContext: SQLContext = _

  val conf = new SparkConf()
    .setMaster(master)
    .setAppName(appName)

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    sc = new SparkContext(conf)
    sqlContext = new SQLContext(sc)
  }
  
  override protected def afterAll(): Unit = {
    try {
      sc.stop()
      sc = null
      sqlContext = null
    } finally {
      super.afterAll()
    }
  }
}