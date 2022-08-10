package route

import db.CRUD
import service.*

import java.util.Date
import java.time.Instant
import model.*
import service.*
import service.search.{NotesSearchService, SearchService}
import zio.*
import zhttp.http.*
import zio.json._

object NotesRoute {

  val notesService: NotesService.type    = NotesService
  val searchService: SearchService[Note] = NotesSearchService

  def getAllNotes: UIO[Response] = for {
    notes    <- notesService.getAll
    response <- ZIO.succeed(Response.text(notes.toJsonPretty))
  } yield response

  def getNoteById(id: Int): Task[Response] =
    notesService.getById(id)
      .fold(_ => Response.text("Error occurred while searching"),
        maybeNote => maybeNote.fold(Response.text(s"Note with id $id was not found"))
        (note => Response.text(note.toJsonPretty)))

  def searchByTitle(title: String): Task[Response] =
    searchService.searchByTitle(title)
      .fold(
        _ => Response.text("Error occurred while searching"),
        maybeNote => maybeNote.fold(
          Response.text(s"Note with title $title does not exist"))
        (note => Response.text(note.toJsonPretty)))

  def removeNoteById(id: Int): Task[Response] = for {
    deleteStatus <- notesService delete id
    response     <- ZIO.succeed {
      if !deleteStatus then Response.text(s"Note with id $id does not exist")
      else Response.text(s"Note with id $id was deleted successfully")
    }
  } yield response

  def addNote(noteAsString: String): Task[Response] = for {
    noteEither <- ZIO.succeed(noteAsString.fromJson[Note])
    status     <- noteEither.fold(err => ZIO.fail(RuntimeException(err)) , notesService.add)
    response   <- ZIO.succeed {
      if status then Response.text(s"Note with title ${noteEither.map(_.title)} was added successfully")
      else Response.text(s"Note with title ${noteEither.map(_.title)} was not added")
    }
  } yield response





}
