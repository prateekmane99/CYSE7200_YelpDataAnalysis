package com.edu.neu.coe.analysis.visualization

import wordcloud.PolarWordCloud
import wordcloud.PolarBlendMode
import wordcloud.CollisionMode
import wordcloud.bg.CircleBackground
import wordcloud.font.FontWeight
import wordcloud.font.scale.SqrtFontScalar
import wordcloud.nlp.FrequencyAnalizer
import com.twitter.chill.Base64.InputStream

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
val wordFrequencies  = frequencyAnalizer.load(aaa.getInputStream("/Users/Prateek/Documents/Data Analytics/Big Data/Spark/What is Spark"))
val wordCloud = new PolarWordCloud(600, 600, CollisionMode.PIXEL_PERFECT, PolarBlendMode.BLUR)
wordCloud.setPadding(2)
wordCloud.setBackground(new CircleBackground(300))
wordCloud.setFontScalar(new SqrtFontScalar(10, 40))
wordCloud.build(wordFrequencies)

//val baos = new ByteArrayOutputStream()
//ImageIO.write(wordCloud.getBufferedImage(), "png", baos)
//val image = new String(Base64.encodeBase64(baos.toByteArray()))

wordCloud.writeToFile("/Users/Prateek/Documents/Data Analytics/Sparkwhale_wordcloud_small.png");
  }

}