import puducalc._

import pudu.parser.generator._

class TreeAnalizerTest extends munit.FunSuite {
  val strParser = SLRParserGenerator(ParserSpec).parser.compose(Lexer.lexer)

  def analizeCtx(ctx: Context)(str: String): Set[ErrorMsg] =
    strParser(str).map {
      case t: ExprTree => TreeAnalizer.check(t)(using ctx)
      case _ => throw Exception()
    }.getOrElse(throw Exception())

  test("no ids") {
    assertEquals(analizeCtx(Context.empty)("2 * 3"), Set.empty)
  }

  test("var") {
    import ErrorMsg._

    val ctx = Context.empty.addVar("x", 3d)
    assertEquals(analizeCtx(ctx)("x * y"), Set(UndefinedVar("y")))
    assertEquals(analizeCtx(ctx)("x + avg(2,3,x,t,3^z)"), Set(UndefinedVar("t"), UndefinedVar("z")))
  }

  test("fn") {
    import ErrorMsg._

    val ctx = Context.empty.addVar("x", 3d)
    assertEquals(analizeCtx(ctx)("x(x) + 3"), Set(UndefinedFunction("x")))
  }
}
