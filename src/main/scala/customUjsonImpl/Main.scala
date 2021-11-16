package customUjsonImpl

object Main {
  def main(args: Array[String]): Unit = {
    /*
    * `Main` is used more as an example of how to (de)serialize boolean expressions.
    * */
    import upickle.default.{read, write}

    println(write[BooleanExpression](Or(True, Variable("x"))))

    println(read[BooleanExpression](
      write[BooleanExpression](And(False, Not(Variable("y"))))))
  }
}
