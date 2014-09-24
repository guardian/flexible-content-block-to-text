package com.gu.util.liveblogs

import org.scalatest.{Matchers, FlatSpec}

class BlockToTextSpec extends FlatSpec with Matchers with ResourcesHelper {
  def expectTransformation(before: String, after: String) =
    BlockToText.unapply(before) shouldEqual Some(after)

  "apply" should "ensure at least one space between paragraphs" in {
    expectTransformation("<p>First paragraph.</p><p>Second paragraph.</p>", "First paragraph. Second paragraph.")
  }

  it should "ensure a full stop at the end of paragraphs that have no punctuation at the end" in {
    expectTransformation("<p>A paragraph without a full stop</p>", "A paragraph without a full stop.")
  }

  it should "not modify paragraphs that already have some kind of breaking punctuation at the end" in {
    val okParagraphs = Seq(
      "<p>Hello!</p>" -> "Hello!",
      "<p>Behold:</p>" -> "Behold:",
      "<p>What?</p>" -> "What?",
      "<p>Enough.</p>" -> "Enough."
    )

    for {
      (before, after) <- okParagraphs
    } yield expectTransformation(before, after)
  }

  it should "ensure at least one space between breaks" in {
    expectTransformation("<p>foo<br />bar.</p>", "foo bar.")
  }

  it should "put pipes between headers and further text" in {
    expectTransformation(
      "<h2>David Cameron at PMQs</h2><p>I will be focusing on PMQs for the next.</p>",
      "David Cameron at PMQs | I will be focusing on PMQs for the next."
    )
  }

  it should "format lists nicely" in {
    expectTransformation(
      "<ul><li>Falafel</li><li>Cheese</li><li>Hummus</li></ul>",
      "Falafel; Cheese; Hummus."
    )

    expectTransformation(
      "<ol><li>First item</li><li>Second</li></ol>",
      "First item; Second."
    )
  }

  it should "retain content not contained in an outer tag" in {
    expectTransformation(
      "• Dutchman shared in Manchester United success<br />• Club hails 'one of best coaches in the world'",
      "• Dutchman shared in Manchester United success • Club hails 'one of best coaches in the world'"
    )
  }

  it should "ignore image captions" in {
    expectTransformation(slurpOrDie("tnt-express.html"), "Dublin house prices up 25% | Houses in Dublin are now almost 25% more than a year ago as the Republic’s property market continues to recover. Henry McDonald writes: Ireland’s Central Statistic Office has revealed that house prices in the capital are 23.1 per cent higher than 2013. The CSO also report on Wednesday that apartment price are also on the rise with the cost a flat now more than 26 per cent compared to this time last year. Overall property prices are 2.3 per cent higher across the Republic than they were last year reflecting the strength of the Dublin property market compared to areas beyond the capital. But the CSO latest report also puts these latest rises in some proper context. It notes that house prices in Dublin are still nearly 40 per cent lower than their 2007 peak when the Celtic Tiger was still roaring. Apartments in the capital are still around 46 per cent lower than in the boom times. Nationally the CSO said residential properties in all other parts of Ireland are 45 per cent lower than in 2007. Those with long memories, though, may worry, Henry concludes: It was the nation’s obsession with rising property prices that fuelled an overheated market, prompting speculators and builders to borrow billions from Irish banks that in large part helped bust the Irish economy.")
  }
}
