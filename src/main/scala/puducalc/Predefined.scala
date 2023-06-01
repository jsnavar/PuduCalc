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

  def constantValue(id: String) = id match
    case "Pi" => math.Pi
    case "E" => math.E
    case _ => throw Exception()

  def evalFn(id: String, args: Seq[Double]) = id match
    case "sum" => args.sum
    case "max" => args.max
    case "min" => args.min
    case "avg" => args.sum / args.size
    case "sqrt" => math.sqrt(args.head)
    case "log2" => math.log(args.head) / math.log(2d)
    case "exp" => math.exp(args.head)
    case _ => throw Exception()
