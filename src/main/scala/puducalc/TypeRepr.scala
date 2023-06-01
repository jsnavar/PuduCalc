package puducalc


sealed trait TypeRepr

object TypeRepr:
  object Num extends TypeRepr
  object VarFunction extends TypeRepr
  case class Function(args: List[TypeRepr], ret: TypeRepr) extends TypeRepr
