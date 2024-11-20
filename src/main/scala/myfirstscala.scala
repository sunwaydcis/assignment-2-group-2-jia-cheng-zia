import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage

import scala.io.Source
import Source.fromFile
import org.apache.commons.csv.{CSVFormat, CSVParser}


object MyApp extends JFXApp3:

  override def start(): Unit =
    stage = new PrimaryStage()

  val csvFilePath = "src/main/resources/hospital.csv"

  //version 1
  //val scalaFileContents: Iterator[String] = Source.fromFile(filePath).getLines
  //scalaFileContents.foreach(println)

  //csv read 
  val reader = Files.newBufferedReader(Paths.get(csvFilePath))
  val csvParser = CSVParser.parse(reader, CSVFormat.DEFAULT.withHeader())

end MyApp
