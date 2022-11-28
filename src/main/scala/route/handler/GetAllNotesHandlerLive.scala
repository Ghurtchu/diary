package route.handler

import db.note.NotesRepositoryLive
import pdi.jwt.{Jwt, JwtCirce}
import server.NotesServer
import zhttp.http.Response
import zio.*
import zio.json.*
import zhttp.http.*
import RequestHandlerDefinitions.GetAllNotesHandler
import domain.Domain.JwtContent
import route.service.GetAllNotesServiceLive
import route.service.ServiceDefinitions.GetAllNotesService

final case class GetAllNotesHandlerLive(getAllNotesService: GetAllNotesService) extends GetAllNotesHandler:
  
  override def handle(jwtContent: JwtContent): Task[Response] =
    getAllNotesService.getNotesByUserId(jwtContent.userId)
      .map(_.toJsonResponse)


object GetAllNotesHandlerLive:
  
  lazy val layer: URLayer[GetAllNotesService, GetAllNotesHandler] =
    ZLayer.fromFunction(GetAllNotesHandlerLive.apply)
  

