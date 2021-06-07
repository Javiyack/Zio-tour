package curriculum

import curriculum.config.Config
import curriculum.operations.{ApiClientService, deleteUser, getUsers, postUser}
import curriculum.restclient.RequestModel.UserRequest
import curriculum.restclient.{APIDeserializationException, APIRequestException, HttpClient, ResponseBodyModel}
import curriculum.restclient.RequestModel.UserRequest.{User, UserRequestBody}
import curriculum.restclient.ResponseBodyModel.GetUsersResponse.GetUsersResponse
import curriculum.restclient.ResponseBodyModel.PostUserResponse.PostUserResponse
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.clock.Clock
import zio.console.{Console, getStrLn, putStr, putStrLn}
import zio.{ZIO, ZLayer}

import java.io.IOException
import java.util.UUID

object Main extends App {
  val sttpClient = ZLayer.fromManaged(AsyncHttpClientZioBackend.managed())
  val curriculumAPILayer = (Config.live ++ sttpClient) >>> HttpClient.live

  val mainLoop: ZIO[zio.ZEnv, Nothing, Unit] =
    for {
      _ <- putStrLn("Wellcome to User administrator page")
    _ <- showMenu.tapError(error => putStrLn(s"chungo: $error")) orElse ZIO.succeed({
      println("fallo")
    })
      _ <- choseAnAction.tapBoth(error => putStrLn(s"chungo: $error"),
        success => putStrLn(s"FIN: $success")) orElse ZIO.succeed({
        println("fallo")
      })
    } yield ()

  val createUserRequestEffect: ZIO[Console, IOException, UserRequest.UserRequest] =
    for {
      _ <- putStr("name?  ")
      name <- getStrLn
      _ <- putStr("surname?  ")
      surname <- getStrLn
      _ <- putStr("nickname?  ")
      nickname <- getStrLn
      _ <- putStr("email?  ")
      email <- getStrLn
    } yield (UserRequest.UserRequest(User(name, surname, nickname, email)))

  val userCreation: ZIO[Clock with ApiClientService with Console, Throwable, Unit] =
    (for {
      _ <- putStrLn("\nNew User Wizard:\n----------------")
      newUser <- createUserRequestEffect
      _ <- zPostUser(newUser.body)
      _ <- showMenu
      _ <- choseAnAction
    } yield ()).tapBoth(error => putStrLn(s"chungo: $error"),
      success => putStrLn(s"Bien: $success")) orElse ZIO.succeed(println("fallo en show user")) as 0

  val showUsers: ZIO[Clock with ApiClientService with Console, Nothing, Unit] =
    (for {
      users <- zGetUsers()
      _ <- showUsers(users.data)
      _ <- showMenu
      _ <- choseAnAction
    } yield ()) orElse ZIO.succeed(println("fallo en show user")) as 0

  val userDelete: ZIO[Console with ApiClientService, Nothing, Unit] =
    for {
      _ <- putStr("Please, provide user id you wish to delete:  ")
      name <- getStrLn orElse putStrLn("userDelete getStrLn") *> ZIO.succeed("aasdad petao")
      _ <- zDeleteUser(name) orElse putStrLn("userDelete zDeleteUser") *> ZIO.succeed("aasdad petao")
      _ <- showMenu orElse putStrLn("userDelete showMenu") *> ZIO.succeed("aasdad petao")
      _ <- choseAnAction orElse putStrLn("userDelete choseAnAction") *> ZIO.succeed("aasdad petao")
    } yield ()

  def choseAnAction: ZIO[Console, Throwable, Unit] =
    for {
      _ <- putStrLn("Choose an option")
      answer <- getStrLn
      action = answer match {
        case "0" => println("Bye")
        case "1" => zio.Runtime.default.unsafeRun(showUsers.provideCustomLayer(Clock.live ++ curriculumAPILayer))
        case "2" => zio.Runtime.default.unsafeRun(userCreation.provideCustomLayer(Clock.live ++ curriculumAPILayer))
        case "3" => zio.Runtime.default.unsafeRun(userDelete.provideCustomLayer(Clock.live ++ curriculumAPILayer))
        case _ => {
          println("Opcion Incorrecta")
          zio.Runtime.default.unsafeRun(choseAnAction)
        }
      }
    } yield (action)


