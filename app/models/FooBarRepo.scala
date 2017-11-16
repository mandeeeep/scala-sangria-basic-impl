package models

import javax.inject.Inject

import models.foo.FooRepo
import models.bar.BarRepo


case class FooBarRepo @Inject() (fooRepo: FooRepo, barRepo: BarRepo) {


}


