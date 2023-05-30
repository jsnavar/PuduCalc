import scala.collection.mutable.HashMap

import scala.io.StdIn
import scala.util.{Try, Success, Failure}

import puducalc._

import pudu.parser.generator._

@main def main: Unit =
  val parser = SLRParserGenerator(ParserSpec).parser
  val strParser = parser.compose(Lexer.lexer)

  val ctx = HashMap[String, Double]()

  val analize = TreeAnalizer(ctx).analize
  val evaluate = TreeEvaluator(ctx).eval

  val quitStr = ".quit"
  var lineNo = 0

  val mainIterator = Iterator.continually(StdIn.readLine())
    .takeWhile(_ != quitStr)
    .map(strParser(_))
    .tapEach {
      case Left(err) => println(s"Error: ${err.msg}")
      case _ => () }
    .collect { case Right(tree) => tree }
    .map {
      case t @ Assignment(id, expr) => (t, analize(expr))
      case t: ExprTree => (t, analize(t))
      case _: ExprSeq => throw Exception() }
    .tapEach { case (_, undef) =>
      if ! undef.isEmpty then
        println(s"Undefined variables or functions: $undef") }
    .filter { case (_, undef) => undef.isEmpty }
    .map { _._1 }

  for tree <- mainIterator do
    tree match
      case Assignment(v, expr) =>
        val res = evaluate(expr)
        ctx += v -> res
        println(s"Assigned '$v' := $res")
      case t: ExprTree =>
        val res = evaluate(t)
        lineNo += 1
        println(s"Result($lineNo) is $res")
      case _ => ()
