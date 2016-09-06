package pdf

import java.awt.Color

import com.lowagie.text._
import com.lowagie.text.pdf._

class DraftPageEvent(text: String) extends PdfPageEventHelper {

  private val watermark = new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 80, Font.BOLD, Color.LIGHT_GRAY))

  override def onEndPage(writer: PdfWriter, document: Document) {
    val canvas: PdfContentByte = writer.getDirectContentUnder

    val x = (writer.getPageSize.getLeft + writer.getPageSize.getRight) / 2
    val y = (writer.getPageSize.getTop + writer.getPageSize.getBottom) / 2

    ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, watermark, x, y, 45)
  }0
}
