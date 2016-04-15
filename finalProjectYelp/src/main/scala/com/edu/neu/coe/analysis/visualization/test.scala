package com.edu.neu.coe.analysis.visualization

import com.edu.neu.coe.analysis.clean.DataCleansingUtil
import com.edu.neu.coe.analysis.clean.DataParsingUtil

object test {
  def main(args: Array[String]): Unit = {
    WordCloud.outputSentimentWithAll("/Users/YuanHank/Desktop/test")
//    WordCloud.getWordCloud()
//    WordCloud.outputSentimentWithId("/Users/YuanHank/Desktop/test")
//    DataCleansingUtil.cleanData
    
//    DataParsingUtil.getAll.foreach { x => println(x(8)) }
  }
}