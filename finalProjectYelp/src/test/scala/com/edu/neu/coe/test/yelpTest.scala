package com.edu.neu.coe.test

import org.scalatest._
import com.edu.neu.coe.analysis.clean._

class yelpTest extends TestBasicSpec with GivenWhenThen with Matchers {
  "Spark Context" should "not be null" in {
   assertResult(2)(sc.parallelize(Seq(1,2,3)).filter(_ <= 2).map(_ + 1).count)
  }
  
  "Load data" should "not be null" in {
    val rawTestData = DataCleansingUtil.data("/Users/YuanHank/Downloads/test.json")
    rawTestData.collect() should have length(1)
  }
  
  "Extract Key Words" should "extract adjs describing nouns from wordsBag" in {
    val keywords = DataParsingUtil.extractKeyWords("good food", Seq("food"))
    keywords should have length(1)
  }
  
  "Extract Key Words" should "not extract adjs describing nouns not from wordsBag" in {
    val keywords = DataParsingUtil.extractKeyWords("good food", Seq("haha"))
    keywords should have length(0)
  }
  
  "Extract Key Words" should "not extract adjs which reviews dont have to describe nouns" in {
    val keywords = DataParsingUtil.extractKeyWords("good fish", Seq("food"))
    keywords should have length(0)
  }
  
  "Extract Key Words" should "not extract things reviews and wordbag dont have" in {
    val keywords = DataParsingUtil.extractKeyWords("good qqqq sdfa adf", Seq())
    keywords should have length(0)
  }
  
  
}