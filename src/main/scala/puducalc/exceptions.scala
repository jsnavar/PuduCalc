package puducalc

case class VarNotFoundException(id: String) extends Exception

case class FunctionNotFoundException(id: String) extends Exception
