package com.jensyl.scannyxam;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;

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
import com.jensyl.scannyxam.database.Badging;
import com.jensyl.scannyxam.database.ScannyXamDatabase;
import com.jensyl.scannyxam.database.UserWithBadgings;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfUtil {
    private static final Font FONT_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);

    private static final Font FONT_CELL = new Font(Font.FontFamily.TIMES_ROMAN,  12, Font.NORMAL);
    private static final Font FONT_COLUMN = new Font(Font.FontFamily.TIMES_ROMAN,  14, Font.NORMAL);

    public interface OnDocumentClose
    {
        void onPDFDocumentClose(File file);
    }

    static void createPdf(@NonNull Context mContext, OnDocumentClose mCallback, @NonNull String filePath) throws Exception
    {
        new Thread(() -> {
            try {
                if (filePath.equals("")) {
                    throw new NullPointerException("PDF File Name can't be null or blank. PDF File can't be created");
                }

                File file = new File(filePath);

                if (file.exists()) {
                    file.delete();
                    Thread.sleep(50);
                }

                Document document = new Document();
                document.setMargins(24f, 24f, 32f, 32f);
                document.setPageSize(PageSize.A4);

                PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filePath));
                pdfWriter.setFullCompression();
                pdfWriter.setPageEvent(new PageNumeration());

                document.open();

                setMetaData(document);

                addHeader("Relevé de pointage", document);
                addEmptyLine(document, 3);
                ScannyXamDatabase db = Room
                        .databaseBuilder(
                                mContext,
                                ScannyXamDatabase.class,
                                "scanny-xam")
                        .build();

                createDataTable(db.userDao().getUsersWithBadgings(),document);

                addEmptyLine(document, 2);

                document.close();

                try {
                    pdfWriter.close();
                } catch (Exception ignored) {
                }

                Looper.prepare();
                if (mCallback != null) {
                    mCallback.onPDFDocumentClose(file);
                }
                Looper.loop();
                Looper.myLooper().quit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
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

    private static void addHeader(String text, Document document) throws Exception
    {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
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

            Paragraph temp = new Paragraph(text ,FONT_TITLE);
            temp.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(temp);

            table.addCell(cell);
        }

        document.add(table);
    }

    private static void createDataTable(List<UserWithBadgings> dataTable, Document document) throws Exception
    {
        HashMap<String, HashMap<String,String[]>> values = new HashMap<>();
        for (UserWithBadgings userWithBadgings: dataTable) {
            String username = userWithBadgings.user.firstName + " " + userWithBadgings.user.lastName;

            Log.i("Test", String.valueOf(userWithBadgings.Badgings.size()));
            for (Badging badging: userWithBadgings.Badgings) {
                if(!values.containsKey(badging.exam)) {
                    values.put(badging.exam, new HashMap<>());
                }
                HashMap<String, String[]> examHashMap = values.get(badging.exam);
                assert examHashMap != null;
                if(!examHashMap.containsKey(username)) {
                    String[] badges = new String[2];
                    badges[0] = badging.date;
                    examHashMap.put(username, badges);
                }
                else {
                    String[] badges = examHashMap.get(username);
                    assert badges != null;
                    badges[1] = badging.date;
                    examHashMap.put(username,badges);
                }
            }
        }

        Log.i("Test",values.toString());

        for(Map.Entry<String, HashMap<String,String[]>> entry : values.entrySet()) {
            String exam = entry.getKey();
            HashMap<String, String[]> userBadges = entry.getValue();

            addHeader(exam,document);

            PdfPTable table1 = new PdfPTable(3);
            table1.setWidthPercentage(100);
            table1.setHeaderRows(1);
            table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell;
            {
                cell = new PdfPCell(new Phrase("Etudiant", FONT_COLUMN));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(4f);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Entrée", FONT_COLUMN));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(4f);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Sortie", FONT_COLUMN));
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

            for (Map.Entry<String,String[]> userBadge: userBadges.entrySet()) {
                String user = userBadge.getKey();
                String[] badges = userBadge.getValue();

                cell_color = alternate ? lt_gray : BaseColor.WHITE;

                cell = new PdfPCell(new Phrase(user, FONT_CELL));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPaddingLeft(left_right_Padding);
                cell.setPaddingRight(left_right_Padding);
                cell.setPaddingTop(top_bottom_Padding);
                cell.setPaddingBottom(top_bottom_Padding);
                cell.setBackgroundColor(cell_color);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(badges[0], FONT_CELL));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPaddingLeft(left_right_Padding);
                cell.setPaddingRight(left_right_Padding);
                cell.setPaddingTop(top_bottom_Padding);
                cell.setPaddingBottom(top_bottom_Padding);
                cell.setBackgroundColor(cell_color);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(badges[1], FONT_CELL));
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

            document.add(table1);
        }
    }
}
