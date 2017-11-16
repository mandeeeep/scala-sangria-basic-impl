package models.bar

class BarRepo {

  def getAllBar: Seq[Bar] = {
    Seq(Bar("1", Some("FooA"), "BarA"))
  }

}
