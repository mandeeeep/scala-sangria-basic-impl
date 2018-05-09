package models

import javax.inject.Inject
import models.bar.BarSchema
import models.foo.FooSchema
import sangria.execution.batch.BatchExecutor
import sangria.schema.{BuiltinDirectives, ObjectType, Schema}

/**
  * Created by mandeep on 11/7/17.
  */
class FooBarBatchSchema @Inject()(fooSchema: FooSchema, barSchema: BarSchema) {

  import barSchema._
  import fooSchema._

  val query = ObjectType("Query", barFields ++ fooFields)

  val schema = Schema(query, directives = BuiltinDirectives :+ BatchExecutor.ExportDirective)

}
