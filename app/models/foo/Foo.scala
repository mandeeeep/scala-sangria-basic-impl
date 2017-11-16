package models.foo

import play.api.libs.json.Json

/**
  * Created by mandeep on 11/7/17.
  */
case class Foo(id: String, foo: Option[String] = None, bar: String)

object FooInit {
  implicit val fooFormat = Json.format[Foo]
}
