import scala.io.Source

object app {
  def main(args: Array[String]) {
    println("Hello, world!")
    val filename = "input.txt"
    for (line <- Source.fromFile(filename).getLines) {
      println(line)
    }
  }
}
