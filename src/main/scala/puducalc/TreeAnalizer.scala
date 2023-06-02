package puducalc

/** Analizes a tree collecting error messages */
object TreeAnalizer:
  import ErrorMsg._

  def check(tree: CalcAST)(using ctx: Context): Set[ErrorMsg] = tree match
    case Assignment(id, expr) =>
      check(expr) ++
      (if Predefined.constants.contains(id) then Set(CannotRedefineId(id))
      else Set.empty)
    case FunDef(id,args,expr) =>
      check(expr)(using ctx.withPairs(args.map(_ -> Double.NaN).toMap)) ++
      (if Predefined.functions.contains(id) then Set(CannotRedefineId(id))
      else Set.empty)

    case IdSeq(_) => throw Exception()
    case ExprSeq(_) => throw Exception()

    case ConstDouble(_) => Set.empty
    case Var(id) =>
      if !ctx.vars.contains(id) && !Predefined.constants.contains(id) then Set(UndefinedVar(id))
      else Set.empty
    case Addition(l, r) => check(l) ++ check(r)
    case Subtraction(l, r) => check(l) ++ check(r)
    case Multiplication(l, r) => check(l) ++ check(r)
    case Division(l, r) => check(l) ++ check(r)
    case Pow(l, r) => check(l) ++ check(r)
    case UMinus(expr) => check(expr)
    case FuncCall(fn, args) =>
      var argsErrors = args.map(check).reduce(_ ++ _)
      val numArgs = Predefined.functions.get(fn) match
        case Some(TypeRepr.Num) => throw Exception()
        case Some(TypeRepr.VarFunction) => Set.empty
        case Some(TypeRepr.Function(defArgs, ret)) =>
          if args.size != defArgs.size then
            Set(WrongNumberOfArgs(fn, defArgs.size, args.size))
          else Set.empty
        case None =>
          if ctx.functions.contains(fn) then
            val expected = ctx.functions(fn).args.size
            val found = args.size
            if expected != found then Set(WrongNumberOfArgs(fn, expected, found)) else Set.empty
          else Set(UndefinedFunction(fn))
      argsErrors ++ numArgs
