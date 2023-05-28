import pudu.lexer._

/** Lexer for PuduCalc. */
object CalcLexer extends Lexer[CalcToken]:
  "\\(" { CalcToken.LPar() }
  "\\)" { CalcToken.RPar() }
  "\\+" { CalcToken.Plus() }
  "\\-" { CalcToken.Minus() }
  "\\*" { CalcToken.Asterisk() }
  "\\^" { CalcToken.Caret() }
  "," { CalcToken.Comma() }
  "/" { CalcToken.Slash() }
  ":=" { CalcToken.Assign() }

  /* Literal doubles. Note that we define several regular expressions
   * for the same token, which can be useful in complex cases */
  "Inf" { CalcToken.Literal(Double.PositiveInfinity) }
  "NegInf" { CalcToken.Literal(Double.NegativeInfinity) }
  "NaN" { CalcToken.Literal(Double.NaN) }
  "-?([0-9]*\\.)?[0-9]+([eE]-?[0-9]+)?" { str => CalcToken.Literal(str.toDouble) }

  /* identifiers are formed by _, letters and numbers, but strings of only numbers
   * or _'s are not allowed. */
  val all = "[a-zA-Z0-9_]"
  s"${all}*[a-zA-Z]${all}*" { CalcToken.Id(_) } // at least one letter
  s"${all}*_${all}*[0-9]${all}*" { CalcToken.Id(_) } // at least one _ followed somewhere by a number
  s"${all}*[0-9]${all}*_${all}*" { CalcToken.Id(_) } // at least one number followed somewhere by an _

  /* as PuduCalc is line oriented, we treat newline as an error */
  "[ \t]+".ignore
  "." { CalcToken.ERROR() }

  override val eof = CalcToken.EOF()
