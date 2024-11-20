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

  // Calculate total beds and COVID beds for each state
    val stateBedsMap = rows
      .groupBy(_("state")) // Group by state
      .map { case (state, hospitals) =>
        val totalBeds = hospitals
          .map(_("beds").trim)
          .filter(_.nonEmpty)
          .map(_.toInt)
          .sum

        val covidBeds = hospitals
          .map(_("beds_covid").trim)
          .filter(_.nonEmpty)
          .map(_.toInt)
          .sum

        (state, (totalBeds, covidBeds))
      }

end MyApp
