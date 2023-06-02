import pudu.lexer._

import puducalc._

class CalcLexerTest extends munit.FunSuite {

  val lexer = puducalc.Lexer.lexer

  def checkToken(lexerInput: String, token: Token) =
    assertEquals(lexer(lexerInput).next, token)

  def checkSeq(lexerInput: String, tokens: Seq[Token]) =
    assertEquals(lexer(lexerInput).toSeq, tokens)

  test("simple operators") {
    checkToken("(", Token.LPar())
    checkToken(")", Token.RPar())
    checkToken("+", Token.Plus())
    checkToken("-", Token.Minus())
    checkToken("*", Token.Asterisk())
    checkToken("^", Token.Caret())
    checkToken(",", Token.Comma())
    checkToken("/", Token.Slash())
    checkToken(":=", Token.Assign())
  }

  test("special doubles") {
    checkToken("Inf", Token.Literal(Double.PositiveInfinity))
    checkToken("NegInf", Token.Literal(Double.NegativeInfinity))

    // NaN is not equal to NaN, so we can't compare them directly
    val lexerNaN = lexer("NaN").next
    assert(lexerNaN.isInstanceOf[Token.Literal])
    val value = lexerNaN.asInstanceOf[Token.Literal].value
    assert(value.isNaN)
  }

  test("double without exponent") {
    checkToken("000", Token.Literal(0d))
    checkToken("30", Token.Literal(30d))
    checkToken("-10", Token.Literal(-10d))
    checkToken(".20", Token.Literal(0.2))
    checkToken("6.40", Token.Literal(6.4))
    checkToken("0007.80", Token.Literal(7.8))
    checkToken("-0003.1327", Token.Literal(-3.1327))
    checkToken("-123456789.000000", Token.Literal(-123456789d))
    checkToken("-.132", Token.Literal(-.132))
  }

  test("double with exponent") {
    // lowercase e
    checkToken("12e3", Token.Literal(12000d))
    checkToken("5e-31", Token.Literal(5E-31))

    // uppercase e
    checkToken("84E51", Token.Literal(84E51))
    checkToken("84E-53", Token.Literal(84E-53))
  }

  test("identifiers") {
    checkToken("id", Token.Id("id"))
    checkToken("ID", Token.Id("ID"))
    checkToken("Id", Token.Id("Id"))
    checkToken("iD", Token.Id("iD"))
    checkToken("_123__3", Token.Id("_123__3"))
    checkToken("____1", Token.Id("____1"))
    checkToken("x____1", Token.Id("x____1"))
    checkToken("x____1", Token.Id("x____1"))
  }

  test("whitespace") {
    checkSeq("2 +   \t  _1  ", Seq(Token.Literal(2), Token.Plus(), Token.Id("_1"), Token.EOF()))
  }
}