  def showMenu: ZIO[Console, IOException, Unit] = {
    val actions = List("Salir", "Ver Usuarios", "Crear Usuario", "Eliminar Usuario")
    for {
      _ <- putStrLn(List.fill(35)("_").mkString(""))
      n <- ZIO effectTotal (0 to actions.size - 1).map(n => {
        val action = actions(n)
        val size = actions(n).size
        println(s"\t\t$n.\t${action.concat(List.fill(21 - size)(" ").mkString(""))}")
      }
      )
      _ <- putStrLn(List.fill(35)("_").mkString("") + "\n")
    } yield ()

  }

  def showUsers(users: List[ResponseBodyModel.User]): ZIO[Console, Throwable, Unit] =
    for {
      _ <- ZIO.effectTotal(users.map(u => {
        val lastField = u.productElementNames.length - 1
        val uuid = UUID.randomUUID()
        val id = u.id.getOrElse(uuid)
        println(s"$id\t-\t${
          (1 to lastField)
            .map(i => u.productElement(i).toString
              .concat(" " +
                List.fill(30 - u.productElement(i).toString.size)("_").mkString(""))
            ).mkString("\t")
        }")
      }))
    } yield ()


  zio.Runtime.default.unsafeRun(mainLoop)


  def zGetUsers(): ZIO[ApiClientService, Throwable, GetUsersResponse] =
    for {
      result <- getUsers
        .tapBoth(
          error => error match {
            case sttpFail: APIRequestException => ZIO.succeed(println(s"APIRequestException - unable to GET /users - code: ${sttpFail.code} - msg: ${sttpFail.errorMessage}"))
            case circeFail: APIDeserializationException => ZIO.succeed(println(s"APIDeserializationException - unable to GET /users - Error: ${circeFail.errorMessage}"))
            case _ => ZIO.succeed(println(s"unable to GET /users"))
          },
          success =>
            ZIO.succeed(
              println(
                s"Successful GET /users."
              )
            )
        )
    } yield result

  def zPostUser(user: UserRequestBody): ZIO[ApiClientService, Throwable, PostUserResponse] =
    for {
      result <- postUser(user)
        .tapBoth(
          error => error match {
            case sttpFail: APIRequestException => ZIO.succeed(println(s"APIRequestException - unable to POST /users - code: ${sttpFail.code} - msg: ${sttpFail.errorMessage}"))
            case circeFail: APIDeserializationException => ZIO.succeed(println(s"APIDeserializationException - unable to POST /users - Error: ${circeFail.errorMessage}"))
            case _ => ZIO.succeed(println(s"unable to POST /users - ${UserRequest.toJson(user)}"))
          },
          _ =>
            ZIO.succeed(
              println(
                s"Successful POST /users - ${UserRequest.toJson(user)}"
              )
            )
        )
    } yield result

  def zDeleteUser(userId: String): ZIO[ApiClientService, Throwable, PostUserResponse] =
    for {
      result <- deleteUser(userId)
        .tapBoth(
          error => error match {
            case sttpFail: APIRequestException => ZIO.succeed(println(s"APIRequestException - unable to DELETE /users - code: ${sttpFail.code} - msg: ${sttpFail.errorMessage}"))
            case circeFail: APIDeserializationException => ZIO.succeed(println(s"APIDeserializationException - unable to DELETE /users - Error: ${circeFail.errorMessage}"))
            case _ => ZIO.succeed(println(s"unable to DELETE /users - $userId"))
          },
          _ =>
            ZIO.succeed(
              println(
                s"Successful DETELE /users - $userId"
              )
            )
        )
    } yield result


}