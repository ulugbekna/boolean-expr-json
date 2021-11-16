package customUjsonImpl

import ujson._
import upickle.default.{ReadWriter => RW}

sealed trait BooleanExpression

object BooleanExpression {
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
      json match {
        case Str(s) => Variable(s)
        case bool: Bool => if (bool.bool) True else False
        case Obj(map) =>
          map.head match {
            case ("not", e) => Not(fromJson(e))
            case ("and", v) =>
              val vmap = v.obj
              assert(vmap.contains("e1") && vmap.contains("e2"))
              And(fromJson(vmap("e1")), fromJson(vmap("e2")))
            case ("or", v) =>
              val vmap = v.obj
              assert(vmap.contains("e1") && vmap.contains("e2"))
              Or(fromJson(vmap("e1")), fromJson(vmap("e2")))
          }
        case v@(Num(_)
                | Null
                | Arr(_)) => throw new Exception(s"unexpected json value $v")
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

