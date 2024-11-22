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

  // Calculate ratios and find state with highest total beds
    val stateRatios = stateBedsMap.map { case (state, (total, covid)) =>
      val ratio = if (total > 0) covid.toDouble / total else 0.0
      (state, total, covid, ratio)
    }

  // Find state with highest total beds
    val (stateWithMaxBeds, totalBeds, covidBeds, ratio) = stateRatios.maxBy(_._2)

    println(s"\nState with highest number of hospital beds: $stateWithMaxBeds")
    println(s"Total beds: $totalBeds")
    println(s"COVID-19 dedicated beds: $covidBeds")
    println(f"Ratio of COVID-19 beds to total beds: ${ratio * 100}%.2f%%")

    println("\nRatio of COVID-19 beds to total beds for all states:")
    stateRatios.toList.sortBy(-_._4).foreach { case (state, total, covid, ratio) =>
      println(f"$state: ${ratio * 100}%.2f%% ($covid COVID beds out of $total total beds)")
    }

   // Calculate averages by state for each category
      val stateAdmissionStats = rows
        .groupBy(_("state"))
        .map { case (state, records) =>
          // Helper function to safely parse numbers
          def safeAvg(column: String): Double = {
            val validNumbers = records
              .map(_(column).trim)
              .filter(_.nonEmpty)
              .map(_.toDouble)
            if (validNumbers.isEmpty) 0.0 else validNumbers.sum / validNumbers.size
          }
  
          // Calculate averages for each category
          val avgPUI = safeAvg("admitted_pui")
          val avgCovid = safeAvg("admitted_covid")
          val avgTotal = safeAvg("admitted_total")
          // Non-COVID admissions can be derived from total minus (PUI + COVID)
          val avgNonCovid = math.max(0.0, avgTotal - (avgPUI + avgCovid))
  
          (state, (avgPUI, avgCovid, avgNonCovid))
        }

    // Print results
        println("\nAverage Daily Admissions by Category for Each State:")
        println("=" * 80)
        println(f"%%-20s | %%-15s | %%-15s | %%-15s".format(
          "State", "Suspected/PUI", "COVID-19", "Non-COVID"))
        println("=" * 80)

      stateAdmissionStats.toList
            .sortBy(_._1) // Sort by state name
            .foreach { case (state, (pui, covid, nonCovid)) =>
              println(f"%%-20s | %%-15.2f | %%-15.2f | %%-15.2f".format(
                state, pui, covid, nonCovid))
            }

  // Calculate and print overall averages
      val overallStats = stateAdmissionStats.values.foldLeft((0.0, 0.0, 0.0)) {
        case ((totalPUI, totalCovid, totalNonCovid), (pui, covid, nonCovid)) =>
          (totalPUI + pui, totalCovid + covid, totalNonCovid + nonCovid)
      }
      val stateCount = stateAdmissionStats.size

end MyApp
