package server.endpoint.note

import server.middleware.RequestContextManager

object NoteEndpointDefinitions:
  trait GetAllNotesEndpoint extends HasRoute[RequestContextManager]
  trait SearchNoteEndpoint  extends HasRoute[RequestContextManager]
  trait DeleteNoteEndpoint  extends HasRoute[RequestContextManager]
  trait CreateNoteEndpoint  extends HasRoute[RequestContextManager]
  trait UpdateNoteEndpoint  extends HasRoute[Any]
  trait SortNoteEndpoint    extends HasRoute[RequestContextManager]
  trait GetNoteEndpoint     extends HasRoute[RequestContextManager]

