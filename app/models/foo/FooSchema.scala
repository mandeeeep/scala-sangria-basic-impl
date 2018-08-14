package models.foo

import models.FooBarRepo
import models.bar.BarRepo
import sangria.macros.derive.{ObjectTypeName, deriveObjectType}
import sangria.schema.{Argument, BooleanType, Field, IntType, ListType, ObjectType, fields}

class FooSchema {

  val y = Argument("y", BooleanType, description = "")

  implicit val fooType = deriveObjectType[Unit, Foo](ObjectTypeName("Foo"))

  implicit val fooCheckType = deriveObjectType[Unit, FooCheck](ObjectTypeName("FooCheck"))

  val fooFields = fields[FooBarRepo, Unit](
    Field("foos", ListType(fooType), resolve = c ⇒ c.ctx.fooRepo.getAllFoo),
    Field("foos_check", fooCheckType, arguments = y :: Nil, resolve = c ⇒ c.ctx.fooRepo.fooCheck(c arg y))
  )



}
