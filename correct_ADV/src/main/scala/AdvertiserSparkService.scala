import Constants._
import entities.CorrAdvertiser
import org.apache.spark.rdd.RDD
import org.apache.spark.sql._
import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by atomkevich on 2/16/17.
  */
object AdvertiserSparkService extends AdvertiserService with HttpPPASClient {
  import Implicits._

  val conf = new SparkConf()
    .setAppName("WordCount")
    .setMaster("local")
  val sc = new SparkContext(conf)

  val sqlcontext = SparkSession.builder()
    .config(conf).getOrCreate()


  def dataFrameFromCSV = {
    val correctAdvertisers = sqlcontext.read.format("csv").option("header", "true")
      .load(advertiserFile).select("Affiliate advertiser ID", "Name")
    correctAdvertisers.toDF("affiliatedAdvertiserId", "correctName")
  }

  def dataFrameFromPPAS = {
    val totalAdv = getAllAdvertisersFromPPas
    val ppasAdvertisersRDD = sqlcontext.sparkContext.parallelize(totalAdv)
    sqlcontext.createDataFrame(ppasAdvertisersRDD)
  }

  def joinDataFromCsvAndPPAS(ppasData: DataFrame, csvData: DataFrame): RDD[CorrAdvertiser] = {
    ppasData.join(csvData, Seq("affiliatedAdvertiserId")).rdd.map(_.toAdvertiser)
  }

  def reimportAdv = {
    val joinedAdvertisers =joinDataFromCsvAndPPAS(dataFrameFromPPAS, dataFrameFromCSV)
    joinedAdvertisers.groupBy(_.affiliatedAdvertiserId)
      .mapValues(correctInvalidNames)
      .values.flatMap(identity).collect()
  }
}
