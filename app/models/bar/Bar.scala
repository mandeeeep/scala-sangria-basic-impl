package models.bar

import play.api.libs.json.Json

case class Bar(id: String, fooTwo: Option[String] = None, bar: String)

case class BarCheck(x: Boolean)

object BarInit {
  implicit val barFormat = Json.format[Bar]
  implicit val barCheckFormat = Json.format[BarCheck]
}