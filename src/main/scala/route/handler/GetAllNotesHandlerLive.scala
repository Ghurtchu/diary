package route.handler

import db.{CRUD, NotesRepository}
import model.{JwtContent, LoginPayload, Note}
import pdi.jwt.{Jwt, JwtCirce}
import route.interface.*
import route.implementation.GetAllNotesService
import server.NotesServer
import zhttp.http.Response
import zio.*
import zio.json.*
import zhttp.http.*

trait GetAllNotesHandler {
  def handle(jwtContent: JwtContent): Task[Response]
}

final case class GetAllNotesHandlerLive(getAllNotesService: RecordsRetriever[Note]) extends GetAllNotesHandler {
  
  override def handle(jwtContent: JwtContent): Task[Response] = for {
    notes         <- getAllNotesService.retrieveRecords
    filteredNotes <- ZIO.succeed(notes.filter(note => note.userId.isDefined && note.userId.get == jwtContent.id))
    response      <- ZIO.succeed(Response.text(filteredNotes.toJsonPretty))
  } yield response

}

object GetAllNotesHandlerLive {
  
  lazy val layer: URLayer[RecordsRetriever[Note], GetAllNotesHandler] =
    ZLayer.fromFunction(GetAllNotesHandlerLive.apply _)
  
}
