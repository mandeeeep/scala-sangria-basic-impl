package models.bar

import models.FooBarRepo
import sangria.macros.derive.{ObjectTypeName, deriveObjectType}
import sangria.schema.{Argument, BooleanType, Field, ListType, ObjectType, IntType, fields}

class BarSchema {

  val x = Argument("x", IntType, description = "")

  implicit val barType = deriveObjectType[Unit, Bar](ObjectTypeName("Bar"))

  implicit val barCheckType = deriveObjectType[Unit, BarCheck](ObjectTypeName("BarCheck"))

  val barFields = fields[FooBarRepo, Unit](
    Field("bars", ListType(barType), resolve = c ⇒ c.ctx.barRepo.getAllBar),
    Field("bars_check", barCheckType, arguments = x :: Nil, resolve = c ⇒ c.ctx.barRepo.barCheck(c arg x))
  )

}
