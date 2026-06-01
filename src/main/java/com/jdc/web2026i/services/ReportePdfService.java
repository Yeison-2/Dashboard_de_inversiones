package com.jdc.web2026i.services;

import com.jdc.web2026i.entities.FuenteInversion;
import com.jdc.web2026i.entities.InversionEntity;
import com.jdc.web2026i.entities.ProyectoEntity;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportePdfService {

    private static final float MARGEN = 50f;
    private static final float ANCHO_PAGINA = PDRectangle.A4.getWidth();
    private static final float ALTO_PAGINA = PDRectangle.A4.getHeight();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] generarInforme(List<ProyectoEntity> proyectos) throws IOException {
        Map<String, List<ProyectoEntity>> porMunicipio = proyectos.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getMunicipio().getNombre(),
                        LinkedHashMap::new,
                        Collectors.toList()));

        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            for (Map.Entry<String, List<ProyectoEntity>> entry : porMunicipio.entrySet()) {
                generarSeccionMunicipio(document, entry.getKey(), entry.getValue());
            }

            if (proyectos.isEmpty()) {
                PDPage pagina = new PDPage(PDRectangle.A4);
                document.addPage(pagina);
                escribirTextoSimple(document, pagina, "No hay proyectos para mostrar.", 50, ALTO_PAGINA - 100);
            }

            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void generarSeccionMunicipio(PDDocument document,
                                         String municipio,
                                         List<ProyectoEntity> proyectos) throws IOException {

        Map<FuenteInversion, BigDecimal> totalesPorFuente = new EnumMap<>(FuenteInversion.class);
        for (FuenteInversion f : FuenteInversion.values()) totalesPorFuente.put(f, BigDecimal.ZERO);

        for (ProyectoEntity p : proyectos) {
            for (InversionEntity inv : p.getInversiones()) {
                totalesPorFuente.merge(inv.getFuenteInversion(), inv.getMonto(), BigDecimal::add);
            }
        }

        BigDecimal totalGeneral = totalesPorFuente.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        // ── Página 1: encabezado + resumen + tabla de proyectos ──
        PDPage pagina1 = new PDPage(PDRectangle.A4);
        document.addPage(pagina1);

        try (PDPageContentStream cs = new PDPageContentStream(document, pagina1)) {
            float y = ALTO_PAGINA - MARGEN;

            y = escribirTitulo(cs, "INFORME DE INVERSION POR MUNICIPIO", y, 16);
            y = escribirTitulo(cs, municipio.toUpperCase(), y - 4, 14);
            y -= 6;
            y = dibujarLineaHorizontal(cs, y);
            y -= 10;

            y = escribirSubtitulo(cs, "Resumen de Inversiones", y);
            y -= 4;
            y = escribirFila(cs, "Municipio:", formatMonto(totalesPorFuente.get(FuenteInversion.MUNICIPIO)), y);
            y = escribirFila(cs, "Departamento:", formatMonto(totalesPorFuente.get(FuenteInversion.DEPARTAMENTO)), y);
            y = escribirFila(cs, "Nacion:", formatMonto(totalesPorFuente.get(FuenteInversion.NACION)), y);
            y = escribirFila(cs, "Otro Aportante:", formatMonto(totalesPorFuente.get(FuenteInversion.OTRO_APORTANTE)), y);
            y -= 4;
            y = dibujarLineaHorizontal(cs, y);
            y = escribirFilaNegrita(cs, "TOTAL:", formatMonto(totalGeneral), y + 2);
            y -= 16;

            y = escribirSubtitulo(cs, "Proyectos (" + proyectos.size() + ")", y);
            y -= 4;
            y = dibujarEncabezadoTabla(cs, y,
                    new String[]{"Proyecto", "Estado", "Sectorial", "Responsable", "Fecha Reg."},
                    new float[]{200, 80, 90, 110, 72});

            for (ProyectoEntity p : proyectos) {
                if (y < 80) break;
                y = dibujarFilaTabla(cs, y,
                        new String[]{
                                truncar(p.getNombreProyecto(), 30),
                                p.getEstado().name(),
                                p.getSectorial().getNombre(),
                                truncar(p.getResponsable(), 18),
                                p.getFechaRegistro().format(FMT)
                        },
                        new float[]{200, 80, 90, 110, 72});
            }
        }

        // ── Página 2: detalle de inversiones por proyecto ──
        PDPage pagina2 = new PDPage(PDRectangle.A4);
        document.addPage(pagina2);

        try (PDPageContentStream cs = new PDPageContentStream(document, pagina2)) {
            float y = ALTO_PAGINA - MARGEN;
            y = escribirTitulo(cs, "DETALLE DE INVERSIONES - " + municipio.toUpperCase(), y, 13);
            y -= 6;
            y = dibujarLineaHorizontal(cs, y);
            y -= 8;

            for (ProyectoEntity p : proyectos) {
                if (p.getInversiones().isEmpty()) continue;
                if (y < 140) break;

                y = escribirSubtitulo(cs, p.getNombreProyecto(), y);
                y = dibujarEncabezadoTabla(cs, y,
                        new String[]{"Fecha", "Fuente", "Aportante", "Monto ($)"},
                        new float[]{80, 110, 130, 100});

                List<InversionEntity> invOrdenadas = p.getInversiones().stream()
                        .sorted(Comparator.comparing(InversionEntity::getFechaInversion))
                        .toList();

                for (InversionEntity inv : invOrdenadas) {
                    if (y < 80) break;
                    y = dibujarFilaTabla(cs, y,
                            new String[]{
                                    inv.getFechaInversion().format(FMT),
                                    inv.getFuenteInversion().name(),
                                    inv.getNombreAportante() != null ? truncar(inv.getNombreAportante(), 20) : "-",
                                    formatMonto(inv.getMonto())
                            },
                            new float[]{80, 110, 130, 100});
                }
                y -= 8;
            }
        }

        // ── Página 3: graficas ──
        PDPage pagina3 = new PDPage(PDRectangle.A4);
        document.addPage(pagina3);

        try (PDPageContentStream cs = new PDPageContentStream(document, pagina3)) {
            float y = ALTO_PAGINA - MARGEN;
            y = escribirTitulo(cs, "GRAFICAS - " + municipio.toUpperCase(), y, 13);
            y -= 6;
            y = dibujarLineaHorizontal(cs, y);
            y -= 15;

            BufferedImage torta = generarGraficaTorta(municipio, totalesPorFuente);
            PDImageXObject imgTorta = LosslessFactory.createFromImage(document, torta);
            float imgW = 360f, imgH = 200f;
            cs.drawImage(imgTorta, MARGEN, y - imgH, imgW, imgH);
            y -= (imgH + 20);

            BufferedImage barras = generarGraficaBarras(municipio, proyectos);
            PDImageXObject imgBarras = LosslessFactory.createFromImage(document, barras);
            cs.drawImage(imgBarras, MARGEN, y - imgH, imgW, imgH);

            escribirTextoSimple(cs, "Generado el: " + LocalDate.now().format(FMT), MARGEN, 40f, 9);
        }
    }

    private BufferedImage generarGraficaTorta(String municipio, Map<FuenteInversion, BigDecimal> totales) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<FuenteInversion, BigDecimal> e : totales.entrySet()) {
            if (e.getValue().compareTo(BigDecimal.ZERO) > 0) {
                dataset.setValue(e.getKey().name(), e.getValue().doubleValue());
            }
        }
        JFreeChart chart = ChartFactory.createPieChart(
                "Inversion por Fuente - " + municipio, dataset, true, false, false);
        chart.setBackgroundPaint(java.awt.Color.WHITE);
        return chart.createBufferedImage(600, 330);
    }

    private BufferedImage generarGraficaBarras(String municipio, List<ProyectoEntity> proyectos) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (ProyectoEntity p : proyectos) {
            BigDecimal total = p.getInversiones().stream()
                    .map(InversionEntity::getMonto)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (total.compareTo(BigDecimal.ZERO) > 0) {
                dataset.addValue(total.doubleValue(), "Inversion Total", truncar(p.getNombreProyecto(), 20));
            }
        }
        JFreeChart chart = ChartFactory.createBarChart(
                "Inversion Total por Proyecto - " + municipio,
                "Proyecto", "Monto ($)",
                dataset, PlotOrientation.VERTICAL, false, false, false);
        chart.setBackgroundPaint(java.awt.Color.WHITE);
        return chart.createBufferedImage(600, 330);
    }

    // ─── Helpers de dibujo ────────────────────────────────────────────────────────

    private float escribirTitulo(PDPageContentStream cs, String texto, float y, int tamano) throws IOException {
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, tamano);
        cs.newLineAtOffset(MARGEN, y);
        cs.showText(texto);
        cs.endText();
        return y - tamano - 4;
    }

    private float escribirSubtitulo(PDPageContentStream cs, String texto, float y) throws IOException {
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 11);
        cs.newLineAtOffset(MARGEN, y);
        cs.showText(texto);
        cs.endText();
        return y - 16;
    }

    private float escribirFila(PDPageContentStream cs, String etiqueta, String valor, float y) throws IOException {
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 10);
        cs.newLineAtOffset(MARGEN + 10, y);
        cs.showText(etiqueta);
        cs.setFont(PDType1Font.HELVETICA, 10);
        cs.newLineAtOffset(120, 0);
        cs.showText(valor);
        cs.endText();
        return y - 14;
    }

    private float escribirFilaNegrita(PDPageContentStream cs, String etiqueta, String valor, float y) throws IOException {
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 11);
        cs.newLineAtOffset(MARGEN + 10, y);
        cs.showText(etiqueta + "  " + valor);
        cs.endText();
        return y - 18;
    }

    private void escribirTextoSimple(PDDocument doc, PDPage pagina, String texto, float x, float y) throws IOException {
        try (PDPageContentStream cs = new PDPageContentStream(doc, pagina, PDPageContentStream.AppendMode.APPEND, true)) {
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 11);
            cs.newLineAtOffset(x, y);
            cs.showText(texto);
            cs.endText();
        }
    }

    private void escribirTextoSimple(PDPageContentStream cs, String texto, float x, float y, int size) throws IOException {
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA, size);
        cs.newLineAtOffset(x, y);
        cs.showText(texto);
        cs.endText();
    }

    private float dibujarLineaHorizontal(PDPageContentStream cs, float y) throws IOException {
        cs.setLineWidth(0.5f);
        cs.moveTo(MARGEN, y);
        cs.lineTo(ANCHO_PAGINA - MARGEN, y);
        cs.stroke();
        return y - 6;
    }

    private float dibujarEncabezadoTabla(PDPageContentStream cs, float y,
                                         String[] columnas, float[] anchos) throws IOException {
        // ── CORRECCIÓN: sumar float[] manualmente ──
        float anchoTotal = 0f;
        for (float a : anchos) anchoTotal += a;

        cs.setNonStrokingColor(0.85f, 0.85f, 0.85f);
        cs.addRect(MARGEN, y - 14, anchoTotal, 16);
        cs.fill();
        cs.setNonStrokingColor(0f, 0f, 0f);

        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 9);
        cs.newLineAtOffset(MARGEN + 3, y - 10);
        for (int i = 0; i < columnas.length; i++) {
            cs.showText(columnas[i]);
            if (i < columnas.length - 1) cs.newLineAtOffset(anchos[i], 0);
        }
        cs.endText();
        return y - 16;
    }

    private float dibujarFilaTabla(PDPageContentStream cs, float y,
                                   String[] valores, float[] anchos) throws IOException {
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA, 8);
        cs.newLineAtOffset(MARGEN + 3, y - 10);
        for (int i = 0; i < valores.length; i++) {
            cs.showText(valores[i] != null ? valores[i] : "");
            if (i < valores.length - 1) cs.newLineAtOffset(anchos[i], 0);
        }
        cs.endText();

        cs.setLineWidth(0.2f);
        cs.moveTo(MARGEN, y - 12);
        cs.lineTo(ANCHO_PAGINA - MARGEN, y - 12);
        cs.stroke();

        return y - 14;
    }

    private String formatMonto(BigDecimal monto) {
        if (monto == null) return "$ 0";
        return "$ " + String.format("%,.0f", monto);
    }

    private String truncar(String texto, int max) {
        if (texto == null) return "";
        return texto.length() > max ? texto.substring(0, max - 1) + "." : texto;
    }
}