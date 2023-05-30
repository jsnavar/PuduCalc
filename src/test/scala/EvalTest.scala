import puducalc._

import pudu.parser.generator._

class EvalTest extends munit.FunSuite {
  val strParser = SLRParserGenerator(ParserSpec).parser.compose(Lexer.lexer)

  def evalCtx(ctx: scala.collection.Map[String, Double])(str: String): Double =
    val eval = TreeEvaluator(ctx).eval
    strParser(str).map {
      case t: ExprTree => eval(t)
      case _ => throw Exception()
    }.getOrElse(throw Exception())

  test("without variables") {
    val eval = evalCtx(Map.empty)

    assertEquals(eval("128"), 128d)
    assertEquals(eval("2 + 3"), 5d)
    assertEquals(eval("10 - 20"), -10d)
    assertEquals(eval("3 * 4"), 12d)
    assertEquals(eval("10 / 4"), 2.5)
    assertEquals(eval("2 ^ 10"), 1024d)
    assertEquals(eval("sum(1,2,3)"), 6d)
    assertEquals(eval("max(4,2,-8,10,0)"), 10d)
    assertEquals(eval("min(4,2,-8,10,0)"), -8d)
    assertEquals(eval("avg(1,1,1,2,10)"), 3d)

    assertEquals(eval("2 * 4 - 2"), 6d)
    assertEquals(eval("sum(4,2,3^4)"), 87d)
  }

  test("variables") {
    val ctx = Map("x" -> 10d, "y" -> 20d)
    val eval = evalCtx(ctx)

    assertEquals(eval(" x + 4 - y"), -6d)
  }

  test("var not found") {
    intercept[VarNotFoundException] {
      evalCtx(Map.empty)("x")
    }
  }

  test("fn not found") {
    intercept[FunctionNotFoundException] {
      evalCtx(Map.empty)("x(2)")
    }
  }

}
