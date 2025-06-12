package dev.luiiscarlos.academ_iq_api.shared.mail;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailTemplateProcessor {

    private final TemplateEngine templateEngine;

    public String buildTemplate(String templateName, Map<String, Object> variables) {
        Context context = new Context();

        variables.forEach(context::setVariable);

        return templateEngine.process(templateName, context);
    }
}