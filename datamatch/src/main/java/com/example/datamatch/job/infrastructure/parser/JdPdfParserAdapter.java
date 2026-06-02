package com.example.datamatch.job.infrastructure.parser;

import com.example.datamatch.job.application.ports.JdPdfParserPort;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class JdPdfParserAdapter implements JdPdfParserPort {

    @Override
    public String parse(String storagePath) {
        try (PDDocument document = Loader.loadPDF(new File(storagePath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JD PDF document at: " + storagePath, e);
        }
    }
}
