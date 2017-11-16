package models.bar

import play.api.libs.json.Json

case class Bar(id: String, fooTwo: Option[String] = None, bar: String)

object DummyTwoInit {
  implicit val barFormat = Json.format[Bar]
}