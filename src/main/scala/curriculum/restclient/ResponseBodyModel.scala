package curriculum.restclient

import curriculum.restclient.RequestModel.UserRequest.User
import io.circe.syntax.EncoderOps
import io.circe.Printer
import io.circe.generic.auto._

object ResponseBodyModel {

  object PostUserResponse {
    final case class PostUserResponse(code: Int, message: String, command: String, rowcount: Int, body: User)

  }
  object GetUsersResponse {
    type GetUsersResponseBody = List[User]
    final case class GetUsersResponse(code: Int, data: List[User])

  }
  final case class User(
    id: Option[String],
    name: String,
    surname: String,
    nickname: String,
    email: String
 ){
    def toJson(): String =
      Printer.noSpaces.copy(dropNullValues = true).print(new User(id, name, surname,nickname, email).asJson)
  }
}
