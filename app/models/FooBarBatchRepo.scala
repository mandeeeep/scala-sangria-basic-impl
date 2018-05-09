package models

import javax.inject.Inject
import models.bar.BarRepo
import models.foo.FooRepo


case class FooBarBatchRepo @Inject() (fooRepo: FooRepo, barRepo: BarRepo) {


}


