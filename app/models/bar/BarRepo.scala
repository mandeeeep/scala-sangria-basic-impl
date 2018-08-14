package models.bar

class BarRepo {

  def getAllBar: Seq[Bar] = {
    Seq(Bar("1", Some("BarA"), "BarA"))
  }

  def barCheck(x: Int): BarCheck = {
    println("BarCheck Called....")
    if(x == 1){
      BarCheck(true)
    }else{
      BarCheck(false)
    }
  }

}
