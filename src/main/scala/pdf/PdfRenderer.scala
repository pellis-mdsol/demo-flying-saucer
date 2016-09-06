package pdf

import java.io.{ByteArrayOutputStream, StringReader, StringWriter}
import java.nio.file.{Files, Path}

import org.w3c.tidy.Tidy
import org.xhtmlrenderer.context.StyleReference
import org.xhtmlrenderer.pdf.ITextRenderer
import org.xhtmlrenderer.resource.XMLResource

class PdfRenderer(implicit classLoader: ClassLoader) {

  private val renderer = doto(new ITextRenderer) { renderer =>
    val sharedContext = renderer.getSharedContext
    val userAgent = new ClassLoaderUserAgent(
      renderer.getOutputDevice,
      classLoader,
      sharedContext
    )
    sharedContext.setUserAgentCallback(userAgent)
    sharedContext.setCss(new StyleReference(userAgent))
  }

  private val tidy = doto(new Tidy)(_ setXHTML true)

  def toFile(file: Path, body: String, draft: Boolean = false): Path = {
    Files.deleteIfExists(file)
    Files.write(file, toBytes(body, draft))
  }

  def toBytes(body: String, draft: Boolean = false): Array[Byte] =
    toStream(body, draft).toByteArray

  def toStream(body: String, draft: Boolean = false): ByteArrayOutputStream =
    doto(new ByteArrayOutputStream) { output =>
      try {
        val reader = new StringReader(tidify(body))
        val document = XMLResource.load(reader).getDocument
        if(draft) {
          renderer.setListener(new WatermarkListener("DRAFT"))
        }
        renderer.setDocument(document, "")
        renderer.layout()
        renderer.createPDF(output)
      } finally output.close()
    }

  private def tidify(body: String) =
    doto(new StringWriter) {
      tidy.parse(new StringReader(body), _)
    }.toString

  private def doto[T](t: T)(code: T => Unit): T = {code(t); t}
}