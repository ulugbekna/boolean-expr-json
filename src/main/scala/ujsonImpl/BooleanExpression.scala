package ujsonImpl

import upickle.default.{macroRW, ReadWriter => RW}

/*
* This implementation of ``BooleanExpression` (de)serializers uses straightforward `uPickle`'s macro-based
* approach.
*
* We have to have repeated code for each subclass because of a bug in the Scala compiler SI-7046 [1].
*
* [1] (https://com-lihaoyi.github.io/upickle/#ManualSealedTraitPicklers)
* */

sealed trait BooleanExpression

object BooleanExpression {
  implicit val rw: RW[BooleanExpression] = RW.merge(True.rw, False.rw, Variable.rw, Not.rw, Or.rw, And.rw)
}

case object True extends BooleanExpression {
  implicit val rw: RW[this.type] = macroRW
}

case object False extends BooleanExpression {
  implicit val rw: RW[this.type] = macroRW
}

case class Variable(symbol: String) extends BooleanExpression

case object Variable {
  implicit val rw: RW[Variable] = macroRW
}

case class Not(e: BooleanExpression) extends BooleanExpression

case object Not {
  implicit val rw: RW[Not] = macroRW
}

case class Or(e1: BooleanExpression, e2: BooleanExpression) extends BooleanExpression

case object Or {
  implicit val rw: RW[Or] = macroRW
}

case class And(e1: BooleanExpression, e2: BooleanExpression) extends BooleanExpression

case object And {
  implicit val rw: RW[And] = macroRW
}
