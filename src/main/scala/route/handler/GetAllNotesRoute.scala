package route.handler

import db.{CRUD, NotesRepository}
import model.Note
import route.interface.{RecordsRetriever, SimpleRequestHandler}
import route.implementation.GetAllNotesService
import zhttp.http.Response
import zio.*
import zio.json.*

class GetAllNotesRoute extends SimpleRequestHandler[RecordsRetriever[Note]] {
  
  final override def handle: RIO[RecordsRetriever[Note], Response] = for {
    getAllNotesService <- ZIO.service[RecordsRetriever[Note]]
    notes              <- getAllNotesService.retrieveRecords
    response           <- ZIO.succeed(Response.text(notes.toJsonPretty))
  } yield response

}
