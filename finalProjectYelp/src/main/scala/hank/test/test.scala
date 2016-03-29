package hank.test

import org.codehaus.jackson.map.ObjectMapper
import org.apache.spark.SparkConf
import com.fasterxml.jackson.module.scala.DefaultScalaModule

class test {
  val mapper = new ObjectMapper
  val conf = new SparkConf
  
}