import java.io.IOException

object EstimatePi extends zio.App {

  import zio._
  import zio.console._
  import zio.random._
  import zio.duration._

  final case class PiState(inside: Long, total: Long)

  def estimatedPi(inside: Long, total: Long): Double = 4.0 * (inside.toDouble / total.toDouble)

  def isInsideCircle(x: Double, y: Double): Boolean = Math.sqrt(x * x + y * y) <= 1.0

  val randomPoint: ZIO[Random, Nothing, (Double, Double)] = nextDouble zip nextDouble

  def updatePiState(ref: Ref[PiState]): ZIO[Random, Nothing, Unit] =
    for {
      tuple <- randomPoint
      (x, y) = tuple
      inside = if (isInsideCircle(x, y)) 1 else 0
      _ <- ref.update(state => PiState(state.inside + inside, state.total + 1))
    } yield ()

  def printPiStimation(ref: Ref[PiState]): ZIO[Console, IOException, Unit] =
    for {
      state <- ref.get
      _ <- putStrLn(s"Iterations number: ${state.total} -> Pi: ${estimatedPi(state.inside, state.total)}")
    } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    (for {
      ref <- Ref.make((PiState(0L, 0L)))
      worker = updatePiState(ref).forever
      workers = List.fill(8)(worker)
      fiber1 <- ZIO.forkAll(workers)
      fiber2 <- (printPiStimation(ref) *> ZIO.sleep(1.second)).forever.fork
      _ <- putStrLn("Press any key to terminate...")
      _ <- getStrLn *> (fiber1 zip fiber2).interrupt
    } yield 0).exitCode
}
