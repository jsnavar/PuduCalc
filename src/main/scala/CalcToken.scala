

/** Tokens for PuduCalc without positions */
enum CalcToken:
  case LPar()
  case RPar()
  case Literal(value: Double)
  case Plus()
  case Minus()
  case Asterisk()
  case Slash()
  case Caret()
  case Id(name: String)
  case Comma()
  case Assign()

  case ERROR()
  case EOF()
