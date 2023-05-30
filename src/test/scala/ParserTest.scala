import puducalc._

import pudu.parser.generator._

class ParserTest extends munit.FunSuite {
  val strParser = SLRParserGenerator(ParserSpec).parser.compose(Lexer.lexer) // ...

  def nodeTest(str: String, expected: => CalcAST) =
    val result = strParser(str)
    assert(result.isRight)
    val ast = result.getOrElse(throw Exception())
    assertEquals(ast, expected)

  def getError(str: String) =
    val result = strParser(str)
    assert(result.isLeft)
    result.swap.getOrElse(throw Exception())

  test("assignment") {
    nodeTest(" x := 4", Assignment("x", ConstDouble(4d)))
  }

  test("simple binary op") {
    nodeTest("5 * x", Multiplication(ConstDouble(5d), Var("x")))
    nodeTest("1 + 1 ", Addition(ConstDouble(1d), ConstDouble(1d)))
    nodeTest("x - y", Subtraction(Var("x"), Var("y")))
    nodeTest(".123e-4 / __1", Division(ConstDouble(.123e-4), Var("__1")))
    nodeTest("x ^ y", Pow(Var("x"), Var("y")))
  }

  test("function call") {
    nodeTest("fn(2, 3, x, 5 ^ 7 - 8)", FuncCall("fn", Seq(ConstDouble(2), ConstDouble(3), Var("x"), Subtraction(Pow(ConstDouble(5), ConstDouble(7)), ConstDouble(8)))))
  }

  test("precedence") {
    nodeTest("- 2 + 3 * - 4 ^ 5", Addition(UMinus(ConstDouble(2d)), Multiplication(ConstDouble(3d), UMinus(Pow(ConstDouble(4), ConstDouble(5))))))
  }

  test("errors") {
    import pudu.parser._

    assert(getError("1 := 2 ").isInstanceOf[SyntaxError[_]])
    assert(getError("2 + * 4").isInstanceOf[SyntaxError[_]])
    assert(getError(" 2 * 4 +").isInstanceOf[InputEndedUnexpectedly])
    assert(getError(" fn(2,3,").isInstanceOf[InputEndedUnexpectedly])
  }

}
