package org.example.starter.startup

import com.netgrif.application.engine.startup.AbstractOrderedCommandLineRunner
import com.netgrif.application.engine.startup.ImportHelper
import groovy.util.logging.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Slf4j
@Component
class CustomRunner extends AbstractOrderedCommandLineRunner {

    private final ImportHelper importHelper

    CustomRunner(ImportHelper importHelper) {
        this.importHelper = importHelper
    }

    private static final PROCESS_IDENTIFIERS = [
            "settings",
            "application",
            "academic_achievement",
            "review",
            "module",
            "module_application",
            "study_program"
    ]

    @Override
    void run(String... args) throws Exception {
        PROCESS_IDENTIFIERS.each {id ->
            importHelper.upsertNet("${id}.xml", id)
        }
    }
}
