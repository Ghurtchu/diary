import route.handler.*
import search.*
import zio.*
import db.*
import db.mongo.{DataSourceLive, MongoDatabaseInitializer}
import hash.*
import server.NotesServer
import server.endpoint.*
import server.middleware.RequestContextManagerLive
import auth.{JwtDecoderLive, JwtEncoderLive}
import db.note.NotesRepositoryLive
import db.user.UserRepositoryLive
import route.service.{CreateNoteServiceLive, DeleteNoteServiceLive, GetAllNotesServiceLive, GetNoteServiceLive, LoginServiceLive, SignupServiceLive, UpdateNoteServiceLive}
import server.endpoint.note.{CreateNoteEndpointLive, DeleteNoteEndpointLive, GetAllNotesEndpointLive, GetNoteEndpointLive, SearchNoteEndpointLive, SortNoteEndpointLive, UpdateNoteEndpointLive}
import server.endpoint.user.{LoginEndpointLive, SignupEndpointLive}

import sort.SortNoteService

object Main extends ZIOAppDefault:

    private lazy val endpointLayers = SignupEndpointLive.layer ++
      CreateNoteEndpointLive.layer ++
      DeleteNoteEndpointLive.layer ++
      GetAllNotesEndpointLive.layer ++
      GetNoteEndpointLive.layer ++
      LoginEndpointLive.layer ++
      SearchNoteEndpointLive.layer ++
      SortNoteEndpointLive.layer ++
      UpdateNoteEndpointLive.layer

    private lazy val serverLayer = NotesServer.layer

    private lazy val handlerLayers = SearchNoteHandlerLive.layer ++
      SortNoteHandlerLive.layer ++
      UpdateNoteHandlerLive.layer ++
      CreateNoteHandlerLive.layer ++
      GetAllNotesHandlerLive.layer ++
      GetNoteHandlerLive.layer ++
      DeleteNoteHandlerLive.layer ++
      LoginHandlerLive.layer ++
      SignupHandlerLive.layer

    private lazy val serviceLayers = SearchNoteService.layer ++
      SortNoteService.layer ++
      UpdateNoteServiceLive.layer ++
      CreateNoteServiceLive.layer ++
      GetAllNotesServiceLive.layer ++
      GetNoteServiceLive.layer ++
      LoginServiceLive.layer ++
      SignupServiceLive.layer ++
      DeleteNoteServiceLive.layer

    private lazy val repoLayers = NotesRepositoryLive.layer ++ UserRepositoryLive.layer ++ MongoDatabaseInitializer.layer

    private lazy val dataSourceLayer = DataSourceLive.layer

    private lazy val otherLayers = SecureHashService.layer ++ RequestContextManagerLive.layer ++ JwtEncoderLive.layer ++ JwtDecoderLive.layer

    override def run: Task[Unit] =
      ZIO.serviceWithZIO[NotesServer](_.start)
        .provide(
          serverLayer,
          endpointLayers,
          handlerLayers,
          serviceLayers,
          repoLayers,
          dataSourceLayer,
          otherLayers
        )

