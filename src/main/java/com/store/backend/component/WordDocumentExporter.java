package com.store.backend.component;


import com.store.backend.data.dto.SellsPerCategoryReport;
import com.store.backend.data.dto.SellsPerItemReport;
import com.store.backend.data.dto.SellsPerShopReport;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class WordDocumentExporter {

    public void createWordDocForShops(List<SellsPerShopReport> sellsPerShopReports) throws IOException {
        XWPFDocument document = new XWPFDocument();
        FileOutputStream outputStream = new FileOutputStream("generated/" + "Shops" + "_" + UUID.randomUUID() + ".docx");
        XWPFParagraph paragraph = document.createParagraph();

        for (SellsPerShopReport sellPerShopReports : sellsPerShopReports) {
            XWPFRun header = paragraph.createRun();
            header.setBold(true);
            header.setFontSize(16);
            header.setText(sellPerShopReports.getShop().getShopName());
            header.addBreak();

            XWPFRun body = paragraph.createRun();
            body.setText("sells: " + sellPerShopReports.getSells());
            body.addBreak();
            body.addBreak();
        }
        document.write(outputStream);
        outputStream.close();

    }

    public void createWordDocForItems(List<SellsPerItemReport> sellsPerItemReport) throws IOException {
        XWPFDocument document = new XWPFDocument();
        FileOutputStream outputStream = new FileOutputStream("generated/" + "Items" + "_" + UUID.randomUUID() + ".docx");
        XWPFParagraph paragraph = document.createParagraph();

        for (SellsPerItemReport sellPerShopReports : sellsPerItemReport) {
            XWPFRun header = paragraph.createRun();
            header.setBold(true);
            header.setFontSize(16);
            header.setText(sellPerShopReports.getItem().getName());
            header.addBreak();

            XWPFRun body = paragraph.createRun();
            body.setText("sells: " + sellPerShopReports.getCount());
            body.addBreak();
            body.addBreak();
        }
        document.write(outputStream);
        outputStream.close();
    }

    public void createWordDocForCategory(List<SellsPerCategoryReport> sellsPerItemReport) throws IOException {
        XWPFDocument document = new XWPFDocument();
        FileOutputStream outputStream = new FileOutputStream("generated/" + "Category" + "_" + UUID.randomUUID() + ".docx");
        XWPFParagraph paragraph = document.createParagraph();

        for (SellsPerCategoryReport sellPerShopReports : sellsPerItemReport) {
            XWPFRun header = paragraph.createRun();
            header.setBold(true);
            header.setFontSize(16);
            header.setText(sellPerShopReports.getCategory());
            header.addBreak();

            XWPFRun body = paragraph.createRun();
            body.setText("sells: " + sellPerShopReports.getCount());
            body.addBreak();
            body.addBreak();
        }
        document.write(outputStream);
        outputStream.close();
    }
}
