package com.jensyl.scannyxam;

import android.content.Context;

import androidx.annotation.NonNull;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class PdfUtil {
    private static final Font FONT_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);

    private static final Font FONT_CELL = new Font(Font.FontFamily.TIMES_ROMAN,  12, Font.NORMAL);
    private static final Font FONT_COLUMN = new Font(Font.FontFamily.TIMES_ROMAN,  14, Font.NORMAL);

    public interface OnDocumentClose
    {
        void onPDFDocumentClose(File file);
    }

    static void createPdf(@NonNull Context mContext, OnDocumentClose mCallback, List<String[]> items, @NonNull String filePath) throws Exception
    {
        if(filePath.equals(""))
        {
            throw new NullPointerException("PDF File Name can't be null or blank. PDF File can't be created");
        }

        File file = new File(filePath);

        if(file.exists())
        {
            file.delete();
            Thread.sleep(50);
        }

        Document document = new Document();
        document.setMargins(24f,24f,32f,32f);
        document.setPageSize(PageSize.A4);

        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        pdfWriter.setFullCompression();
        pdfWriter.setPageEvent(new PageNumeration());

        document.open();

        setMetaData(document);

        addHeader(mContext,document);
        addEmptyLine(document, 3);

        document.add(createDataTable(items));

        addEmptyLine(document,2);

        document.close();

        try
        {
            pdfWriter.close();
        }
        catch (Exception ignored)
        {}

        if(mCallback!=null)
        {
            mCallback.onPDFDocumentClose(file);
        }
    }

    private static  void addEmptyLine(Document document, int number) throws DocumentException
    {
        for (int i = 0; i < number; i++)
        {
            document.add(new Paragraph(" "));
        }
    }

    private static void setMetaData(Document document)
    {
        document.addCreationDate();
        document.addAuthor( "Allan MERCOU & Mike DEVRESSE");
        document.addCreator("Allan MERCOU & Mike DEVRESSE");
        document.addHeader("DEVELOPER","Allan MERCOU & Mike DEVRESSE");
    }

    private static void addHeader(Context mContext, Document document) throws Exception
    {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2,7,2});
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell;
        {
            /*MIDDLE TEXT*/
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(8f);
            cell.setUseAscender(true);

            Paragraph temp = new Paragraph("Relevé de pointage" ,FONT_TITLE);
            temp.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(temp);

            table.addCell(cell);
        }

        document.add(table);
    }

    private static PdfPTable createDataTable(List<String[]> dataTable) throws DocumentException
    {
        PdfPTable table1 = new PdfPTable(2);
        table1.setWidthPercentage(100);
        table1.setWidths(new float[]{1f,2f});
        table1.setHeaderRows(1);
        table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell;
        {
            cell = new PdfPCell(new Phrase("COLUMN - 1", FONT_COLUMN));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(4f);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("COLUMN - 2", FONT_COLUMN));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(4f);
            table1.addCell(cell);
        }

        float top_bottom_Padding = 8f;
        float left_right_Padding = 4f;
        boolean alternate = false;

        BaseColor lt_gray = new BaseColor(221,221,221); //#DDDDDD
        BaseColor cell_color;

        int size = dataTable.size();

        for (int i = 0; i < size; i++)
        {
            cell_color = alternate ? lt_gray : BaseColor.WHITE;
            String[] temp = dataTable.get(i);

            cell = new PdfPCell(new Phrase(temp[0], FONT_CELL));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPaddingLeft(left_right_Padding);
            cell.setPaddingRight(left_right_Padding);
            cell.setPaddingTop(top_bottom_Padding);
            cell.setPaddingBottom(top_bottom_Padding);
            cell.setBackgroundColor(cell_color);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(temp[1], FONT_CELL));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPaddingLeft(left_right_Padding);
            cell.setPaddingRight(left_right_Padding);
            cell.setPaddingTop(top_bottom_Padding);
            cell.setPaddingBottom(top_bottom_Padding);
            cell.setBackgroundColor(cell_color);
            table1.addCell(cell);

            alternate = !alternate;
        }

        return table1;
    }
}
