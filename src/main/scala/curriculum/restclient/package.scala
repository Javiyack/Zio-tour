package curriculum

import curriculum.restclient.HttpClient
import curriculum.restclient.RequestModel.UserRequest.UserRequestBody
import curriculum.restclient.ResponseBodyModel.GetUsersResponse.{GetUsersResponse, GetUsersResponseBody}
import curriculum.restclient.ResponseBodyModel.PostUserResponse.PostUserResponse
import zio.{Has, ZIO}

package object operations {
  type ApiClientService = Has[HttpClient.Service]

  def postUser(
                payload: UserRequestBody
              ): ZIO[ApiClientService, Throwable, PostUserResponse] =
    ZIO.accessM(_.get.postUser(payload))
  def deleteUser(
                id: String
              ): ZIO[ApiClientService, Throwable, PostUserResponse] =
    ZIO.accessM(_.get.deleteUser(id))

  def getUsers(): ZIO[ApiClientService, Throwable, GetUsersResponse] =
    ZIO.accessM(_.get.getUsers)
}
