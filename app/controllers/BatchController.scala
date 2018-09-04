package controllers

import javax.inject._
import models._
import models.playJson._
import play.api._
import play.api.libs.json._
import play.api.mvc._
import sangria.execution.Executor
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

              //val response = resp
              val response: JsValue = (resp \ "data").as[JsValue]

              oprs match {
                case Some(operation) => {
                  Json.obj(operation-> response)
                }
                case None => {
                  response
                }
              }
            }
            .recover {
              case error: Exception => {
                Logger.info("Error occured...")
                oprs match {
                  case Some(operation) => {
                    Json.obj(operation-> error.getMessage)
                  }
                  case None => {
                    (JsString(error.getMessage))
                  }
                }
              }
            }
        }
        case Failure(error: SyntaxError) => {
          Logger.info("Invalid query supplied.")
          oprs match {
            case Some(operation) => {
              Future(Json.obj(operation-> error.getMessage))
            }
            case None => {
              Future(JsString(error.getMessage))
            }
          }
        }
      }
    }

    (request.body).validate[Seq[GQLRequest]].asOpt match {
      case Some(gqlReq) => {
        def treatVars(o: Option[JsValue]) = o.flatMap {
          case JsString(vars) ⇒ Some(if (vars.trim == "" || vars.trim == "null") Json.obj() else Json.parse(vars).as[JsObject])
          case obj: JsObject ⇒ Some(obj)
          case _ ⇒ None
        }

        val operation = (request.body \ "operationName").asOpt[String]

        val futuras: Seq[Future[JsValue]] = gqlReq.map(x => aligator(vars = treatVars(x.variables), oprs = x.operationName, qrs = x.query))
        val batchFuturas: Future[Seq[JsValue]] = Future.sequence(futuras)

        batchFuturas.map { batchResponses =>
          Ok(Json.obj("data"->JsArray(batchResponses)))
        }
      }
      case None => {
        Logger.info("GraphQL Request Invalid Format")
        Future.successful(Ok(Json.obj("error" -> "GraphQL Request Invalid Format")))
      }
    }
  }
}


