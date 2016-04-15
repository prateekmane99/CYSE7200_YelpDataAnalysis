package com.edu.neu.coe.analysis.clean

import org.apache.log4j.{ Logger }
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.Row
import org.apache.spark.rdd.RDD
import scala.collection.mutable.WrappedArray

object DataCleansingUtil {
  val conf = new SparkConf().setAppName("SS").setMaster("local")
  val sc = new SparkContext(conf)
  val input = sc.textFile("/Users/YuanHank/Downloads/abc.json")
  val sqlContext = new SQLContext(sc)
  val data: RDD[Row] = {
    val business = sqlContext.read.json("/Users/YuanHank/Downloads/abc.json").select("business_id", "name", "categories",
                                                                                    "full_address", "city", "state", 
                                                                                    "latitude", "longitude")
//    val cates = business.collect().filter { x => x.get(1).asInstanceOf[WrappedArray[String]].contains("Restaurants") }.map { x => x.get(0) }
    val review = sqlContext.read.json("/Users/YuanHank/Downloads/review.json").select("text", "business_id")
    val table = business.join(review, business("business_id").alias("id") === review("business_id"), "left")
    val res = table.rdd.filter { x => {
        val categor = x.get(2).asInstanceOf[WrappedArray[String]] 
        categor.contains("Restaurants") || categor.contains("Nightlife")
      }
    }
    res 
  }
  
  def cleanData = data
  
  def cleanDataOutRDDString: RDD[String] = {
    cleanData.map { x => x.mkString.dropRight(22) } 
  }
  
  def getSparkContext: SparkContext = sc
 
  
  //NOT FINISHED!!!!
  def getBigramsWithBusinessIdAndLocation = {
    val business = sqlContext.read.json("")
  }
  
}