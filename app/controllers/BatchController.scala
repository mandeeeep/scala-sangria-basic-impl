package controllers

import javax.inject._
import models._
import models.playJson._
import play.api._
import play.api.libs.json._
import play.api.mvc._
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.parser.{QueryParser, SyntaxError}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class BatchController @Inject()(fooBarRepo: FooBarRepo, fooBarSchema: FooBarSchema) extends Controller {

  def index = Action.async(parse.json) { request =>

    def aligator(vars: Option[JsObject], oprs: Option[String], qrs: String): Future[JsValue] = {
      QueryParser.parse(qrs) match {
        case Success(queryAst) => {
          Logger.info("Query is valid.")
          Executor.execute(fooBarSchema.schema,
            queryAst,
            userContext = fooBarRepo,
            variables = vars getOrElse Json.obj(),
            operationName = oprs)
            .map { resp =>
              resp
            }
            .recover {
              case error: QueryAnalysisError => {
                Logger.info("QueryAnalysisError occured...")
                Json.toJson(error.resolveError)
              }
              case error: ErrorWithResolver => {
                Logger.info("ErrorWithResolver occured...")
                Json.toJson(error.resolveError)
              }
              case error: Exception => {
                Logger.info("Error occured...")
                Json.toJson(error.getMessage)
              }
            }
        }
        case Failure(error: SyntaxError) => {
          Logger.info("Invalid query supplied.")
          Future.successful((Json.obj(
            "syntaxError" → error.getMessage,
            "locations" → Json.arr(Json.obj(
              "line" → error.originalError.position.line,
              "column" → error.originalError.position.column)))))
        }
//        case Failure(error) => {
//          Future.successful((Json.obj(
//            "syntaxError" → error.getMessage)))
//        }
      }
    }

    (request.body).validate[Seq[GQLRequest]].asOpt match {
      case Some(gqlReq) => {
        def treatVars(o: Option[JsValue]) = o.flatMap {
          case JsString(vars) ⇒ Some(if (vars.trim == "" || vars.trim == "null") Json.obj() else Json.parse(vars).as[JsObject])
          case obj: JsObject ⇒ Some(obj)
          case _ ⇒ None
        }

        val futuras: Seq[Future[JsValue]] = gqlReq.map(x => aligator(vars = treatVars(x.variables), oprs = x.operationName, qrs = x.query))
        val batchFuturas: Future[Seq[JsValue]] = Future.sequence(futuras)

        batchFuturas.map { batchResponses =>
          Ok(JsArray(batchResponses))
        }
      }
      case None => {
        Logger.info("GraphQL Request Invalid Format")
        Future.successful(Ok(Json.obj("error" -> "GraphQL Request Invalid Format")))
      }
    }
  }
}


