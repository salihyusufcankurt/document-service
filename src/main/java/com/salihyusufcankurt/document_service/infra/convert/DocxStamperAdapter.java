package com.salihyusufcankurt.document_service.infra.convert;

import io.reflectoring.docxstamper.DocxStamper;
import io.reflectoring.docxstamper.DocxStamperConfiguration;
import org.springframework.context.expression.MapAccessor; // ✅ doğru paket
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import org.docx4j.model.datastorage.migration.VariablePrepare; // run-merge
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

@Component
public class DocxStamperAdapter {

    public byte[] fill(byte[] templateBytes, Map<String, Object> model) {
        try (var in = new ByteArrayInputStream(templateBytes);
             var out = new ByteArrayOutputStream()) {

            System.out.println("[STAMP] keys=" + model.keySet());
            System.out.println("[STAMP] issueDate=" + model.get("issueDate"));
            System.out.println("[STAMP] totalText=" + model.get("totalText"));
            System.out.println("[STAMP] customer=" + model.get("customer"));

            // 1) DOCX içindeki parçalanmış ${...} run'larını birleştir
            byte[] preparedTemplate = prepareTemplate(templateBytes);

            // 2) SpEL Map erişimi (dot-notation) için accessor ekle
            var conf = new DocxStamperConfiguration()
                    .setFailOnUnresolvedExpression(false)
                    .setEvaluationContextConfigurer((StandardEvaluationContext ctx) -> {
                        ctx.addPropertyAccessor(new MapAccessor());
                    });

            new DocxStamper<Map<String, Object>>(conf)
                    .stamp(new ByteArrayInputStream(preparedTemplate), model, out);

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Template fill failed", e);
        }
    }

    private byte[] prepareTemplate(byte[] templateBytes) {
        try (var bin = new ByteArrayInputStream(templateBytes);
             var bout = new ByteArrayOutputStream()) {
            WordprocessingMLPackage pkg = WordprocessingMLPackage.load(bin);
            VariablePrepare.prepare(pkg); // 🔧 run split fix
            pkg.save(bout);
            return bout.toByteArray();
        } catch (Exception e) {
            System.out.println("[STAMP][WARN] prepareTemplate failed, using original. Reason: " + e.getMessage());
            return templateBytes;
        }
    }
}
