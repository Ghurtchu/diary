import route.handler.*
import route.implementation.*
import util.search.*
import zio.*
import db.*
import util.hash.*
import server.NotesServer
import server.endpoint.{CreateNoteEndpoint, DeleteNoteEndpoint, GetAllNotesEndpoint, GetNoteEndpoint, LoginEndpoint, SearchNoteEndpoint, SignupEndpoint, SortNoteEndpoint, UpdateNoteEndpoint}
import util.sort.SortNoteService

object Main extends ZIOAppDefault {

    val endpointLayers = SignupEndpoint.layer ++
      CreateNoteEndpoint.layer ++
      DeleteNoteEndpoint.layer ++
      GetAllNotesEndpoint.layer ++
      GetNoteEndpoint.layer ++
      LoginEndpoint.layer ++
      SearchNoteEndpoint.layer ++
      SortNoteEndpoint.layer ++
      UpdateNoteEndpoint.layer

    val serverLayer = NotesServer.layer

    val handlerLayers = SearchNoteHandlerImpl.layer ++
      SortNoteHandlerImpl.layer ++
      UpdateNoteHandlerImpl.layer ++
      CreateNoteHandlerImpl.layer ++
      GetAllNotesHandlerImpl.layer ++
      GetNoteHandlerImpl.layer ++
      DeleteNoteHandlerImpl.layer ++
      LoginHandlerImpl.layer ++
      SignupHandlerImpl.layer

    val serviceLayers = SearchNoteService.layer ++
      SortNoteService.layer ++
      UpdateNoteService.layer ++
      CreateNoteService.layer ++
      GetAllNotesService.layer ++
      GetNoteService.layer ++
      LoginServiceImpl.layer ++
      SignupServiceImpl.layer ++
      DeleteNoteService.layer

    val dbLayers = NotesRepository.layer ++ UserRepository.layer

    val otherLayers = SecureHashService.layer

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
