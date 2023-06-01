package puducalc

/** Mutable context. Contains user defined variables and functions */
class Context(var vars: Map[String, Double], var functions: Map[String, FunDef]):
  def addVar(id: String, v: Double) =
    vars += (id -> v)
    this
  def addFn(id: String, f: FunDef) =
    functions += (id -> f)
    this
  def withIds(ids: Set[String]) = Context(vars ++ ids.map(_ -> 0d), functions)

object Context:
  def empty = Context(Map.empty, Map.empty)
