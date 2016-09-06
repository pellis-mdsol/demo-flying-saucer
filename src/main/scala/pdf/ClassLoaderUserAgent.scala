package pdf

import org.xhtmlrenderer.layout.SharedContext
import org.xhtmlrenderer.pdf.{ITextOutputDevice, ITextUserAgent}

class ClassLoaderUserAgent(
                            outputDevice: ITextOutputDevice,
                            classLoader: ClassLoader,
                            sharedContext: SharedContext
                          ) extends ITextUserAgent(outputDevice) {

  setSharedContext(sharedContext)

  override protected def resolveAndOpenStream(uri: String) =
    Option(classLoader getResourceAsStream uri) getOrElse super.resolveAndOpenStream(uri)

  override protected def resolveURI(uri: String): String = uri
}