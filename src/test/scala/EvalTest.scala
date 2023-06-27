import puducalc._

import pudu.parser.generator._

class EvalTest extends munit.FunSuite {
  val strParser = SLRParserGenerator(ParserSpec.grammar).parser.compose(Lexer.lexer)

  def evalCtx(ctx: Context)(str: String): Double =
    strParser(str).map {
      case t: ExprTree => TreeEvaluator.eval(t)(using ctx)
      case _ => throw Exception()
    }.getOrElse(throw Exception())

  test("without variables") {
    val eval = evalCtx(Context.empty)

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
    val ctx = Context.empty.addVar("x", 10d).addVar("y", 20d)
    val eval = evalCtx(ctx)

    assertEquals(eval(" x + 4 - y"), -6d)
  }

  test("var not found") {
    intercept[VarNotFoundException] {
      evalCtx(Context.empty)("x")
    }
  }

  test("fn not found") {
    intercept[FunctionNotFoundException] {
      evalCtx(Context.empty)("x(2)")
    }
  }

}
