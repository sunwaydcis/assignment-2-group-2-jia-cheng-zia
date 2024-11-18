import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage

import scala.io.Source
import Source.fromFile


object MyApp extends JFXApp3:

  override def start(): Unit =
    stage = new PrimaryStage()

  val filePath = "src/main/resources/hospital.csv"

  //version 1
  //val scalaFileContents: Iterator[String] = Source.fromFile(filePath).getLines
  //scalaFileContents.foreach(println)

  //version2(does not work yet might use the above one)
  def open(path: String) = new File(filePath)
  
  implicit class RichFile(file: File){
    def read() = Source.fromFile(file).getLines
  }
  
  val readLikeABoss = open(filePath).read
  readLikeABoss.foreach(println)

end MyApp
