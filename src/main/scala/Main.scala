import java.net.URLClassLoader
import java.nio.file.{Files, Paths}

import pdf.PdfRenderer

import scala.io.{Codec, Source}

object Main extends App {
  val assets = Paths.get("./assets")
  implicit val classLoader = new URLClassLoader(Array(assets.toUri.toURL))

  System.getProperties.setProperty("xr.util-logging.loggingEnabled", "true")

  val letter = Paths.get("letter.pdf")

  val draftLetter = Paths.get("letterDraft.pdf")

  val htmlSource = Source.fromInputStream(classLoader.getResourceAsStream("letter.html"))(Codec.UTF8).mkString.trim

  val renderer = new PdfRenderer

  renderer.toFile(letter, htmlSource)
  renderer.toFile(draftLetter, htmlSource, draft = true)
}