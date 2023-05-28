import pudu.lexer._

class CalcLexerTest extends munit.FunSuite {

  val lexer = CalcLexer.lexer

  def checkToken(lexerInput: String, token: CalcToken) =
    assertEquals(lexer(lexerInput).next, token)

  def checkSeq(lexerInput: String, tokens: Seq[CalcToken]) =
    assertEquals(lexer(lexerInput).toSeq, tokens)

  test("simple operators") {
    checkToken("(", CalcToken.LPar())
    checkToken(")", CalcToken.RPar())
    checkToken("+", CalcToken.Plus())
    checkToken("-", CalcToken.Minus())
    checkToken("*", CalcToken.Asterisk())
    checkToken("^", CalcToken.Caret())
    checkToken(",", CalcToken.Comma())
    checkToken("/", CalcToken.Slash())
    checkToken(":=", CalcToken.Assign())
  }

  test("special doubles") {
    checkToken("Inf", CalcToken.Literal(Double.PositiveInfinity))
    checkToken("NegInf", CalcToken.Literal(Double.NegativeInfinity))

    // NaN is not equal to NaN, so we can't compare them directly
    val lexerNaN = lexer("NaN").next
    assert(lexerNaN.isInstanceOf[CalcToken.Literal])
    val value = lexerNaN.asInstanceOf[CalcToken.Literal].value
    assert(value.isNaN)
  }

  test("double without exponent") {
    checkToken("000", CalcToken.Literal(0d))
    checkToken("30", CalcToken.Literal(30d))
    checkToken("-10", CalcToken.Literal(-10d))
    checkToken(".20", CalcToken.Literal(0.2))
    checkToken("6.40", CalcToken.Literal(6.4))
    checkToken("0007.80", CalcToken.Literal(7.8))
    checkToken("-0003.1327", CalcToken.Literal(-3.1327))
    checkToken("-123456789.000000", CalcToken.Literal(-123456789d))
    checkToken("-.132", CalcToken.Literal(-.132))
  }

  test("double with exponent") {
    // lowercase e
    checkToken("12e3", CalcToken.Literal(12000d))
    checkToken("5e-31", CalcToken.Literal(5E-31))

    // uppercase e
    checkToken("84E51", CalcToken.Literal(84E51))
    checkToken("84E-53", CalcToken.Literal(84E-53))
  }

  test("identifiers") {
    checkToken("id", CalcToken.Id("id"))
    checkToken("ID", CalcToken.Id("ID"))
    checkToken("Id", CalcToken.Id("Id"))
    checkToken("iD", CalcToken.Id("iD"))
    checkToken("____1", CalcToken.Id("____1"))
    checkToken("x____1", CalcToken.Id("x____1"))
    checkToken("x____1", CalcToken.Id("x____1"))
    checkToken("1strXYZ_", CalcToken.Id("1strXYZ_"))
  }

  test("whitespace") {
    checkSeq("2 +   \t  _1", Seq(CalcToken.Literal(2), CalcToken.Plus(), CalcToken.Id("_1"), CalcToken.EOF()))
  }
}
