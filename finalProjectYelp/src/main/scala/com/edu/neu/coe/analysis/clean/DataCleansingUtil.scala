package com.edu.neu.coe.analysis.clean

import org.apache.log4j.{ Logger }
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.Row
import org.apache.spark.rdd.RDD
import scala.collection.mutable.WrappedArray
import com.edu.neu.coe.configuration.Config
import com.typesafe.config.ConfigFactory

object DataCleansingUtil {
  
  val sc = Config.getSparkContext()
  val sqlCtx = Config.getSqlContext()
  
  val strReview = ConfigFactory.load().getString("Review.Prateek.value")
  val strBusiness = ConfigFactory.load().getString("Review.Prateek.value")
 val review_Rdd = sc.textFile(strReview)
  val business_Rdd = sc.textFile(strBusiness)
  
  
  
  def cleanData: RDD[Row] = {
    val business = sqlCtx.read.json("/Users/Prateek/Documents/Spring 2016/Scala/Project/YELP/yelp/DS - YELP/yelp_academic_dataset_business.json").select("business_id", "categories")
//    val cates = business.collect().filter { x => x.get(1).asInstanceOf[WrappedArray[String]].contains("Restaurants") }.map { x => x.get(0) }
    val review = sqlCtx.read.json("/Users/Prateek/Documents/Spring 2016/Scala/Project/YELP/yelp/DS - YELP/yelp_academic_dataset_review.json").select("text", "business_id")
    val table = business.join(review, business("business_id").alias("id") === review("business_id"), "left")
    val res = table.rdd.filter { x => x.get(1).asInstanceOf[WrappedArray[String]].contains("Restaurants") }
     
    res 
  }
  
  def cleanDataOutRDDString: RDD[String] = {
    cleanData.map { x => x.mkString.dropRight(22) } 
  }
  
}