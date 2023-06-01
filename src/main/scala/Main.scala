import scala.io.StdIn
import puducalc._

import pudu.parser.generator._

@main def main: Unit =
  val parser = SLRParserGenerator(ParserSpec).parser
  val strParser = parser.compose(Lexer.lexer)

  val ctx = Context.empty

  val evaluate = TreeEvaluator(ctx).eval

  val quitStr = ".quit"
  var lineNo = 0

  val mainIterator = Iterator.continually(StdIn.readLine())
    .takeWhile(_ != quitStr)
    .map(strParser(_))
    .tapEach {
      case Left(err) => println(s"Error: ${err.msg}")
      case _ => () }
    .collect { case Right(tree) =>
                 (tree, TreeAnalizer.check(tree)(using ctx)) }
    .tapEach { case (_, msgs) =>
      if !msgs.isEmpty then msgs.foreach(println) }
    .filter { case (_, msgs) => msgs.isEmpty }
    .map { _._1 }

  for tree <- mainIterator do
    tree match
      case Assignment(v, expr) =>
        val res = evaluate(expr)
        ctx.addVar(v, res)
        println(s"Assigned '$v' := $res")
      case t: ExprTree =>
        val res = evaluate(t)
        lineNo += 1
        println(s"Result($lineNo) is $res")
      case _ => ()
