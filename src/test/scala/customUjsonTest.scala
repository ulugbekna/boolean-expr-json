import customUjsonImpl._
import utest._

object customUjsonTest extends TestSuite {
  override def tests: Tests = Tests {
    test("serialize") {
      def assertCorrectSerialization(v: BooleanExpression, s: String): Unit = {
        val serV = upickle.default.write[BooleanExpression](v)
        assert(serV == s)
      }

      test("simple values") {
        test("True") {
          assertCorrectSerialization(True, "true")
        }
        test("False") {
          assertCorrectSerialization(False, "false")
        }
        test("""Variable("x")""") {
          assertCorrectSerialization(Variable("x"), "\"x\"")
        }
        test("Not(True)") {
          assertCorrectSerialization(Not(True), """{"not":true}""")
        }
        test("Or(False, True)") {
          assertCorrectSerialization(
            Or(False, True),
            """{"or":{"e1":false,"e2":true}}"""
          )
        }
        test("And(False, True)") {
          assertCorrectSerialization(
            And(False, True),
            """{"and":{"e1":false,"e2":true}}"""
          )
        }
      }
      test("composite values") {
        test("""(x \/ T) /\ (x /\ F)""") {
          assertCorrectSerialization(
            And(Or(Variable("x"), True), And(Variable("x"), False)),
            """{"and":{"e1":{"or":{"e1":"x","e2":true}},"e2":{"and":{"e1":"x","e2":false}}}}"""
          )
        }
      }
      test("exceptions") {

      }
    }
    test("deserialize") {
      def assertCorrectDeser(s: String, v: BooleanExpression): Unit = {
        val deserV = upickle.default.read[BooleanExpression](s)
        assert(deserV == v)
      }

      test("simple values") {
        test("True") {
          assertCorrectDeser("true", True)
        }
        test("False") {
          assertCorrectDeser("false", False)
        }
        test("""Variable("x")""") {
          assertCorrectDeser("\"x\"", Variable("x"))
        }
        test("Not(True)") {
          assertCorrectDeser("""{"not":true}""", Not(True))
        }
        test("Or(False, True)") {
          assertCorrectDeser(
            """{"or":{"e1":false,"e2":true}}""",
            Or(False, True)
          )
        }
        test("And(False, True)") {
          assertCorrectDeser(
            """{"and":{"e1":false,"e2":true}}""",
            And(False, True)
          )
        }
      }
      test("various cases") {
        test("""x /\ T -- with many spaces inside string""") {
          assertCorrectDeser(
            """{"and":{"e1"  : "x", "e2": true}}""",
            And(Variable("x"), True))
        }
        test("""T /\ x -- with object fields shuffled""") {
          assertCorrectDeser(
            """{"and":{"e2":"x","e1":true}}""",
            And(True, Variable("x")))
        }
      }
      test("composite values") {
        assertCorrectDeser(
          """{"and":{"e1":{"or":{"e1":"x","e2":true}},"e2":{"and":{"e1":"x","e2":false}}}}""",
          And(Or(Variable("x"), True), And(Variable("x"), False))
        )
      }
    }
  }
}
