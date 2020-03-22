import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.twitter.TwitterUtils

object TwitterPopularTags extends App {
  Utils.setTwitterAuthProperty
  val conf = new SparkConf().setAppName("TwitterAnalysis").setMaster("local[2]")
  val ssc = new StreamingContext(conf, Seconds(2))
  val filter = if (args.isEmpty) Nil else args.toList
  val stream = TwitterUtils.createStream(ssc, None, filter)

  val hashTags = stream.flatMap(status => status.getText.split(" ").filter(_.startsWith("#")))
  val topCounts60 = hashTags.map((_, 1)).reduceByKeyAndWindow(_ + _, Seconds(60))
    .map { case (topic, count) => (count, topic) }
    .transform(_.sortByKey(ascending = false))


  topCounts60.foreachRDD(rdd => {
    val topList = rdd.take(10)
    println("Popular topics in last 60 seconds (%s total):".format(rdd.count()))
    topList.foreach { case (count, tag) => println(("%s (%s tweets)".format(tag, count))) }
  })

  ssc.start()
  ssc.awaitTermination()
}

