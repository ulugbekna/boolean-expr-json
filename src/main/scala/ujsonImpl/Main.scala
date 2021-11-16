package ujsonImpl

object Main {
  def main(args: Array[String]): Unit = {
    /*
    * We do not provide much code in this package's `main` because (de)serializers implementation is
    * done automatically by `uPickle`
    * */
    import upickle.default.{read, write}

    println(write(Or(True, Variable("x"))))
    println(read[BooleanExpression](
      write(And(False, Not(Variable("y"))))))
  }
}
