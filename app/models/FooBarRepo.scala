package models

import models.foo.FooRepo
import models.bar.BarRepo


class BaseRepo(fooRepo: FooRepo, barRepo: BarRepo) {
}

trait RepoCollection {
  lazy val fooRepo = new FooRepo()
  lazy val barRepo = new BarRepo()
}

class DummyMergedRepository extends RepoCollection {
  lazy val bakery = new BaseRepo(fooRepo, barRepo)

  import fooRepo._
  import barRepo._

}


