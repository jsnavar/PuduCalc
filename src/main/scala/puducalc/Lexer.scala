package puducalc

import pudu.lexer._

/** Lexer for PuduCalc. */
object Lexer extends Lexer[Token]:
  /* simple tokens */
  "\\(" { Token.LPar() }
  "\\)" { Token.RPar() }
  "\\+" { Token.Plus() }
  "\\-" { Token.Minus() }
  "\\*" { Token.Asterisk() }
  "\\^" { Token.Caret() }
  ","   { Token.Comma() }
  "/"   { Token.Slash() }
  ":="  { Token.Assign() }

  /* Literal doubles. Note that we define several regular expressions
   * for the same token, which may be useful in some cases */
  "Inf" { Token.Literal(Double.PositiveInfinity) }
  "NegInf" { Token.Literal(Double.NegativeInfinity) }
  "NaN" { Token.Literal(Double.NaN) }
  "-?([0-9]*\\.)?[0-9]+([eE]-?[0-9]+)?" { str => Token.Literal(str.toDouble) }

  /* identifiers are formed by _, letters and numbers, must start with a letter
   * or an _, and can not be a string of just _'s */
  val all = "[0-9a-zA-Z_]"
  s"[a-zA-Z]${all}*" { Token.Id(_) } // starts with a letter
  s"_${all}*[0-9a-zA-Z]${all}*" { Token.Id(_) } // starts with _

  /* as PuduCalc is line oriented, newline is treated as an error */
  "[ \t]+".ignore
  "." { Token.ERROR() }

  override val eof = Token.EOF()
