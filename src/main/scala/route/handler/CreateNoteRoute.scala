package route.handler

import model.Note
import route.interface._
import zhttp.http.Response
import zio.*
import zhttp.http.Request
import zio.json.*

import java.net.http.HttpResponse.ResponseInfo

class CreateNoteRoute extends CommonRequestHandler[Request, RecordCreator[Note]] {

  final override def handle(request: Request): RIO[RecordCreator[Note], Response] = for {
    service        <- ZIO.service[RecordCreator[Note]]
    noteAsJson     <- request.bodyAsString
    noteEither     <- ZIO.succeed(noteAsJson.fromJson[Note])
    creationStatus <- noteEither.fold(
      err => ZIO.fail(RuntimeException(err)),
      service.createRecord
    )
    response       <- ZIO.succeed(creationStatus.fold(
      Response.text,
      Response.text
    ))
  } yield response

}
