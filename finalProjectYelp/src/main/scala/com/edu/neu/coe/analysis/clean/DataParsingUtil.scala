package com.edu.neu.coe.analysis.clean

import com.edu.neu.coe.analysis.clean._
import com.edu.neu.coe.analysis.model.WordsBag
import org.apache.spark.rdd.RDD
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import com.edu.neu.coe.analysis.YelpDataAnalysis

/*
 * "business_id", "name", "categories", "full_address", "city", "state", "latitude", "longitude"
 * "text"
 */

object DataParsingUtil {
  val r = DataCleansingUtil.cleanData
  val sc = DataCleansingUtil.getSparkContext
  val loc = YelpDataAnalysis.getPosTaggerModelLocation
  val mx = new MaxentTagger("/Users/YuanHank/Downloads/stanford-postagger-2015-12-09/models/english-bidirectional-distsim.tagger");

  val wordsBag = WordsBag.getWordsBag
  
  def getAll = {
    val res = r.map { x => x.toSeq.dropRight(1) }
    println("getting all business in ROW --- DataParsingUtil def getAll")
    res
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
    if (w<f.size-1 && f(w+1)._2.startsWith("VB")) f.slice(w, f.size).find(p => p._2.startsWith("JJ"))
    else f.slice(0, w).reverse.find(p => p._2.startsWith("JJ"))
  }

  //Transfer text reviews to bigrams
  def getBigramWithAll = {
    val res = getAll.map {
      x =>
        Option(x.last) match {
          case Some(y) => (x.dropRight(1), extractKeyWords(y.toString().toLowerCase(), wordsBag))
          case None    => (x.dropRight(1), Seq("No Reviews Yet"))
        }
    }
    println("All bigrams got --- DataParsingUtil def getBigramWithAll")
    res
  }
  def getBigram = {
    getBigramWithAll.map { x => x._2 }
  }
  def getBigramWithId = {
    getBigramWithAll.map { x => (x._1(0).toString(), x._2) }
  }
}


