package com.gu.util.liveblogs

import org.scalatest.{Matchers, FlatSpec}

class BlockToTextSpec extends FlatSpec with Matchers {
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
}
