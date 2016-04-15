package com.edu.neu.coe.analysis.clean

import com.edu.neu.coe.analysis.clean._
import org.apache.spark.rdd.RDD
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/*
 * "business_id", "name", "categories", "full_address", "city", "state", "latitude", "longitude"
 * "text"
 */

object DataParsingUtil {
  val r = DataCleansingUtil.cleanData
  val sc = DataCleansingUtil.getSparkContext
  val mx = new MaxentTagger("/Users/YuanHank/Downloads/stanford-postagger-2015-12-09/models/english-bidirectional-distsim.tagger");

  val wordsBag = List("food", "hoagie", "burger", "patty", "ingredient", "service", "place", "veggie", "wing", "area", "dining",
    "cajun", "sauce", "bear", "fish", "sandwich", "bread", "soup", "chicken", "salad", "steak");
  
  def getAll = {
    r.map { x => x.toSeq.dropRight(1) }
  }
  def getReviews: RDD[Option[String]] = {
    getAll.map { x => Option(x(8).toString()) }
  }
  def getReviewsWithBusinessId: RDD[(String, Option[String])] = {
    getAll.map { x => (x(0).toString(), Option(x(8).toString())) }
  }

  //  def dealWithReviews: RDD[(String,String)]={
  //    getReviewsWithBusinessId.map { x => (x._1,x._2)}
  //  }

  /*
   * extract all adj+nouns from one certain review
   */
  def extractKeyWords(text: String, wordsBag: Seq[String]): Seq[String] = {
    val after = mx.tagString(text).split(" ")
    val f = after.map { x => (x.split("_")(0), x.split("_")(1)) }
    val s = Seq[String]()

    def findProperAdjWithNouns(s: Seq[String], source: Array[(String, String)], words: Seq[String]): Seq[String] = words match {
      case Nil => s
      case word :: seq =>
        if (containsWord(source, word)) {
          val list = source.map(f => f._1).toList
          val index = wordIndex(list, word, 0)
          findProperAdjWithNouns(s :+ (findAdj(source, index) + " " + word), source, seq)
        } else findProperAdjWithNouns(s, source, seq)
    }

    def containsWord(s: Array[(String, String)], word: String) = {
      val words = s.map(sou => sou._1)
      val f = (b: Boolean, str: String) => b || str.startsWith(word)
      words.foldLeft(false)(f)
    }

    def wordIndex(words: List[String], word: String, index: Int): Int = {
      words match {
        case Nil    => index
        case w :: s => if (w.startsWith(word)) index else wordIndex(s, word, index + 1)
      }
    }

    findProperAdjWithNouns(s, f, wordsBag)
  }

  def findAdj(f: Array[(String, String)], w: Int) = findAdjOption(f, w) match {
    case Some(x) => x._1
    case None    => 0
  }

  def findAdjOption(f: Array[(String, String)], w: Int) = {
    //follow with VB
    if (f(w + 1)._2.startsWith("VB")) f.slice(w, f.size).find(p => p._2.startsWith("JJ"))
    else f.slice(0, w).reverse.find(p => p._2.startsWith("JJ"))
  }

  def getBigramWithAll = {
    getAll.map { 
      x => 
        Option(x.last) match {
          case Some(y) => (x.dropRight(1), extractKeyWords(y.toString().toLowerCase(), wordsBag))
          case None    => (x.dropRight(1), Seq("No Reviews Yet"))
        }
    }
  }
  def getBigram = {
    getBigramWithAll.map { x => x._2 }
  }
  def getBigramWithId = {
    getBigramWithAll.map { x => (x._1(0).toString(), x._2) }
  }
  


  def main(args: Array[String]): Unit = {
    /*
     * 1. analyze r Row by Row using Stanford NLP
     * 2. for each Row, find adj and noun, return them as List(business_id -> adj+n)
     * 3. Combine all the Lists, return it as the result
     */
    //     val res = getReviews.map {f => f match{
    //         case Some(x) => extractKeyWords(x.toLowerCase(), wordsBag)
    //         case None => Nil
    //       }
    //     }
    val ress = getReviewsWithBusinessId.map {
      f =>
        f match {
          case (x, Some(y)) => extractKeyWords(y.toLowerCase(), wordsBag).map { str => (x + " => " + str) }
          case (x, None)    => Seq(x + " => " + "No Reviews Yet")
        }
    }
    for (r <- ress; w <- r) println(w)
  }
}


