package pdf

import org.xhtmlrenderer.pdf.{DefaultPDFCreationListener, ITextRenderer}

class WatermarkListener(watermark: String) extends DefaultPDFCreationListener {
  override def preWrite(iTextRenderer: ITextRenderer, pageCount: Int): Unit = {
    iTextRenderer.getWriter.setPageEvent(new DraftPageEvent(watermark))
  }
}