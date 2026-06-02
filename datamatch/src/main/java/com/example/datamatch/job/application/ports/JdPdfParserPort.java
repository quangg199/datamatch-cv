package com.example.datamatch.job.application.ports;

/**
 * Port để parse file PDF thành raw text cho JD module.
 * Tách riêng khỏi CV module's PdfParserPort để đảm bảo Module Independence.
 */
public interface JdPdfParserPort {
    String parse(String storagePath);
}
