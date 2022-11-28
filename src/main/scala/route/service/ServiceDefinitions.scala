package route.service

import domain.Domain.{LoginPayload, Note, User}
import route.service.ServiceDefinitions.LoginService.{JWT, LoginError}
import zio.Task

object ServiceDefinitions:

  trait CreateNoteService:
    def createNote(note: Note): Task[Either[String, String]]

  trait DeleteNoteService:
    def deleteRecord(noteId: Long, userId: Long): Task[Either[String, String]]

  trait GetAllNotesService:
    def getNotesByUserId(userId: Long): Task[List[Note]]

  trait GetNoteService:
    def getNote(noteId: Long, userId: Long): Task[Either[String, Note]]

  trait LoginService:
    def login(loginPayload: LoginPayload): Task[Either[LoginError, JWT]]

  object LoginService:
    case class JWT(value: String)
    case class LoginError(value: String)

  trait SignupService:
    def signUp(user: User): Task[Either[String, String]]

  trait UpdateNoteService:
    def updateNote(noteId: Long, newNote: Note): Task[Either[String, String]]

