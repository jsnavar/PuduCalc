package puducalc

import pudu.lexer._

/** Lexer for PuduCalc. */
object Lexer extends Lexer[Token]:
  "\\(" { Token.LPar() }
  "\\)" { Token.RPar() }
  "\\+" { Token.Plus() }
  "\\-" { Token.Minus() }
  "\\*" { Token.Asterisk() }
  "\\^" { Token.Caret() }
  "," { Token.Comma() }
  "/" { Token.Slash() }
  ":=" { Token.Assign() }

  /* Literal doubles. Note that we define several regular expressions
   * for the same token, which can be useful in complex cases */
  "Inf" { Token.Literal(Double.PositiveInfinity) }
  "NegInf" { Token.Literal(Double.NegativeInfinity) }
  "NaN" { Token.Literal(Double.NaN) }
  "-?([0-9]*\\.)?[0-9]+([eE]-?[0-9]+)?" { str => Token.Literal(str.toDouble) }

  /* identifiers are formed by _, letters and numbers, but strings of only numbers
   * or _'s are not allowed. */
  val all = "[a-zA-Z0-9_]"
  s"${all}*[a-zA-Z]${all}*" { Token.Id(_) } // at least one letter
  "[0-9_]*_[0-9_]*[0-9][0-9_]*" { Token.Id(_) } // no letters, at least one _ followed somewhere by a number
  "[0-9]+_+" { Token.Id(_) }

  /* as PuduCalc is line oriented, we treat newline as an error */
  "[ \t]+".ignore
  "." { Token.ERROR() }

  override val eof = Token.EOF()
