package route.handler

import model.Note
import route.interface
import interface._
import route.implementation.GetNoteService
import zhttp.http.Response
import zio.*
import zio.json.*
import zhttp.http.Status

class GetNoteRoute extends CommonRequestHandler[Int, RecordRetriever[Note]] { 

  final override def handle(id: Int): RIO[RecordRetriever[Note], Response] = for {
    service   <- ZIO.service[RecordRetriever[Note]]
    maybeNote <- service.retrieveRecord(id)
    response  <- maybeNote.fold(
      notFound => ZIO.succeed(Response.text(notFound).setStatus(Status.NotFound)),
      note     => ZIO.succeed(Response.text(note.toJson).setStatus(Status.Ok))
    )
  } yield response

}
