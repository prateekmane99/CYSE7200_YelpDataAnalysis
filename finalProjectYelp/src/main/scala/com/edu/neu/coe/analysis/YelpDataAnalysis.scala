package com.edu.neu.coe.analysis

import org.apache.log4j.{ Logger }
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SQLContext
import com.edu.neu.coe.configuration.Config

object YelpDataAnalysis {
  //@transient lazy val logger = Logger.getLogger(getClass.getName)

  // start the main program
  def main(args: Array[String]): Unit = {
     
    
    
    //  val sc = Config.getSparkContext()
     // val sqlCtx = Config.getSqlContext()
     val conf = new SparkConf().setAppName("SS").setMaster("local")
      val sc = new SparkContext(conf)
      val sqlCtx = new SQLContext(sc)
     
 

     val business = sc.textFile("/Users/Prateek/Documents/Spring 2016/Scala/Project/YELP/yelp/DS - YELP/yelp_academic_dataset_business.json")
     val review = sc.textFile("/Users/Prateek/Documents/Spring 2016/Scala/Project/YELP/yelp/DS - YELP/yelp_academic_dataset_review.json")
     
     
       val business_DF = sqlCtx.read.json("/Users/Prateek/Documents/Spring 2016/Scala/Project/YELP/yelp/DS - YELP/yelp_academic_dataset_business.json").select("business_id", "state", "city", "name", "longitude", "latitude")
	    
	     val review_DF = sqlCtx.read.json("/Users/Prateek/Documents/Spring 2016/Scala/Project/YELP/yelp/TEST/review.json").select("business_id", "text")
	     
        val joinedDF = business_DF.join(review_DF, business_DF("business_id") === review_DF("business_id"), "inner")
        
         val abc = joinedDF.rdd
         
        val cd =  abc.map { x => (x.getString(0), x.getString(1), x.getString(2), x.getString(3), x.getString(4), x.getString(5), x.getString(7)) }
      // print(cd.collect())

       
        
	       // val Cbusiness = sqlCtx.read.json("/Users/Prateek/Documents/Spring 2016/Scala/Project/YELP/yelp/TEST/abc.json")
     
     import com.typesafe.config.ConfigFactory
	        //val strReview = ConfigFactory.load("/src/main/scala/com/edu/neu/coe/resources/application.conf").getString("my.secret.value")
	      //   val strReview = ConfigFactory.load().getString("my.secret.value")
	       
	       // print(strReview)
	        
	        
        
	   val wordsBag = List("food","hoagie","burger","patty", "ingredient", "service", "place","veggie", "wing", "area", "dining",
        "cajun", "sauce", "bear", "fish", "sandwich", "bread", "soup", "chicken", "salad", "steak")
  
	        
	   for(abc <- wordsBag){
	    val  a = "http://thesaurus.altervista.org/thesaurus/v1?word="+abc+"&key=Zpaz3EmJcfOOA5vVTOWN&language=en_US&output=json"
      val result = scala.io.Source.fromURL(a).mkString
      import scala.util.parsing.json._
      //println(result)
	    val b = JSON.parseFull(result)
	    b match {
         case Some(e) => //println(e) // => Map(name -> Naoki, lang -> List(Java, Scala))
          case None => println("Failed.")
            }
	   }
    
	   case class CertFile(name: String, path: String, extension: String)

case class Certificate(certType: String, certFile: CertFile)



 import scala.util.parsing.json._
 
   val json =
    """{ "certificates": [{"type": "abc","file": {"name": "xyz","path":"/usr/local","extension": "csv"}} ,  {"type": "xyz","file": {"name": "xyz","path": "/usr/local","extension": "csv"}} , {"type": "nmo","file": {"name": "xyz","path": "/usr/local","extension": "csv"}}] }"""

  val jsonValue = JSON.parseFull(json)

 // val list = (jsonValue \ "certificates").as[List[Certificate]]
  
  import org.json4s._
  import org.json4s.JsonDSL._
  import org.json4s.jackson.JsonMethods._
  
   val  a = "http://thesaurus.altervista.org/thesaurus/v1?word=chicken&key=Zpaz3EmJcfOOA5vVTOWN&language=en_US&output=json"
      val result = scala.io.Source.fromURL(a).mkString
    print(result)
	   print("\n")
  val b = parse(result)
  print("\n")
	     
  val c = b\"response"
	    for(
	      cc <- c.children){
	   //   print(cc)
	    }
  
  ///  val df = sqlContext.read.json(result)
   // df.show
	  //  val  a = "http://thesaurus.altervista.org/thesaurus/v1?word=chicken&key=Zpaz3EmJcfOOA5vVTOWN&language=en_US&output=json"
	   // val ids = a.map(_("Locations")("list").map(_("id"))).getOrElse(List())
	   
	   
	        
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