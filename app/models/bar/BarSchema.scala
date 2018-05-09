package models.bar

import models.FooBarRepo
import sangria.macros.derive.{ObjectTypeName, deriveObjectType}
import sangria.schema.{Field, ListType, ObjectType, fields}

class BarSchema {

  implicit val barType = deriveObjectType[Unit, Bar](ObjectTypeName("Bar"))

  val barFields = fields[FooBarRepo, Unit](Field("bars", ListType(barType), resolve = c ⇒ c.ctx.barRepo.getAllBar))

}
