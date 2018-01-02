import java.text.DecimalFormat

import org.apache.spark.{SparkConf, SparkContext}

object Analysis {

  val conf = new SparkConf().setAppName("Chicago Crime Data Analysis").setMaster("local")
  val sc = new SparkContext(conf)

  def main(args: Array[String]): Unit = {

    val data = sc.textFile("data/CrimeDataWithoutHeader.csv")
    val communityCodesData = sc.textFile("data/CommunityCodes.csv").
                                map(rec => (rec.split(",")(0), rec.split(",")(1)))

    /*val totalNumberOfRecords = data.count()
    println(totalNumberOfRecords) 6508475*/

    // Cases with no community
    /*val casesWithNoCommunity = data.
                                  filter(rec => rec.split(",")(13) == "").
                                  count()*/

    // println(casesWithNoCommunity) (594681)

    // Top 10 communities with most/least crime
    /*data.
      filter(rec => rec.split(",")(13) != "").
      map(rec => (rec.split(",")(13), 1)).
      reduceByKey(_ + _, 1).
      join(communityCodesData).
      map(item => (item._2._1.toInt, (item._1,item._2._2))).
      takeOrdered(10)(Ordering[Int].on(x=>x._1)).
      foreach(println)*/

    // Top Crime Types
    data.
      map(rec => (rec.split(",")(5),1)).
      reduceByKey(_ + _, 4).
      map(rec => (rec._1, BigDecimal((rec._2.toDouble/6508475)*100).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble)).
      takeOrdered(3)(Ordering[Double].reverse.on(x=>x._2)).
      foreach(println)

  }
}
