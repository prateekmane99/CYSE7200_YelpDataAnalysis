package com.edu.neu.coe.analysis.compare

import com.itextpdf.text.Document
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.tool.xml.XMLWorkerHelper
import com.itextpdf.text.Element;
import java.io.FileOutputStream;
import java.io.StringReader
import scala.xml.Elem
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

object GeneratePdf {
  val rdd = {
    val r = Compare.getRDD
    println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeee\n"+r.count())
    r
  }

  def mapRDD(filePath: String) = {
    println("generating pdf files")
    rdd.foreach { x => //TO loop each row of RDD
    gennerate(x, filePath)
    }
  }
  
  def gennerate(x:Compare.Comparison, filePath: String) = {
    val htmlReport =

  <html>
        <head>
          <title>My report</title>
          <link rel="stylesheet" href="default.css"/>
        </head>
        <body>
	         <center><h1>Business Review Report</h1></center>
          <h4>Business Analysis:</h4>
          <p> { "Name - " + x.businessName } </p>
          <p> { "State - " + x.businessState } </p>
          <p> { "City - " + x.businessCity } </p>
          <p> { "Total Good Reviews - " + x.goodReviewNum } </p>
          <p> { "Total Bad Reviews -  " + x.badReviewNum } </p>
          <p> { "Best Reviews      -  " + x.bestReview } </p>
          <p> { "Bad Reviews       -  " + "no" } </p>
          <br/>
          <h4> Business Competitors Analysis: </h4>
        </body>
      </html>

    val document = new Document(PageSize.A4)
//    println("trying to transfer it to pdf")
    val t = try {
      val pdfWriter = PdfWriter.getInstance(
        document,
        new FileOutputStream(filePath +"/" + x.bid + ".pdf"))
//      println("generating pdf")
      document.open()
      val worker = XMLWorkerHelper.getInstance()
      worker.parseXHtml(
        pdfWriter,
        document,
        new StringReader(htmlReport.toString))

      val tbl = new PdfPTable(5);

      tbl.addCell("Business");
      tbl.addCell("Distance(miles)");
      tbl.addCell("Good");
      tbl.addCell("Bad");
      tbl.addCell("Best")
      tbl.setHeaderRows(1);

      x.competetors.foreach { c =>//To loop each bad and good review

        def get(x: Iterable[(String,String)]): String = x match {
          case Nil => ""
          case x::list  =>  x._1+"  \n"+get(list)
        }
        
        val cl1 = new PdfPCell(new Paragraph(c._1))
        val cl2 = new PdfPCell(new Paragraph(c._2.toString().concat("0000").substring(0, 3)))
        val cl3 = new PdfPCell(new Paragraph(get(c._3) match {
          case ""  => "No reviews yet"
          case s   =>  s
        }))
        val cl4 = new PdfPCell(new Paragraph(get(c._4) match {
          case ""  => "No reviews yet"
          case s   =>  s
        }))
        val cl5 = new PdfPCell(new Paragraph(c._5))

        
        tbl.addCell(cl1);
        tbl.addCell(cl2);
        tbl.addCell(cl3);
        tbl.addCell(cl4);
        tbl.addCell(cl5)

      }
      document.add(tbl);

    } finally {
      document.close
    }
  }
 
  
}