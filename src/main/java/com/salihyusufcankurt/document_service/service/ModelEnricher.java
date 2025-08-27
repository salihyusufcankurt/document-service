package com.salihyusufcankurt.document_service.service;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class ModelEnricher {

    private static final DateTimeFormatter DATE_TR = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    /**
     * Req’ten gelen data Map’ini alır; şablonda işe yarayacak ek string alanlar üretir:
     * - itemsText : satırları hizalı, çok satırlı metin
     * - totalText : “15.500 TL” gibi formatlı toplam
     * - issueDate : “dd.MM.yyyy” formatında bugünün tarihi
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> enrich(Map<String, Object> data) {
        Map<String, Object> model = new HashMap<>(data == null ? Map.of() : data);

        // --- itemsText (listeyi hizalı metne çevir) ---
        List<Map<String, Object>> items =
                (List<Map<String, Object>>) model.getOrDefault("items", List.of());

        StringBuilder sb = new StringBuilder();
        for (Map<String, Object> it : items) {
            String desc = String.valueOf(it.getOrDefault("desc", ""));
            int qty     = parseInt(it.get("qty"), 0);
            long price  = parseLong(it.get("price"), 0);

            sb.append(String.format("%-20s %3d %10s%n", desc, qty, fmtTRY(price)));
        }
        model.put("itemsText", sb.toString().trim());

        // --- totalText ---
        long total = parseLong(model.get("total"), 0);
        model.put("totalText", fmtTRY(total));

        // --- issueDate (opsiyonel) ---
        model.putIfAbsent("issueDate", LocalDate.now().format(DATE_TR));

        Map<String,Object> snapshot = new HashMap<>(model);
        System.out.println("[ENRICH] " + snapshot);

        return model;
    }

    private static String fmtTRY(long amount) {
        // 15500 -> "15.500 TL"
        return String.format("%,d", amount).replace(',', '.') + " TL";
    }

    private static int parseInt(Object v, int def) {
        try { return Integer.parseInt(String.valueOf(v)); } catch (Exception e) { return def; }
    }

    private static long parseLong(Object v, long def) {
        try { return Long.parseLong(String.valueOf(v)); } catch (Exception e) { return def; }
    }
}
