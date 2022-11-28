package route.handler

import domain.Domain.JwtContent
import zhttp.http.{Request, Response}
import zio.Task

object RequestHandlerDefinitions:

  trait CreateNoteHandler:
    def handle(request: Request, jwtContent: JwtContent): Task[Response]

  trait DeleteNoteHandler:
    def handle(noteId: Long, userId: Long): Task[Response]

  trait GetAllNotesHandler:
    def handle(jwtContent: JwtContent): Task[Response]

  trait GetNoteHandler:
    def handle(noteId: Long, userId: Long): Task[Response]

  trait LoginHandler:
    def handle(request: Request): Task[Response]

  trait SearchNoteHandler:
    def handle(request: Request, jwtContent: JwtContent): Task[Response]

  trait SignupHandler:
    def handle(request: Request): Task[Response]

  trait SortNoteHandler:
    def handle(request: Request, jwtContent: JwtContent): Task[Response]

  trait UpdateNoteHandler:
    def handle(request: Request, noteId: Long): Task[Response]
