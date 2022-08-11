package route

import zhttp.http.Response

package object implementation {
  
    extension (eitherStringString: Either[String, String])
      def getMessage: String = eitherStringString.fold(identity, identity)
      def toResponse: Response = Response text getMessage
    
    extension(message: String)
      def toResponse: Response = Response text message
}
