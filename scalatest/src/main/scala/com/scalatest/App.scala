package com.scalatest

/**
 * Hello world!
 *
 */
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.api.java.JavaSparkContext

object App {
  def main(args: Array[String]): Unit = {
   // Define a configuration to use to interact with Spark
        SparkConf conf = new SparkConf().setMaster("local").setAppName("Work Count App");

        // Create a Java version of the Spark Context from the configuration
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Load the input data, which is a text file read from the command line
        JavaRDD<String> logs = sc.textFile( filename );

    case class Log(
      host: String,
      user: String,
      date: String,
      request: String,
      response: String,
      time: Long
    )

//    val logRows = logs.map(log => log.split("\t")).map(x =>
//      Log(
//        host = x(0),
//        user = x(1),
//        date = x(2),
//        request = x(3),
//        response = x(4),
//        time = x(5).toLong
//      ))
  }
}
