package puducalc

object Predefined:
  val functions: Map[String, TypeRepr] = Map("min" -> TypeRepr.VarFunction,
                                             "max" -> TypeRepr.VarFunction,
                                             "sum" -> TypeRepr.VarFunction,
                                             "avg" -> TypeRepr.VarFunction,
                                             "sqrt" -> TypeRepr.Function(List(TypeRepr.Num), TypeRepr.Num),
                                             "log2" -> TypeRepr.Function(List(TypeRepr.Num), TypeRepr.Num),
                                             "exp" -> TypeRepr.Function(List(TypeRepr.Num), TypeRepr.Num))
  val constants: Set[String] = Set("Pi", "E")
