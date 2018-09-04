package models

import play.api.libs.json.{JsObject, JsValue, Json}

case class GQLRequest(variables: Option[JsValue], query: String, operationName: Option[String])

object GQLRequest{
  implicit val gqlRequestFormat = Json.format[GQLRequest]
}
