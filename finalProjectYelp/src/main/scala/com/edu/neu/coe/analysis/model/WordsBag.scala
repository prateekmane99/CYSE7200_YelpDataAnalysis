package com.edu.neu.coe.analysis.model

import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SQLContext
import play.api.libs.functional.syntax._
import play.api.libs.json._
import com.edu.neu.coe.analysis.clean.DataCleansingUtil
import scala.util.{Try, Success, Failure}

object WordsBag {

  val conf = DataCleansingUtil.conf
  val sc = DataCleansingUtil.sc

  val wordsBag = List("food", "hoagie", "burger", "patty", "ingredient", "service", "place", "veggie", "wing", "area", "dining",
    "cajun", "sauce", "bear", "fish", "sandwich", "bread", "soup", "chicken", "salad", "steak", "sausage", "breakfast", "lunch",
    "ambiance", "coffee", "milk", "frie");

  val getWordsBagInRDD = {
//    val href = "http://thesaurus.altervista.org/thesaurus/v1?word=sandwich&key=Zpaz3EmJcfOOA5vVTOWN&language=en_US&output=json"
//    val result = scala.io.Source.fromURL(href).mkString
//    val bf = Json.parse(result) \\ "list"

    val v = for (word <- wordsBag) yield {
      println("words are downloading from Internet")
      val href = "http://thesaurus.altervista.org/thesaurus/v1?word=" + word + "&key=Zpaz3EmJcfOOA5vVTOWN&language=en_US&output=json"
      val result = Try(scala.io.Source.fromURL(href).mkString)
      val bf = Json.parse(result match{
        case Success(x) => x
        case Failure(_) => "null"
      }) \\ "list"
      

      val bfa = bf.filter { b => b.\("category").toString().contains("noun") }

      //      println("somethong +++++++ " + bfa(0).\("synonyms").toString())

      val c = bfa.flatMap { b => b.\("synonyms").toString().replace("|", ",").replace("\"", "").split(',') }
      word +: c.filter { x => !x.contains(" ") }
    }
    println("words downloading from Internet completed")
    val res = sc.parallelize(v.flatMap { x => x }).distinct()
    println("words cleaning completed")
    res.distinct()
  }

  def getWordsBag = getWordsBagInRDD.collect().toList
}