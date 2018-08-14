package models.foo

/**
  * Created by mandeep on 11/7/17.
  */


class FooRepo {

  def getAllFoo: Seq[Foo] = {
    Seq(Foo("1", Some("FooA"), "BarA"))
  }

  def fooCheck(x: Boolean): FooCheck = {
    println("FooCheck Called....")
    if(x == true){
      FooCheck(1)
    }else{
      FooCheck(0)
    }
  }

}




