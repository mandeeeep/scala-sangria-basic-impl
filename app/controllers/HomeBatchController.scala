package controllers

import javax.inject._
import models._
import models.foo.FooInit._
import models.playJson._
import play.api._
import play.api.libs.json._
import play.api.mvc._
import sangria.execution.Executor
import sangria.execution.batch.BatchExecutor
import sangria.parser.{QueryParser, SyntaxError}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class HomeBatchController @Inject()(fooBarRepo: FooBarRepo, fooBarSchema: FooBarSchema)  extends Controller {

  def index = Action.async(parse.json) { request =>

    val query = (request.body \ "query").as[String]
    val operation = (request.body \ "operations").asOpt[Seq[String]]

    val variables = (request.body \ "variables").toOption.flatMap {
      case JsString(vars) ⇒ Some(if (vars.trim == "" || vars.trim == "null") Json.obj() else Json.parse(vars).as[JsObject])
      case obj: JsObject ⇒ Some(obj)
      case _ ⇒ None
    }

    QueryParser.parse(query) match {
      case Success(queryAst) => {
        Logger.info("Query is valid.")
        BatchExecutor.executeBatch(
          fooBarSchema.schema,
          queryAst,
          userContext = fooBarRepo,
          variables = variables getOrElse Json.obj(),
          operationNames = operation.getOrElse(Nil))
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


