package com.edu.neu.coe.analysis.clean

import org.apache.log4j.{ Logger }
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.Row
import org.apache.spark.rdd.RDD
import scala.collection.mutable.WrappedArray
import com.edu.neu.coe.analysis.YelpDataAnalysis

object DataCleansingUtil {
  val conf = new SparkConf().setAppName("SS").setMaster("local")
    .set("spark.driver.allowMultipleContexts","true")
  val sc = new SparkContext(conf)
  val businessPath = YelpDataAnalysis.getBusinessDataLocation
  val reviewPath = YelpDataAnalysis.getReviewDataLocation
  
  val input = sc.textFile(businessPath)
  println("business file has been textfiled")
  val sqlContext = new SQLContext(sc)
  val data: RDD[Row] = {
  println("-------------------------"+businessPath+"-----------------")
    val business = sqlContext.read.json("/Users/YuanHank/Downloads/abc.json").select("business_id", "name", "categories",
                                                                                    "full_address", "city", "state", 
                                                                                    "latitude", "longitude")
    println("File has been read")
//    val cates = business.collect().filter { x => x.get(1).asInstanceOf[WrappedArray[String]].contains("Restaurants") }.map { x => x.get(0) }
    val review = sqlContext.read.json("/Users/YuanHank/Downloads/review.json").select("text", "business_id")
    println("Get All Reviews")
    val table = business.join(review, business("business_id").alias("id") === review("business_id"), "left")
    val res = table.rdd.filter { x => {
        val categor = x.get(2).asInstanceOf[WrappedArray[String]] 
        categor.contains("Restaurants") || categor.contains("Nightlife")
      }
    }
    println("result count ========================\n" + res.count())
    res.distinct()
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