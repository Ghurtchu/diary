package route.handler

import db.{CRUD, NotesRepository}
import model.Note
import route.interface.{RecordsRetriever, SimpleRequestHandler}
import route.implementation.GetAllNotesService
import zhttp.http.Response
import zio.*
import zio.json.*

trait GetAllNotesHandler {
  def handle: Task[Response]
}

final case class GetAllNotesHandlerImpl(getAllNotesService: RecordsRetriever[Note]) extends GetAllNotesHandler {
  
  final override def handle: Task[Response] = for {
    notes              <- getAllNotesService.retrieveRecords
    response           <- ZIO.succeed(Response.text(notes.toJsonPretty))
  } yield response

}

object GetAllNotesHandlerImpl {
  
  lazy val layer: URLayer[RecordsRetriever[Note], GetAllNotesHandlerImpl] =
    ZLayer.fromFunction(GetAllNotesHandlerImpl.apply _)
  
}
