package models.foo

/**
  * Created by mandeep on 11/7/17.
  */


class FooRepo {

  def getAllFoo: Seq[Foo] = {
    Seq(Foo("1", Some("FooA"), "BarA"))
  }

}




