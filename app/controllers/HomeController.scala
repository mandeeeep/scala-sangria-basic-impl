package controllers

import javax.inject._

import models._
import models.playJson._
import models.foo.FooInit._
import models.foo.{FooRepo, FooSchema}
import models.bar.{BarRepo, BarSchema}
import play.api._
import play.api.mvc._
import play.api.libs.json._
import sangria.execution.Executor
import sangria.parser.{QueryParser, SyntaxError}
import sangria.marshalling._

import scala.util.{Failure, Success}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class HomeController @Inject() (fooBarRepo: FooBarRepo, fooBarSchema: FooBarSchema)  extends Controller {

  def index = Action.async(parse.json) { request =>

    val query = (request.body \ "query").as[String]
    val operation = (request.body \ "operationName").asOpt[String]

    val variables = (request.body \ "variables").toOption.flatMap {
      case JsString(vars) ⇒ Some(if (vars.trim == "" || vars.trim == "null") Json.obj() else Json.parse(vars).as[JsObject])
      case obj: JsObject ⇒ Some(obj)
      case _ ⇒ None
    }

    QueryParser.parse(query) match {
      case Success(queryAst) => {
        Logger.info("Query is valid.")
        Executor.execute(fooBarSchema.schema,
          queryAst,
          userContext = fooBarRepo,
          variables = variables getOrElse Json.obj(),
          operationName = operation)
          .map(
            res => {
              println(res.toString())
              Ok(res)
            }
          )
          .recover {
            case error: Exception => Logger.info("Error occured..."); Ok(error.getMessage)
          }
      }
      case Failure(error: SyntaxError) => Logger.info("Invalid query supplied."); Future.successful(BadRequest(Json.obj("error" → error.getMessage)))
    }

  }
}


