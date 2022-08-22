package route.handler

import db.{CRUD, NotesRepository}
import model.Note
import route.interface.RecordsRetriever
import route.implementation.GetAllNotesService
import zhttp.http.Response
import zio.*
import zio.json.*
import route.interface.BasicRequestHandler


class GetAllNotesRoute {
  
  final def handle: RIO[RecordsRetriever[Note], Response] = for {
    service  <- ZIO.service[RecordsRetriever[Note]]
    notes    <- service.retrieveRecords
    response <- ZIO.succeed(Response.text(notes.toJsonPretty))
  } yield response

}
