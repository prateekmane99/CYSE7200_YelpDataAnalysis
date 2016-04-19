package com.edu.neu.coe.analysis.compare

import com.edu.neu.coe.analysis.visualization.WordCloud
import com.edu.neu.coe.analysis.model.Business
import com.edu.neu.coe.analysis.clean.DataCleansingUtil

object Compare {
  val sc = DataCleansingUtil.sqlContext
  import sc.implicits._
  val rdd = WordCloud.getRddBusiness
  //group all businesses, (state, Business)
  val sam = { println("samsamsamsmamasmsmmamasmmsamasmsam"); rdd.groupBy(f => f.getState) }
  val dean = sam.collect()

  case class ComparisonI(bid: String, businessName: String, goodReviewNum: Int, 
      badReviewNum: Int, bestReview: String, 
      competetors:Seq[(String, Double, Iterable[(String, String)], Iterable[(String, String)], String)]) extends java.io.Serializable
  
  class Comparison(b: Business, compe: Seq[Business]) extends java.io.Serializable {
    def bid = b.getBusiness_id
    def businessName = b.getName
    def businessState = b.getState
    def businessCity = b.getCity
    def goodReviewNum = b.getGoodReview.size
    def badReviewNum = b.getBadReview.size
    def bestReview = b.getBest
    def competetors = compe.map { x => (x.getBusiness_id, b.getDistance(x), x.getGoodReview, x.getBadReview, x.getBest) }
    
    def tStr = ComparisonI(bid, businessName, goodReviewNum, badReviewNum, bestReview, competetors)
  }

  /*
   * organize RDD of comparisons in a proper way
   */
  def compare(fileName: String) = {
    val re = getRDD.map { x => x.tStr }
    println("all comparison prepared")
    re.saveAsTextFile(fileName)
    //    val busi = sam.first()._2.last
    //    val res = busi.getCompetitor(sam.first()._2.toSeq, 100, Seq())
    //    
    //    res.foreach { x => println(x.getName) }
    //    re.foreach { x => println(x) }
    //    sam.foreach(f => println("===========================\n"+f+"\n==========================="))
  }

  /*
   * For every business,
   * Get all business in the same state with one certain business
   * Then, find all competitors with in 10 miles away
   * Use this business and its competitors to construct a Comparison Object
   */
  def getRDD = {
    rdd.map { x =>
      val ss = dean.filter(a => a._1.equals(x.getState)).map(b => b._2)
      val seq = {
        ss(0).toSeq
      }
      //      println("seqqqqqqqqqqqqqqqqqq:\n" + seq)
      //      val seq = sam.first()._2.toSeq
      val e = new Comparison(x, x.getCompetitor(seq, 10, Seq()))
      e
    }
  }
  
}