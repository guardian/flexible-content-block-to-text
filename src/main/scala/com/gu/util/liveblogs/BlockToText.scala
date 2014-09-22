package com.gu.util.liveblogs

import org.jsoup.Jsoup
import lib.JSoup._
import lib.Strings._
import scala.collection.JavaConversions._
import org.jsoup.nodes.Document
import scala.util.Try

object BlockToText {
  /** Ensures all paragraphs and breaks have at least one space at the end for padding purposes. When we convert to
    * text later, if there is no padding, paragraphs will run on. e.g.,
    *
    * <p>hello<p><p>world</p> -> "helloworld"
    *
    * @param doc The JSoup Document
    */
  private def addPaddingToBreaks(doc: Document) {
    for (paragraph <- doc.select("p").iterator()) {
      val withPadding = paragraph.text() + " "
      paragraph.text(withPadding)
    }

    for (break <- doc.select("br").iterator()) {
      break.after(" ")
    }
  }

  /** Adds a pipe after headers, so it looks like something leading into the text. e.g.,
    *
    * <h2>David Cameron at PMQs</h2>
    * <p>I will be focusing on ... </p>
    *
    * -> "David Cameron at PMQs | I will be focusing on ..."
    *
    * @param doc The JSoup Document
    */
  private def addPipesAfterHeaders(doc: Document) {
    for (header <- doc.select("h2").iterator()) {
      val text = header.text.unicodeTrim
      header.text(if (!text.endsWithBreaking) text + " | " else text)
    }
  }

  /** Adds full stops to the end of paragraphs that lack terminal punctuation.
    *
    * @param doc The JSoup Document
    */
  private def addFullStops(doc: Document) {
    for (paragraph <- doc.select("p").iterator()) {
      val text = paragraph.text.unicodeTrim

      if (!text.endsWithBreaking) {
        paragraph.text(text + ". ")
      }
    }
  }

  /** Transform HTML lists into text items separated by semicolons and ending in a full stop:
    *
    * <ul>
    *   <li>A list item</li>
    *   <li>Another</li>
    *   <li>And a final</li>
    * <ul>
    *
    * -> "A list item; Another; And a final."
    *
    * Full stop must be applied before semicolons (as these are indiscriminately applied to list items).
    *
    * @param doc The JSoup Document
    */
  private def punctuateLists(doc: Document) {
    for (list <- doc.select("ul, ol").iterator()) {
      val items = list.select("li").iterator().toList

      items.init foreach { item =>
        val text = item.text.unicodeTrim

        item.text(if (text.endsWithBreaking) text + " " else text + "; ")
      }

      items.lastOption foreach { item =>
        val text = item.text.unicodeTrim

        item.text(if (text.endsWithBreaking) text else text + ".")
      }
    }
  }

  def apply(html: String) = Try {
    val doc = Jsoup.parse(html)

    addPaddingToBreaks(doc)
    addPipesAfterHeaders(doc)
    addFullStops(doc)
    punctuateLists(doc)

    doc.bodyText.replaceAll("""\s{2,}""", " ").trim
  }.toOption

  def unapply(html: String) = apply(html)
}
