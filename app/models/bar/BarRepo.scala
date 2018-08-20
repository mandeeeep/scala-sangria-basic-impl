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

  def barCheck2(z: Option[Int]): BarCheck = {
    println("BarCheck Called....")
    val x = z.getOrElse(0)
    if(x == 1){
      BarCheck(true)
    }else{
      BarCheck(false)
    }
  }

  def barTopperInsert(bt: Option[BarTopper]): BarCheck = {
    println("BarTopper Called....")
    val d = bt
    BarCheck(false)
  }

}
