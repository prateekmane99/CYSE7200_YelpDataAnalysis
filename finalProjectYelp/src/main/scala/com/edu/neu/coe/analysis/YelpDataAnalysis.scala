package com.edu.neu.coe.analysis

import org.apache.log4j.{ Logger }
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SQLContext

case class person(business: String, address: String)

object YelpDataAnalysis {
  //@transient lazy val logger = Logger.getLogger(getClass.getName)

  // start the main program
  def main(args: Array[String]): Unit = {

    

//    for (aa <- a) yield {
//      val sentences: IndexedSeq[IndexedSeq[String]] = sentenceSplitter(aa).map(tokenizer).toIndexedSeq
//      for (sentence <- sentences) {
//        val tags = tagger.bestSequence(sentence)
//        println(a.indexOf(aa) + ": " + tags.render)
        
        
        
        
        
        
//        for(i <- 0 to tags.length-3){
//          if(tags.pairs(i)._1.toString().startsWith("JJ") 
//              && tags.pairs(i+1)._1.toString().startsWith("NN")){
//            if(tags.pairs(i+2)._1.toString().startsWith("NN")) 
//              println(tagger.bestSequence(IndexedSeq(tags.pairs(i)._2,tags.pairs(i+2)._2)).render)
//            else
//              println(tagger.bestSequence(IndexedSeq(tags.pairs(i)._2+" "+tags.pairs(i+1)._2)).render)
//          }
//          if(i != tags.length-3 && tags.pairs(i)._1.toString().startsWith("NN") 
//              && tags.pairs(i+1)._1.toString()=="VBZ" 
//              && tags.pairs(i+2)._1.toString().startsWith("JJ")){
//            if(tags.pairs(i+3)._1.toString().startsWith("JJ")) 
//              println(tagger.bestSequence(IndexedSeq(tags.pairs(i+3)._2+" "+tags.pairs(i)._2)).render)
//            else 
//              println(tagger.bestSequence(IndexedSeq(tags.pairs(i+2)._2+" "+tags.pairs(i)._2)).render)
//          }
//          if(tags.pairs(i)._1.toString().startsWith("IN") && tags.pairs(i+1)._1.toString()=="NN"){
//            if(tags.pairs(i+2)._1.toString().startsWith("NN")) 
//              println(tagger.bestSequence(IndexedSeq(tags.pairs(i)._2+" "+tags.pairs(i+2)._2)).render)
//            else
//              println(tagger.bestSequence(IndexedSeq(tags.pairs(i)._2+" "+tags.pairs(i+1)._2)).render)
//          }
//        }
//      }
//    }

  }
}