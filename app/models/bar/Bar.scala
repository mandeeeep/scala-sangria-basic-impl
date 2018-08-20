package models.bar

import play.api.libs.json.Json
import models.playJson._

case class Bar(id: String, fooTwo: Option[String] = None, bar: String)

case class BarCheck(x: Boolean)

case class BarTopper(a: Option[Boolean], b: Option[String], c : Option[String] = None)

object BarInit {
  implicit val barFormat = Json.format[Bar]
  implicit val barCheckFormat = Json.format[BarCheck]
  implicit val barTopperFormat = Json.format[BarTopper]
}

object BarTopper {
  implicit val barTopperFormat = Json.format[BarTopper]
}