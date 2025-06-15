package dev.luiiscarlos.academ_iq_api.shared.utils;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TemplateProcessor {

    private final TemplateEngine templateEngine;

    public String buildTemplate(String templateName, Map<String, Object> vars) {
        Context context = new Context();

        vars.forEach(context::setVariable);

        return templateEngine.process(templateName, context);
    }
}