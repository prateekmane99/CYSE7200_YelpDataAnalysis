package com.edu.neu.coe.analysis.model

import java.io._

class Business(business_id:String,name:String, category:Iterable[String], full_address: String,city:String, state:String,
    latitude:Double,longitude:Double, bigramsWithSentiment:Iterable[(String,String)]) extends Serializable {
  
  def getBusiness_id = business_id
  def getName = name
  def getCategory = category
  def getAddress = full_address
  def getCity = city
  def getState = state
  def getLatitude = latitude
  def getLongitude = longitude
  def getBigramsWithSentiment = bigramsWithSentiment
  
  def getGoodReview = bigramsWithSentiment.filter(p => p._2.equals("POSITIVE"))
  def getBadReview = bigramsWithSentiment.filter(p => p._2.equals("NEGATIVE"))
  def getBest = {
    val hm = scala.collection.mutable.HashMap.empty[String,Int]
    val s = getGoodReview.map(f => f._1.split(" ")(1))
    s.foreach { x => 
//      println("++++++++++++" + x)
      if(hm.contains(x))  hm.update(x, hm.get(x).getOrElse(0)+1)
      else  hm.put(x, 0)}
    if(hm.isEmpty)  "No good reviews yet"
    else hm.reduceLeft((o,p) => if(o._2>p._2)  o else p)._1
  }
  
  def getDistance(bus:Business): Double = {
    val R = 3959 //6371; // km (change this constant to get miles)
    val dLat = (bus.getLatitude - latitude) * Math.PI / 180;
    val dLon = (bus.getLongitude - longitude) * Math.PI / 180;
    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(latitude * Math.PI / 180) * Math.cos(bus.getLatitude * Math.PI / 180) *
      Math.sin(dLon / 2) * Math.sin(dLon / 2);
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    val d = R * c;
//    if (d > 1) return Math.round(d) + "miles";
//    else if (d <= 1) return Math.round(d * 1000) + "m";
//    return d + "m";
    d
  }
  
  def getCompetitor(list: Seq[Business],distance: Double, res: Seq[Business]): Seq[Business] = { 
//    list match {
//    case Nil => res
//    case bus::rList => {
//      if(getDistance(bus) <= distance)  getCompetitor(rList, distance, res) :+ bus
//      else  getCompetitor(rList, distance, res)
//    }
//    println("getting competitors --- Business")
    list.filter { x => getDistance(x)<distance }
  }
  
  def allToString(): String = {
    getName
  }
  
}