package models.bar

import models.DummyMergedRepository
import sangria.macros.derive.{ObjectTypeName, deriveObjectType}
import sangria.schema.{Field, ListType, ObjectType, fields}

class BarSchema {

  implicit val barType = deriveObjectType[Unit, Bar](ObjectTypeName("Bar"))

  val barFields = fields[BarRepo, Unit](Field("Bars", ListType(barType), resolve = c ⇒ c.ctx.getAllBar))

}
