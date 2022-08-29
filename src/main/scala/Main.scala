import route.handler.*
import route.implementation.*
import util.search.*
import zio.*
import db.*
import util.hash.*
import server.NotesServer
import server.endpoint.*
import server.endpoint.note.{CreateNoteEndpointLive, DeleteNoteEndpointLive, GetAllNotesEndpointLive, GetNoteEndpointLive, SearchNoteEndpointLive, SortNoteEndpointLive, UpdateNoteEndpointLive}
import server.endpoint.user.{LoginEndpointLive, SignupEndpointLive}
import server.middleware.RequestContextManagerLive
import util.auth.JwtEncoderLive
import util.sort.SortNoteService

object Main extends ZIOAppDefault {

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
      UpdateNoteService.layer ++
      CreateNoteService.layer ++
      GetAllNotesService.layer ++
      GetNoteService.layer ++
      LoginServiceLive.layer ++
      SignupServiceLive.layer ++
      DeleteNoteService.layer

    private lazy val dbLayers = NotesRepository.layer ++ UserRepository.layer

    private lazy val otherLayers = SecureHashService.layer ++ RequestContextManagerLive.layer ++ JwtEncoderLive.layer

    override def run: Task[Unit] =
      ZIO.serviceWithZIO[NotesServer](_.start)
        .provide(
          serverLayer,
          endpointLayers,
          handlerLayers,
          serviceLayers,
          dbLayers,
          otherLayers
        )

}
