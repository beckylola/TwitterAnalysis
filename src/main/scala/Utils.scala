import java.util.Properties

import scala.io.Source

object Utils {
  def setTwitterAuthProperty = {
    System.setProperty("twitter4j.oauth.consumerKey", Utils.getConsumerKey)
    System.setProperty("twitter4j.oauth.consumerSecret", Utils.getConsumerSecret)
    System.setProperty("twitter4j.oauth.accessToken", Utils.getAccessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", Utils.getAccessTokenSecret)
  }


  def getConsumerKey: String = {
    val properties: _root_.java.util.Properties = getProperties
    properties.getProperty("ConsumerKey")
  }

  def getConsumerSecret: String = {
    val properties: _root_.java.util.Properties = getProperties
    properties.getProperty("ConsumerSecret")
  }

  def getAccessToken: String = {
    val properties: _root_.java.util.Properties = getProperties
    properties.getProperty("AccessToken")
  }

  def getAccessTokenSecret: String = {
    val properties: _root_.java.util.Properties = getProperties
    properties.getProperty("AccessTokenSecret")
  }

  private def getProperties = {
    val url = getClass.getResource("application.properties")
    val properties: Properties = new Properties()
    if (url != null) {
      val source = Source.fromURL(url)
      properties.load(source.bufferedReader())
    }
    properties
  }
}
