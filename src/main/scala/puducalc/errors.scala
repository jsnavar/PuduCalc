package puducalc

sealed trait ErrorMsg

object ErrorMsg:
  case class UndefinedFunction(id: String) extends ErrorMsg:
    override def toString = s"Undefined Function: $id"

  case class UndefinedVar(id: String) extends ErrorMsg:
    override def toString = s"Undefined Variable: $id"

  case class CannotRedefineId(id: String) extends ErrorMsg:
    override def toString = s"Can not redefine predefined id: $id"

  case class WrongNumberOfArgs(fn: String, expected: Int, found: Int) extends ErrorMsg:
    override def toString =
      if found < expected then
        s"Too few arguments for function $fn. Expected $expected but found ${found}"
      else if found > expected then
        s"Too many arguments for function $fn. Expected $expected but found ${found}"
      else throw Exception()

  case class RepeatedParameters(s: Iterable[String]) extends ErrorMsg:
    override def toString =
      s"Function has repeated parameters: ${s.mkString(", ")}"
