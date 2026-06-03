package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.entity.*;
import com.cocobizz.cocobizz.repository.ShipmentRepository;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List; // ✅ IMPORTANT

@Service
@RequiredArgsConstructor
public class PdfService {

    private final ShipmentRepository shipmentRepo;

    private static final String BASE_PATH = "C:/cocobizz/dc/";
    private static final String BARCODE_PATH = "C:/cocobizz/barcodes/";

    public String generateDC(DeliveryChallan dc) {

        try {

            Files.createDirectories(Paths.get(BASE_PATH));

            String fileName = dc.getDcNumber() + ".pdf";
            String fullPath = BASE_PATH + fileName;

            PdfWriter writer = new PdfWriter(fullPath);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);

            // ================= HEADER =================
            doc.add(new Paragraph("COCOBIZZ SYSTEM")
                    .setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph("DELIVERY CHALLAN (DC)")
                    .setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph("--------------------------------------------------"));

            // ================= BASIC =================
            doc.add(new Paragraph("DC No: " + dc.getDcNumber()));
            doc.add(new Paragraph("Date: " + dc.getCreatedDate()));

            doc.add(new Paragraph("\n"));

            // ================= PRODUCT =================
            StockInflow stock = dc.getStockList().get(0);

            doc.add(new Paragraph("📦 Product: " + stock.getProductName()));
            doc.add(new Paragraph("📊 Quantity: " + stock.getQuantity() + " " + stock.getUnitType()));
            doc.add(new Paragraph("🏷 Category: " + stock.getCategoryName()));

            doc.add(new Paragraph("\n"));

            // ================= FARMER =================
            Users farmer = stock.getFarmer();

            doc.add(new Paragraph("👨‍🌾 Farmer:"));
            doc.add(new Paragraph("Name: " + farmer.getUserName()));
            doc.add(new Paragraph("Phone: " + farmer.getPhone()));
            doc.add(new Paragraph("Address: " + farmer.getAddress()));

            doc.add(new Paragraph("\n"));

            // ================= WAREHOUSE =================
            Warehouse w = stock.getWarehouse();

            doc.add(new Paragraph("🏬 Warehouse:"));
            doc.add(new Paragraph("Name: " + w.getName()));
            doc.add(new Paragraph("Location: " + w.getLocation()));

            doc.add(new Paragraph("\n"));

            // ================= SHIPMENT =================
            List<Shipment> shipments = shipmentRepo.findByStockInflow_Id(stock.getId());

            doc.add(new Paragraph("🚚 Shipment:"));

            if (shipments != null && !shipments.isEmpty()) {

                Shipment sh = shipments.get(0);

                doc.add(new Paragraph("Vehicle: " +
                        (sh.getVehicle() != null ? sh.getVehicle().getVehicleNumber() : "N/A")));

                doc.add(new Paragraph("Driver: " +
                        (sh.getVehicle() != null ? sh.getVehicle().getDriverName() : "N/A")));

            } else {
                doc.add(new Paragraph("Vehicle: Not Assigned"));
                doc.add(new Paragraph("Driver: Not Assigned"));
            }

            doc.add(new Paragraph("\n--------------------------------------------------"));

            // ================= BARCODE =================
            doc.add(new Paragraph("📡 BARCODE:"));

            String barcodeFullPath = BARCODE_PATH + dc.getBarcodeValue() + ".png";

            // ✅ SAFE CHECK
            if (Files.exists(Paths.get(barcodeFullPath))) {

                ImageData data = ImageDataFactory.create(barcodeFullPath);
                Image img = new Image(data);
                img.setWidth(250);

                doc.add(img);

            } else {
                doc.add(new Paragraph("⚠ Barcode image not found"));
            }

            doc.add(new Paragraph(dc.getBarcodeValue()));

            doc.add(new Paragraph("--------------------------------------------------"));

            // ================= SIGNATURE =================
            doc.add(new Paragraph("\nSignature:\n"));
            doc.add(new Paragraph("Farmer ______"));
            doc.add(new Paragraph("Driver ______"));
            doc.add(new Paragraph("Warehouse ______"));

            doc.add(new Paragraph("--------------------------------------------------"));

            doc.close();

            System.out.println("✅ PDF Created: " + fullPath);

            return "/dc/" + fileName;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ PDF generation failed");
        }
    }
}