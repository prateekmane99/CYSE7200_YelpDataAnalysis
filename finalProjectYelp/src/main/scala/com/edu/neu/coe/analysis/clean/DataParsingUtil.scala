package com.edu.neu.coe.analysis.clean

import com.edu.neu.coe.analysis.clean._
import org.apache.spark.rdd.RDD
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import com.edu.neu.coe.configuration.Config

object DataParsingUtil {
  val r = DataCleansingUtil.cleanData
  val sc = Config.getSparkContext()
  val mx = new MaxentTagger("/Users/Prateek/Documents/Spring 2016/Scala/Project/YELP/Jars/Jars/Jars/english-bidirectional-distsim.tagger");
  
  val wordsBag = List("food","hoagie","burger","patty", "ingredient", "service", "place","veggie", "wing", "area", "dining",
        "cajun", "sauce", "bear", "fish", "sandwich", "bread", "soup", "chicken", "salad", "steak");
  
  def getReviews: RDD[Option[String]]={
    r.map { x => Option(x.getString(2)) }
  }
   
  def getReviewsWithBusinessId:RDD[(String,String)]={
    r.map { x => (x.getString(0), x.getString(2)) }
  }
  
  def dealWithReviews: RDD[(String,String)]={
    getReviewsWithBusinessId.map { x => (x._1,x._2)}
  }
  
  def extractKeyWords(text: String, wordsBag: Seq[String]): Seq[String] = {
    val after = mx.tagString(text).split(" ")
    val f = after.map { x => (x.split("_")(0),x.split("_")(1)) }
    val s = Seq[String]()
    
    def findProperAdj(s: Seq[String], source: Array[(String,String)], words: Seq[String]): Seq[String] = words match {
      case Nil => s
      case word::seq => if(source.contains(word,"NN")) findProperAdj(s:+(findAdj(f, f.indexOf((word,"NN")))+" "+word), source, seq)
                        else findProperAdj(s, source, seq)
    }
    
    findProperAdj(s,f,wordsBag)
  }
  
  def findAdj(f:Array[(String,String)], w:Int) = findAdjOption(f,w) match{
    case Some(x) => x._1
    case None => 
  }
  
  def findAdjOption(f: Array[(String,String)], w: Int) = {
    if(f(w+1)._2 == "VBZ")  f.slice(w, f.size).find(p => p._2.startsWith("JJ"))
    else f.slice(0, w).reverse.find(p => p._2.startsWith("JJ"))
  }
  
   def main(args: Array[String]): Unit = {
    /*
     * 1. analyze r Row by Row using Stanford NLP
     * 2. for each Row, find adj and noun, return them as List(business_id -> adj+n)
     * 3. Combine all the Lists, return it as the result
     */
     val res = getReviews.map {f => f match{
         case Some(x) => extractKeyWords(x.toLowerCase(), wordsBag)
         case None => Nil
       }
     }
     for(r<-res;w<-r)  println(w)
   }
}