package com.edu.neu.coe.analysis.visualization

import wordcloud.PolarWordCloud
import wordcloud.PolarBlendMode
import wordcloud.CollisionMode
import wordcloud.bg.CircleBackground
import wordcloud.font.FontWeight
import wordcloud.font.scale.SqrtFontScalar
import wordcloud.nlp.FrequencyAnalizer
import com.twitter.chill.Base64.InputStream
import java.util.Properties
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.trees.Tree
import edu.stanford.nlp.util.CoreMap
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

//import wordcloud.font.scale.LinearFontScalar
//import wordcloud.bg.PixelBoundryBackground
//import wordcloud.bg.RectangleBackground
//import wordcloud.font.CloudFont
//import wordcloud.palette.ColorPalette
//import org.apache.commons.codec.binary.Base64
//import collection.JavaConverters._
//import java.net.URL
//import java.io.ByteArrayOutputStream
//import javax.imageio.ImageIO

object WordCloud {
 
  def getWordCloud() = {
val frequencyAnalizer = new FrequencyAnalizer()
frequencyAnalizer.setWordFrequencesToReturn(750)
//val wordFrequencies  = frequencyAnalizer.load(aaa.getInputStream("/Users/Prateek/Documents/Data Analytics/Big Data/Spark/What is Spark"))
val wordCloud = new PolarWordCloud(600, 600, CollisionMode.PIXEL_PERFECT, PolarBlendMode.BLUR)
wordCloud.setPadding(2)
wordCloud.setBackground(new CircleBackground(300))
wordCloud.setFontScalar(new SqrtFontScalar(10, 40))
//wordCloud.build(wordFrequencies)

//val baos = new ByteArrayOutputStream()
//ImageIO.write(wordCloud.getBufferedImage(), "png", baos)
//val image = new String(Base64.encodeBase64(baos.toByteArray()))

wordCloud.writeToFile("/Users/Prateek/Documents/Data Analytics/Sparkwhale_wordcloud_small.png");
  }

  
      def getDistance(lat1: Double,  lon1: Double , lat2: Double, lon2: Double) : String = {
	        val R = 3959  //6371; // km (change this constant to get miles)
	              val dLat = (lat2-lat1) * Math.PI / 180;
	              val dLon = (lon2-lon1) * Math.PI / 180;
	              val a = Math.sin(dLat/2) * Math.sin(dLat/2) + 
	                      Math.cos(lat1 * Math.PI / 180 ) * Math.cos(lat2 * Math.PI / 180 ) *
		                    Math.sin(dLon/2) * Math.sin(dLon/2);
	              val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	              val d = R * c;
	                if (d>1) return Math.round(d)+"miles";
	                else if (d<=1) return Math.round(d*1000)+"m";
	              return d + "m";
	        } 
      
      
      
      def getSentiment(strReview: String) = {
            val nlpProps =  {
                              val props = new Properties()
                              props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment")        
                            }

             val pipeline = new StanfordCoreNLP(nlpProps)
             val annotation = pipeline.process(strReview)
             var sentiments: ListBuffer[Double] = ListBuffer()
             var sizes: ListBuffer[Int] = ListBuffer()

              for (sentence <- annotation.get(classOf[CoreAnnotations.SentencesAnnotation])) {
              val tree = sentence.get(classOf[SentimentCoreAnnotations.SentimentAnnotatedTree])
              val sentiment = RNNCoreAnnotations.getPredictedClass(tree)
              
             sentiment match {
                case a if a > 0.0 && a < 2.5 => (strReview, "NEGATIVE")
                case a if a > 2.5 && a < 5.0 => (strReview,"POSITIVE")
                case a if a <= 0.0 || a > 5.0 => (strReview,"NOT_UNDERSTOOD")
              }
  
      }
             }
}