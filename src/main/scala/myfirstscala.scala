import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage

import scala.io.Source
import Source.fromFile
import com.github.tototoshi.csv._
import java.io.File


object MyApp extends JFXApp3:

  override def start(): Unit =
    stage = new PrimaryStage()

  val csvFilePath = "src/main/resources/hospital.csv"

  //version 1
  //val scalaFileContents: Iterator[String] = Source.fromFile(filePath).getLines
  //scalaFileContents.foreach(println)

  //csv read 
  val reader = CSVReader.open(new File(csvFilePath))

  // Read all rows and skip the header
  val rows = reader.allWithHeaders()

end MyApp
