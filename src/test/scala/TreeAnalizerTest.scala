import puducalc._

import pudu.parser.generator._

class TreeAnalizerTest extends munit.FunSuite {
  val strParser = SLRParserGenerator(ParserSpec).parser.compose(Lexer.lexer)

  def analizeCtx(ctx: scala.collection.Map[String, Double])(str: String): UndefinedIds =
    val analize = TreeAnalizer(ctx).analize
    strParser(str).map {
      case t: ExprTree => analize(t)
      case _ => throw Exception()
    }.getOrElse(throw Exception())

  test("no ids") {
    assertEquals(analizeCtx(Map.empty)("2 * 3"), UndefinedIds.empty)
  }

  test("var") {
    val ctx = Map("x" -> 3d)
    assertEquals(analizeCtx(ctx)("x * y"), UndefinedIds(Set("y"), Set.empty))
    assertEquals(analizeCtx(ctx)("x + avg(2,3,x,t,3^z)"), UndefinedIds(Set("t", "z"), Set.empty))
  }

  test("fn") {
    val ctx = Map("x" -> 3d)
    assertEquals(analizeCtx(ctx)("x(x) + 3"), UndefinedIds(Set.empty, Set("x")))
  }
}
