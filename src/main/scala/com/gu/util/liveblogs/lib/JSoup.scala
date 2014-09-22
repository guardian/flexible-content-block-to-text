package com.gu.util.liveblogs.lib

import org.jsoup.nodes.Document

object JSoup {
  implicit class RichDocument(doc: Document) {
    def bodyContents = doc.select("body").first().html()

    def bodyText = doc.select("body").first().text()
  }
}
