package com.edu.neu.coe.analysis

import org.apache.log4j.{ Logger }
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark._

case class person(business:String, address: String)

object YelpDataAnalysis{
  //@transient lazy val logger = Logger.getLogger(getClass.getName)

  // start the main program
  def main(args: Array[String]):Unit = {

    val conf = new SparkConf().setAppName("SS").setMaster("local")
    // conf.set(SPARK_STORAGE_MEMORY_FRACTION, SPARK_STORAGE_MEMORY_VALUE)
    val sc = new SparkContext(conf)

    //logger.info("DD")

    val input = sc.textFile("/Users/YuanHank/Downloads/abc.json")
    println("SSS")

    println(input.collect().mkString("\n"));

  }
}