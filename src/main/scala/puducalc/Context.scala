package puducalc

/** Mutable context. Contains user defined variables and functions */
class Context(var vars: Map[String, Double], var functions: Map[String, FunDef]):
  def addVar(id: String, v: Double) =
    vars += (id -> v)
    this
  def addFn(id: String, f: FunDef) =
    functions += (id -> f)
    this
  def withPairs(pairs: Map[String, Double]) = Context(vars ++ pairs, functions)

object Context:
  def empty = Context(Map.empty, Map.empty)
