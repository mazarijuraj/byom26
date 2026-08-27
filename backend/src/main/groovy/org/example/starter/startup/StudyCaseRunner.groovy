package org.example.starter.startup

import com.netgrif.application.engine.petrinet.service.interfaces.IPetriNetService
import com.netgrif.application.engine.startup.AbstractOrderedCommandLineRunner
import com.netgrif.application.engine.startup.ImportHelper
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component

@Slf4j
@Component
class StudyCaseRunner extends AbstractOrderedCommandLineRunner {

    private final ImportHelper helper
    private final IPetriNetService netService

    StudyCaseRunner(ImportHelper helper, IPetriNetService netService) {
        this.helper = helper
        this.netService = netService
    }

    @Override
    void run(String... args) throws Exception {
        //modules
        studyProgrammes()
    }

    static final STUDY_PROGRAMMES = [
            [
                    "name": "Computer Science B.Sc.",
                    "desc": "The Computer Science B.Sc. degree program. imparts basic knowledge of computer science. They learn to understand technical contexts and to select and apply suitable scientific methods to solve typical problems in computer science. Particular emphasis is placed on imparting practically relevant knowledge and skills. In addition to computer science knowledge, the studies also provide key qualifications in teamwork and communication.",
                    "ects": 180,
                    "deg" : "bsc"
            ],
            [
                    "name": "Computer Science M.Sc.",
                    "desc": "In the Computer Science M.Sc. degree program Acquire in-depth knowledge of computer science and the ability to work independently with scientific methods and insights from computer science, to solve problems theoretically, and to implement the solutions in your professional environment. The topics offered are geared towards the needs and expectations of the job market.",
                    "ects": 120,
                    "deg" : "msc"
            ],
            [
                    "name": "Practical Computer Science M.Sc.",
                    "desc": "The M.Sc. degree program in Practical Computer Science. This leads to a further vocational qualification for graduates who have already completed their first course of study at a university. They acquire practically relevant expertise in computer science and the ability to work with scientific methods and insights from computer science, to solve problems theoretically and to implement the solutions in their professional environment.",
                    "ects": 90,
                    "deg" : "msc"
            ]
    ]

    void studyProgrammes() {
        def searchResult = netService.findByImportId("study_program")
        if (searchResult.empty) {
            throw new IllegalStateException("Could not find study program process")
        }
        def net = searchResult.get()
        STUDY_PROGRAMMES.each { program ->
            def studyProgramCase = helper.createCase(program["name"] as String, net)
        }
    }
}
