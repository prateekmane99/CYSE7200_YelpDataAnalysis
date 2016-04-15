package com.edu.neu.coe.analysis.compare

import com.edu.neu.coe.analysis.visualization.WordCloud

object Compare {
  val rdd = WordCloud.getRddBusiness
  val sam = rdd.groupBy(f => f.getState)
  
  
  def main(args: Array[String]): Unit = {
    val busi = sam.first()._2.last
    val res = busi.getCompetitor(sam.first()._2.toSeq, 100, Seq())
    res.foreach { x => println(x.getName) }
  }
  
}