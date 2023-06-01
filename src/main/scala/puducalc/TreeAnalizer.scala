package puducalc

/** Sets of undefined variables and functions */
case class UndefinedIds(vars: Set[String], functions: Set[String]):
  def addVar(v: String) = UndefinedIds(vars + v, functions)
  def addFn(fn: String) = UndefinedIds(vars, functions + fn)

  def ++(that: UndefinedIds) = UndefinedIds(vars ++ that.vars, functions ++ that.functions)
  def isEmpty = vars.isEmpty && functions.isEmpty

  override def toString =
    def setToString(name: String, set: Set[String]) =
      require(!set.isEmpty)
      val setStr = set.mkString(",")
      s"$name: <$setStr>"
    def varsStr = setToString("variables", vars)
    def funcStr = setToString("functions", functions)
    if isEmpty then ""
    else if vars.isEmpty then funcStr
    else if functions.isEmpty then varsStr
    else s"$varsStr - $funcStr"

object UndefinedIds:
  def empty = UndefinedIds(Set.empty, Set.empty)

/** Collects all undefined variables and functions */
class TreeAnalizer(ctx: scala.collection.Map[String, Double]):
  def analize: ExprTree => UndefinedIds =
    case ConstDouble(d) => UndefinedIds.empty
    case Var(id) => if !Predefined.constants.contains(id) && !ctx.contains(id) then UndefinedIds(Set(id), Set.empty)
                    else UndefinedIds.empty
    case Addition(l, r) => analize(l) ++ analize(r)
    case Subtraction(l, r) => analize(l) ++ analize(r)
    case Multiplication(l, r) => analize(l) ++ analize(r)
    case Division(l, r) => analize(l) ++ analize(r)
    case Pow(l, r) => analize(l) ++ analize(r)
    case UMinus(expr) => analize(expr)
    case FuncCall(fn, args) =>
      val argsUndefIds = args.map(analize).reduce(_ ++ _)
      if !Predefined.functions.contains(fn) then argsUndefIds.addFn(fn) else argsUndefIds

  def typeCheck: CalcAST => Set[String] =
    case Assignment(id,_) => if Predefined.constants.contains(id) then Set(s"Can not redefined constant $id")
                             else Set.empty
    case ExprSeq(_) => throw Exception()
    case ConstDouble(_) => Set.empty
    case Var(_) => Set.empty
    case Addition(l, r) => typeCheck(l) ++ typeCheck(r)
    case Subtraction(l, r) => typeCheck(l) ++ typeCheck(r)
    case Multiplication(l, r) => typeCheck(l) ++ typeCheck(r)
    case Division(l, r) => typeCheck(l) ++ typeCheck(r)
    case Pow(l, r) => typeCheck(l) ++ typeCheck(r)
    case UMinus(expr) => typeCheck(expr)
    case FuncCall(fn, args) =>
      Predefined.functions(fn) match
        case TypeRepr.Num => throw Exception()
        case TypeRepr.VarFunction => Set.empty
        case TypeRepr.Function(defArgs, ret) =>
          if args.size < defArgs.size then
            Set(s"Too few arguments for function $fn. Expected ${defArgs.size} but found ${args.size}")
          else if args.size > defArgs.size then
            Set(s"Too many arguments for function $fn. Expected ${defArgs.size} but found ${args.size}")
          else Set.empty
