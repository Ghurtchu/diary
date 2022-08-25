package server

import zhttp.http.*
import zhttp.service.Server
import zio.*
import endpoint._

final case class NotesServer(
                           signupEndpoint: SignupEndpoint,
                           loginEndpoint: LoginEndpoint,
                           getAllNotesEndpoint: GetAllNotesEndpoint,
                           getNoteEndpoint: GetNoteEndpoint,
                           createNoteEndpoint: CreateNoteEndpoint,
                           updateNoteEndpoint: UpdateNoteEndpoint,
                           deleteNoteEndpoint: DeleteNoteEndpoint,
                           searchNoteEndpoint: SearchNoteEndpoint,
                           sortNoteEndpoint: SortNoteEndpoint
                           ) {

  val allRoutes: HttpApp[Any, Throwable] = {
    signupEndpoint.route ++
      loginEndpoint.route ++
      getAllNotesEndpoint.route ++
      getNoteEndpoint.route ++
      createNoteEndpoint.route ++
      updateNoteEndpoint.route ++
      deleteNoteEndpoint.route ++
      searchNoteEndpoint.route ++
      sortNoteEndpoint.route
  }

  def start: Task[Unit] =
    for {
      _    <- ZIO.succeed(println("Server started"))
      port <- System.envOrElse("PORT", "8080").map(_.toInt)
      _    <- ZIO.succeed(println(s"Accepting requests on port $port"))
      _    <- Server.start(port, allRoutes)
    } yield ()

}

object NotesServer {

  lazy val layer: URLayer[SignupEndpoint & LoginEndpoint & GetAllNotesEndpoint & GetNoteEndpoint & CreateNoteEndpoint & UpdateNoteEndpoint & DeleteNoteEndpoint & SearchNoteEndpoint & SortNoteEndpoint, NotesServer] =
    ZLayer.fromFunction(NotesServer.apply _)

}

