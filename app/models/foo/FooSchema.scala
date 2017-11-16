package models.foo

import models.FooBarRepo
import models.bar.BarRepo
import sangria.macros.derive.{ObjectTypeName, deriveObjectType}
import sangria.schema.{Field, ListType, ObjectType, fields}

class FooSchema {

  implicit val fooType = deriveObjectType[Unit, Foo](ObjectTypeName("Foo"))

  val fooFields = fields[FooRepo, Unit](Field("Foos", ListType(fooType), resolve = c ⇒ c.ctx.getAllFoo))

}
