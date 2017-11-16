package models

import models.foo.FooRepo
import models.bar.BarRepo


class FooBarRepo(fooRepo: FooRepo, barRepo: BarRepo) {

  import fooRepo._
  import barRepo._

}


