package com.codealpha.hotel_management_system.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.codealpha.hotel_management_system.dto.response.ReservationResponse;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class PdfService {
    // Formats as: 12-05-2025
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DeviceRgb HEADER_COLOR = new DeviceRgb(23, 64, 120);
    private static final DeviceRgb ROW_COLOR = new DeviceRgb(240, 240, 240);
    public byte[] generateReservationPdf(ReservationResponse reservation) {
        log.debug("Generating PDF for reservation id: {}", reservation.getId());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);
        addHeader(document);
        addReservationInfo(document, reservation);
        addGuestDetails(document, reservation);
        addRoomDetails(document, reservation);
        addPriceSummary(document, reservation);
        addFooter(document);
        document.close();
        log.info("PDF generated successfully for reservation id: {}", reservation.getId());
        return outputStream.toByteArray();
    }

    // ── Private helper methods ────────────────────────────────────────────
    private void addHeader(Document document) {
        Paragraph title = new Paragraph("StayEase")
                .setFontSize(28)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(HEADER_COLOR);
        document.add(title);
        Paragraph subtitle = new Paragraph("Hotel Reservation Confirmation")
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY);
        document.add(subtitle);
        document.add(new Paragraph("\n"));
    }
    private void addReservationInfo(Document document, ReservationResponse reservation) {
        document.add(createSectionHeading("Reservation Details"));
        Table table = new Table(UnitValue.createPercentArray(new float[]{40, 60})).useAllAvailableWidth();
        addTableRow(table, "Reservation ID", "#" + reservation.getId(), false);
        addTableRow(table, "Status", reservation.getStatus().name(), true);
        addTableRow(table, "Booking Date", reservation.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), false);
        document.add(table);
        document.add(new Paragraph("\n"));
    }
    private void addGuestDetails(Document document, ReservationResponse reservation) {
        document.add(createSectionHeading("Guest Details"));
        Table table = new Table(UnitValue.createPercentArray(new float[]{40, 60})).useAllAvailableWidth();
        addTableRow(table, "Guest Name", reservation.getGuestName(), false);
        addTableRow(table, "Email", reservation.getGuestEmail(), true);
        document.add(table);
        document.add(new Paragraph("\n"));
    }
    // Adds hotel, room, and stay duration details
    private void addRoomDetails(Document document, ReservationResponse reservation) {
        document.add(createSectionHeading("Stay Details"));
        Table table = new Table(UnitValue.createPercentArray(new float[]{40, 60})).useAllAvailableWidth();
        addTableRow(table, "Hotel", reservation.getHotelName(), false);
        addTableRow(table, "City", reservation.getHotelCity(), true);
        addTableRow(table, "Room Number", reservation.getRoomNumber(), false);
        addTableRow(table, "Room Type", reservation.getRoomType(), true);
        addTableRow(table, "Check-in Date", reservation.getCheckInDate().format(DATE_FORMATTER), false);
        addTableRow(table, "Check-out Date", reservation.getCheckOutDate().format(DATE_FORMATTER), true);
        addTableRow(table, "Number of Nights", reservation.getNumberOfNights() + " night(s)", false);
        if (reservation.getSpecialRequests() != null && !reservation.getSpecialRequests().isEmpty()) {
            addTableRow(table, "Special Requests", reservation.getSpecialRequests(), true);
        }
        document.add(table);
        document.add(new Paragraph("\n"));
    }

    // Adds price breakdown at the bottom
    private void addPriceSummary(Document document, ReservationResponse reservation) {
        document.add(createSectionHeading("Price Summary"));
        Table table = new Table(UnitValue.createPercentArray(new float[]{40, 60})).useAllAvailableWidth();
        addTableRow(table, "Price Per Night", "NPR " + reservation.getPricePerNight(), false);
        addTableRow(table, "Number of Nights", reservation.getNumberOfNights() + " night(s)", true);
        Cell labelCell = new Cell().add(new Paragraph("Total Amount").setBold()).setBackgroundColor(HEADER_COLOR).setFontColor(ColorConstants.WHITE).setPadding(6);
        Cell valueCell = new Cell().add(new Paragraph("NPR " + reservation.getTotalPrice()).setBold()).setBackgroundColor(HEADER_COLOR).setFontColor(ColorConstants.WHITE).setPadding(6);
        table.addCell(labelCell);
        table.addCell(valueCell);
        document.add(table);
        document.add(new Paragraph("\n"));
    }

    private void addFooter(Document document) {
        document.add(new Paragraph("Thank you for choosing StayEase!").setFontSize(12).setBold().setTextAlignment(TextAlignment.CENTER).setFontColor(HEADER_COLOR));
        document.add(new Paragraph("For any queries please contact us at support@stayease.com").setFontSize(10).setTextAlignment(TextAlignment.CENTER).setFontColor(ColorConstants.GRAY));
    }
    private Paragraph createSectionHeading(String title) {
        return new Paragraph(title).setFontSize(13).setBold().setFontColor(HEADER_COLOR).setMarginBottom(4);
    }

    private void addTableRow(Table table, String label, String value, boolean alternate) {
        Cell labelCell = new Cell().add(new Paragraph(label).setBold()).setFontSize(10).setPadding(6).setBackgroundColor(alternate ? ROW_COLOR : ColorConstants.WHITE);
        Cell valueCell = new Cell().add(new Paragraph(value != null ? value : "-")).setFontSize(10).setPadding(6).setBackgroundColor(alternate ? ROW_COLOR : ColorConstants.WHITE);
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}
