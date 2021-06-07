package curriculum.restclient

import curriculum.restclient.ResponseBodyModel.GetUsersResponse.GetUsersResponseBody
import org.apache.commons.lang3.builder.ToStringBuilder
import io.circe.syntax._
import io.circe.Printer
import io.circe.generic.auto._

object RequestModel {

  object UserRequest {
    type UserRequestBody = User

    override def toString: String = ToStringBuilder.reflectionToString(this)

    def toJson(body: UserRequestBody): String =
      Printer.noSpaces.copy(dropNullValues = true).print(body.asJson)
    def toJson(body: GetUsersResponseBody): String =
      Printer.noSpaces.copy(dropNullValues = true).print(body.asJson)

    case class UserRequest(body: UserRequestBody)

    case class User(
      name: String,
      surname: String,
      nickname: String,
      email: String
    ){
      def toJson(): String =
        Printer.noSpaces.copy(dropNullValues = true).print(new User(name, surname,nickname, email).asJson)

    }
  }
}
