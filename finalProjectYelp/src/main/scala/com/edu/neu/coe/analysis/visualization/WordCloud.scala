package com.edu.neu.coe.analysis.visualization

import com.edu.neu.coe.analysis.clean.DataParsingUtil
import com.edu.neu.coe.analysis.model.Business

import wordcloud.PolarWordCloud
import wordcloud.PolarBlendMode
import wordcloud.CollisionMode
import wordcloud.bg.CircleBackground
import wordcloud.font.FontWeight
import wordcloud.font.scale.SqrtFontScalar
import wordcloud.nlp.FrequencyAnalyzer
import com.twitter.chill.Base64.InputStream
import java.util.Properties
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.trees.Tree
import edu.stanford.nlp.util.CoreMap
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import scala.collection.mutable.WrappedArray
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

  val nlpProps = {
    val props = new Properties()
    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment")
    props
  }

  val pipeline = new StanfordCoreNLP(nlpProps)
  var sentiments: ListBuffer[Double] = ListBuffer()
  var sizes: ListBuffer[Int] = ListBuffer()

  def getWordCloud() = {
    val frequencyAnalizer = new FrequencyAnalyzer()
    frequencyAnalizer.setWordFrequencesToReturn(750)
    val file = new java.io.File("/Users/YuanHank/Desktop/test/part-00000.txt")
    val stream = new java.io.FileInputStream(file)
    val wordFrequencies  = frequencyAnalizer.load (stream)
    val wordCloud = new PolarWordCloud(600, 600, CollisionMode.PIXEL_PERFECT, PolarBlendMode.BLUR)
    wordCloud.setPadding(2)
    wordCloud.setBackground(new CircleBackground(300))
    wordCloud.setFontScalar(new SqrtFontScalar(10, 40))
    wordCloud.build(wordFrequencies)

    //val baos = new ByteArrayOutputStream()
    //ImageIO.write(wordCloud.getBufferedImage(), "png", baos)
    //val image = new String(Base64.encodeBase64(baos.toByteArray()))

    wordCloud.writeToFile("/Users/YuanHank/Desktop/test/test.png");
  }

  def getSentiment(strReview: String) = {
    val annotation = pipeline.process(strReview)
    
    val sentence = annotation.get(classOf[CoreAnnotations.SentencesAnnotation]).get(0)
    val tree = sentence.get(classOf[SentimentCoreAnnotations.AnnotatedTree])
    val sentiment = RNNCoreAnnotations.getPredictedClass(tree)
    
    sentiment match {
      case a if a >= 0 && a <= 1  => (strReview, "NEGATIVE")
      case a if a == 2  => (strReview, "NEUTRAL")
      case a if a > 2 && a <= 4  => (strReview, "POSITIVE")
      case a if a < 0 || a > 4 => (strReview, "NOT_UNDERSTOOD")
    }

  }

  //A lot of things, bigram, sentiment
  def getSentimentWithAll = {
    val bigrams = DataParsingUtil.getBigramWithAll
    for(bigram <- bigrams; bi <- bigram._2) yield (bigram._1, getSentiment(bi))
  }
  def outputSentimentWithAll(fileName: String) = {
    val rd = getSentimentWithAll.groupBy(y => y._1).map(f => (f._1,f._2.map(z => z._2)))
//    .flatMap(f => (f._1:+f._2._1:+f._2._2))
//    rd.foreach(f => println(f))
    rd
  }
  def outputSentiment(fileName: String) = {
    getSentimentWithAll.map { x => x._2.toString() }.saveAsTextFile(fileName)
  }
  def outputSentimentWithId(fileName: String) = {
     getSentimentWithAll.map { x => (x._1(0).toString()+", "+x._2.toString()) }.saveAsTextFile(fileName)
  }
  
  def getRddBusiness = {
    outputSentimentWithAll("").map { x => new Business(x._1(0).toString(),x._1(1).toString(),x._1(2).asInstanceOf[WrappedArray[String]],x._1(3).toString(),
        x._1(4).toString(),x._1(5).toString(),x._1(6).asInstanceOf[Double],x._1(7).asInstanceOf[Double],x._2) }
  }
}