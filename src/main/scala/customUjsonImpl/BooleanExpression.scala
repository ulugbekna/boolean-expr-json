package customUjsonImpl

import ujson._
import upickle.default.{ReadWriter => RW}

sealed trait BooleanExpression

object BooleanExpression {
  /* Exception raised for a JSON (sub-)value, which is not expected,
  * i.e., does not come in the same shape used to serialize a `BooleanExpression` */
  case class IncorrectJSONValue(json: String) extends Exception

  implicit val rw: RW[BooleanExpression] = {
    def toJson(expr: BooleanExpression): ujson.Value =
      expr match {
        case True => ujson.True
        case False => ujson.False
        case Variable(s) => ujson.Str(s)
        case Not(e) => ujson.Obj(("not", toJson(e)))
        case Or(e1, e2) =>
          ujson.Obj(
            ("or", ujson.Obj(
              ("e1", toJson(e1)),
              ("e2", toJson(e2))))
          )
        case And(e1, e2) =>
          ujson.Obj(
            ("and", ujson.Obj(
              ("e1", toJson(e1)),
              ("e2", toJson(e2)))))
      }

    def fromJson(json: ujson.Value): BooleanExpression = {
      def parseAndOrSubexpr(v: Value) = {
        val vmap = v.obj
        if (!(vmap.contains("e1") && vmap.contains("e2")))
          throw IncorrectJSONValue(v.toString())
        (fromJson(vmap("e1")), fromJson(vmap("e2")))
      }

      json match {
        case Str(s) => Variable(s)
        case bool: Bool => if (bool.bool) True else False
        case Obj(map) =>
          map.head match {
            case ("not", e) => Not(fromJson(e))
            case ("and", v) =>
              val (e1, e2) = parseAndOrSubexpr(v)
              And(e1, e2)
            case ("or", v) =>
              val (e1, e2) = parseAndOrSubexpr(v)
              Or(e1, e2)
          }
        case v@(Num(_)
                | Null
                | Arr(_)) => throw IncorrectJSONValue(v.toString())
      }
    }

    upickle.default.readwriter[ujson.Value].bimap[BooleanExpression](toJson, fromJson)
  }
}

case object True extends BooleanExpression

case object False extends BooleanExpression

case class Variable(symbol: String) extends BooleanExpression

case class Not(e: BooleanExpression) extends BooleanExpression

case class Or(e1: BooleanExpression, e2: BooleanExpression) extends BooleanExpression

case class And(e1: BooleanExpression, e2: BooleanExpression) extends BooleanExpression

