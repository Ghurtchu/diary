package server.endpoint.note.protocol

import server.endpoint.note.HasRoute
import server.middleware.RequestContextManager

trait DeleteNoteEndpoint extends HasRoute[RequestContextManager]
