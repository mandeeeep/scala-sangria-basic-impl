package models

import javax.inject.Inject
import sangria.schema._
import sangria.macros.derive.{ObjectTypeDescription, ObjectTypeName, deriveObjectType}
import sangria.schema.{Field, ListType, ObjectType, OptionType, Schema, fields}
import playJson._
import models.foo.FooInit._
import models.foo.FooSchema
import models.bar.BarSchema
import sangria.execution.batch.BatchExecutor

/**
  * Created by mandeep on 11/7/17.
  */
class FooBarSchema @Inject() (fooSchema: FooSchema, barSchema: BarSchema) {

  import fooSchema._
  import barSchema._

  val query = ObjectType("Query", barFields ++ fooFields)

  val schema = Schema(query)
  val batchSchema = Schema(query, directives = BuiltinDirectives :+ BatchExecutor.ExportDirective)

}
