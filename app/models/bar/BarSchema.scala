package models.bar

import models.FooBarRepo
import sangria.macros.derive.{InputObjectTypeName, ObjectTypeName, deriveInputObjectType, deriveObjectType}
import sangria.schema.{Argument, BooleanType, Field, InputField, InputObjectType, IntType, ListInputType, ListType, ObjectType, OptionInputType, OptionType, StringType, fields}
import models.playJson._
import play.api.libs.json.{JsValue, Json}

class BarSchema {

  val x = Argument("x", IntType, description = "")

  val z = Argument("z", OptionInputType(IntType), description = "")

  val btInput = Argument("bt", (barTopperInputJsType), description = "")

  implicit val barType = deriveObjectType[Unit, Bar](ObjectTypeName("Bar"))

  implicit val barCheckType = deriveObjectType[Unit, BarCheck](ObjectTypeName("BarCheck"))

  implicit val barTopperType = deriveObjectType[Unit, BarTopper](ObjectTypeName("BarTopper"))

  //implicit val barTopperInputType = deriveInputObjectType[BarTopper](InputObjectTypeName("BarTopperInput"))

  lazy val barTopperKidInputType = InputObjectType[JsValue](
    "BarTopperKidInput",
    "A add account input type.",
    List(
      InputField("x", OptionInputType(BooleanType), description = ("")),
      InputField("y", OptionInputType(StringType), description = (""))
    )
  )

  lazy val barTopperInputType = InputObjectType[BarTopper](
    "BarTopperInput",
    "A add account input type.",
    List(
      InputField("a", OptionInputType(BooleanType), description = ("")),
      InputField("b", OptionInputType(StringType), description = ("")),
        InputField("c", OptionInputType(StringType), description = (""))
    )
  )

  lazy val barTopperInputJsType = InputObjectType[JsValue](
    "BarTopperInput",
    "A add account input type.",
    List(
      InputField("a", OptionInputType(BooleanType), description = ("")),
      InputField("b", OptionInputType(StringType), description = ("")),
      InputField("c", OptionInputType(StringType), description = ("")),
      InputField("d", ListInputType(barTopperKidInputType), description = (""))
    )
  )

  val barFields = fields[FooBarRepo, Unit](
    Field("bars", ListType(barType), resolve = c ⇒ c.ctx.barRepo.getAllBar),
    Field(
      "bars_check", barCheckType, arguments = x :: Nil, resolve = c => {

          c.ctx.barRepo.barCheck(c arg x)
      }
    ),
    Field(
      "bars_check2", barCheckType, arguments = z :: Nil, resolve = c => {
        val x = z
        c.ctx.barRepo.barCheck2(c arg x)
      }
    ),
    Field(
      "bars_topper_insert",
      barCheckType,
      arguments = btInput :: Nil,
      description = Some(""),
      resolve = c => {
        val x = btInput
        val d = c.arg(x)
        println(d)
        c.ctx.barRepo.barTopperInsert(None)
      }
    )
  )



}
