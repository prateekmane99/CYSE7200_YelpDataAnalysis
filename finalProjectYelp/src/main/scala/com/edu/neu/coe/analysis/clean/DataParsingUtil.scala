package com.edu.neu.coe.analysis.clean

import epic.preprocess.TreebankTokenizer
import epic.preprocess.MLSentenceSegmenter

object DataParsingUtil {
  val r = DataCleansingUtil.cleanData
  
   def main(args: Array[String]): Unit = {
    val sentenceSplitter = MLSentenceSegmenter.bundled().get
    val tokenizer = new epic.preprocess.TreebankTokenizer()
    val tagger = epic.models.PosTagSelector.loadTagger("en").get // or another 2 letter code.
    
//    val sentences: IndexedSeq[IndexedSeq[String]] = sentenceSplitter(text).map(tokenizer).toIndexedSeq
    val s = r.map { x => x.getString(2) }
    for(a <- s.collect()) println(a)
    val wordsBag:List[String] = List("food","hoagie","burger","patty", "ingredient", "service", "place","veggie", "wing", "area", "dining",
        "cajun", "sauce", "bear", "fish", "sandwich", "bread", "soup", "chicken", "salad", "steak")
  }
}