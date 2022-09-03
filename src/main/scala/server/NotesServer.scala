package server

import db.mongo.{DBConfig, DataSource, DataSourceBuilder, MongoDatabaseBuilder}
import zhttp.http.*
import zhttp.service.Server
import zio.*
import endpoint.*
import io.netty.handler.codec.http.HttpHeaders
import org.mongodb.scala.MongoDatabase
import pdi.jwt.algorithms.JwtUnknownAlgorithm
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}
import server.endpoint.note.{CreateNoteEndpoint, DeleteNoteEndpoint, GetAllNotesEndpoint, GetNoteEndpoint, SearchNoteEndpoint, SortNoteEndpoint, UpdateNoteEndpoint}
import server.endpoint.user.{LoginEndpoint, SignupEndpoint}
import server.middleware.RequestContextManager
import zhttp.html.{Dom, Html}
import zhttp.http.middleware.HttpMiddleware

import java.io.IOException

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

  val allRoutes: Http[RequestContextManager, Throwable, Request, Response] = {
    signupEndpoint.route ++
      loginEndpoint.route ++
      sortNoteEndpoint.route ++
      searchNoteEndpoint.route ++
      getAllNotesEndpoint.route ++
      getNoteEndpoint.route ++
      createNoteEndpoint.route ++
      updateNoteEndpoint.route ++
      deleteNoteEndpoint.route
  }

  def start: ZIO[RequestContextManager & DataSource & DataSourceBuilder, Throwable, Unit] =
    for {
      _      <- ZIO.succeed(println("Server started"))
      port   <- System.envOrElse("PORT", "8080").map(_.toInt)
      dbPort <- System.envOrElse("MONGO_PORT", "mongodb://localhost:27018")
      dbName <- System.envOrElse("MONGO_DB_NAME", "notesdb")
      _      <- ZIO.service[DataSourceBuilder].flatMap(_.initialize(DBConfig(dbPort, dbName)))
      _      <- ZIO.succeed(println(s"Accepting requests on port $port"))
      _      <- Server.start(port, allRoutes)
    } yield ()

}

object NotesServer {

  lazy val layer: URLayer[SignupEndpoint & LoginEndpoint & GetAllNotesEndpoint & GetNoteEndpoint & CreateNoteEndpoint & UpdateNoteEndpoint & DeleteNoteEndpoint & SearchNoteEndpoint & SortNoteEndpoint, NotesServer] =
    ZLayer.fromFunction(NotesServer.apply _)

}

