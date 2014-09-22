package com.gu.util.liveblogs.lib

object Strings {
  val BreakingPunctuation = ".,;!?:-"

  implicit class RichString(string: String) {
    def endsWithOneOf(chars: TraversableOnce[Char]) = string.lastOption exists { last => chars.exists(_ == last) }

    def endsWithBreaking = endsWithOneOf(BreakingPunctuation)

    /** Trim that takes into account non-breaking spaces */
    def unicodeTrim = string.substring(string.indexWhere(!_.isSpaceChar), string.lastIndexWhere(!_.isSpaceChar) + 1)
  }
}