package org.example.starter.startup

import com.netgrif.application.engine.petrinet.domain.dataset.logic.action.ActionDelegate
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
    private final ActionDelegate actionDelegate

    StudyCaseRunner(ImportHelper helper, IPetriNetService netService, ActionDelegate actionDelegate) {
        this.helper = helper
        this.netService = netService
        this.actionDelegate = actionDelegate
        this.actionDelegate.outcomes = []
    }

    @Override
    void run(String... args) throws Exception {
        students()
        modules()
        studyProgrammes()
    }

    static final STUDENTS = [
            [
                    "first_name": "Maximilian",
                    "last_name" : "Müller",
                    "student_id": "3849102",
                    "email"     : "maximilian.mueller@studium.fernuni-hagen.de",
                    "phone"     : "+49 2331 9871001"
            ],
            [
                    "first_name": "Sophie",
                    "last_name" : "Schneider",
                    "student_id": "3849103",
                    "email"     : "sophie.schneider@studium.fernuni-hagen.de",
                    "phone"     : "+49 2331 9871002"
            ],
            [
                    "first_name": "Lukas",
                    "last_name" : "Fischer",
                    "student_id": "3849104",
                    "email"     : "lukas.fischer@studium.fernuni-hagen.de",
                    "phone"     : "+49 2331 9871003"
            ],
            [
                    "first_name": "Emma",
                    "last_name" : "Weber",
                    "student_id": "3849105",
                    "email"     : "emma.weber@studium.fernuni-hagen.de",
                    "phone"     : "+49 2331 9871004"
            ],
            [
                    "first_name": "Felix",
                    "last_name" : "Meyer",
                    "student_id": "3849106",
                    "email"     : "felix.meyer@studium.fernuni-hagen.de",
                    "phone"     : "+49 2331 9871005"
            ],
            [
                    "first_name": "Mia",
                    "last_name" : "Wagner",
                    "student_id": "3849107",
                    "email"     : "mia.wagner@studium.fernuni-hagen.de",
                    "phone"     : "+49 2331 9871006"
            ],
            [
                    "first_name": "Jonas",
                    "last_name" : "Becker",
                    "student_id": "3849108",
                    "email"     : "jonas.becker@studium.fernuni-hagen.de",
                    "phone"     : "+49 2331 9871007"
            ],
            [
                    "first_name": "Hannah",
                    "last_name" : "Hoffmann",
                    "student_id": "3849109",
                    "email"     : "hannah.hoffmann@studium.fernuni-hagen.de",
                    "phone"     : "+49 2331 9871008"
            ],
            [
                    "first_name": "Leon",
                    "last_name" : "Schäfer",
                    "student_id": "3849110",
                    "email"     : "leon.schaefer@studium.fernuni-hagen.de",
                    "phone"     : "+49 2331 9871009"
            ],
            [
                    "first_name": "Laura",
                    "last_name" : "Koch",
                    "student_id": "3849111",
                    "email"     : "laura.koch@studium.fernuni-hagen.de",
                    "phone"     : "+49 2331 9871010"
            ]
    ]

    void students() {
        def searchResult = netService.findByImportId("student")
        if (searchResult.empty) {
            throw new IllegalStateException("Could not find 'student' process")
        }
        def net = searchResult.get()
        STUDENTS.each { student ->
            def studentCase = helper.createCase("${student["first_name"]} ${student["last_name"]}" as String, net)
            def createTask = this.actionDelegate.assignTask("create", studentCase)
            this.actionDelegate.setData(createTask, [
                    "first_name": [
                            "value": student["first_name"],
                            "type" : "text"
                    ],
                    "last_name" : [
                            "value": student["last_name"],
                            "type" : "text"
                    ],
                    "student_id": [
                            "value": student["student_id"],
                            "type" : "text"
                    ],
                    "email"     : [
                            "value": student["email"],
                            "type" : "text"
                    ],
                    "phone"     : [
                            "value": student["phone"],
                            "type" : "text"
                    ]
            ])
            this.actionDelegate.finishTask(createTask)
        }
    }

    static final MODULES = [
            [
                    "number" : "31001",
                    "name"   : "Introduction to Economics",
                    "content": "A large proportion of actions, whether by individuals, companies, central banks or the state, often have an economic motivation, whether indirect or direct. Whether it's buying a new car, taking out a loan, or adjusting employee rights, there are always economic dimensions to consider. In the „Introduction to Economics“ you will learn to recognize and assess these economic dimensions. They will learn about the distinction between microeconomics and macroeconomics and will become familiar with basic economic working methods and theories. This will enable you to accompany public discourse on a new level and to gain a more informed understanding of many political and economic decisions.",
            ],
            [
                    "number" : "31011",
                    "name"   : "External accounting - accounting, annual financial statements, taxes",
                    "content": "The entire business accounting system includes all arithmetic units whose purpose is to record all money and power flows occurring in the business in terms of quantity and value over time.\n" +
                            "\n" +
                            "While internal accounting primarily serves self-information within the company, the primary objective of external accounting is to inform outsiders about important economic relationships within the company. Outsiders can, for example, B. Banks are those that grant loans to the company, shareholders or partners who want to know the company's financial situation, or the tax office, which needs a basis for calculating taxes. The information obtained in external accounting can also be used for internal company planning and control."
            ],
            [
                    "number" : "31021",
                    "name"   : "Investment and financing",
                    "content": "Assessing the benefits of investments is a central task of corporate planning and relevant for anyone who works in a responsible position in companies or is preparing for such an activity during their studies. In the units on „investment“, students are familiarized with the basic methods for evaluating investment projects. By conveying the theoretical foundations and practical application possibilities, they are enabled to critically reflect on the respective methods and make informed investment decisions. Furthermore, the module participants will be introduced to the basic approaches to dealing with uncertainty problems using decision-theoretic models,so that they can understand and apply the basic concept of Portefeuille theory. Reading the study letters enables them to communicate and collaborate with representatives of the relevant department."
            ],
            [
                    "number" : "31031",
                    "name"   : "Internal accounting and functional control",
                    "content": "The world of business administration is divided into many areas that initially appear independent, but which are all interwoven. Within the framework of the Internal Accounting and Functional Control module (IRufS, module 31031) you will gain an initial insight into three different subject areas – cost and performance accounting, production and logistics, and marketing. Each topic area gives you an initial feel for the content of the respective fields. This gives you the opportunity to set priorities for your further study planning according to your individual preferences."
            ],
            [
                    "number" : "31121",
                    "name"   : "Micro- and macroeconomics in business informatics",
                    "content": "Economics is sometimes divided into three „top disciplines“: microeconomics, macroeconomics, and econometrics. While econometrics provides the methodological tool for (now increasingly important) work with data, microeconomics deals with eingehender mit Entscheidungsproblemen von Individuen oder von individuellen Firmen, insbesondere in Marktsituationen. Somit bildet sie gewissermaßen das Fundament der ökonomischen Analyse, denn nur wenn individuelle Entscheidungsprobleme hinreichend gut verstanden sind, kann auch ein tieferes Verständnis gesamtwirtschaftlicher Zusammenhänge erreicht werden. In dem Teil „Mikroökonomik“ des Moduls 31111 betrachten wir Entscheidungsprobleme von Konsumenten, die letztlich zur Marktnachfrage nach einem Gut führen, sowie von Firmen, wodurch wir eine Beschreibung der Angebotsseite des Marktes erhalten. Im einfachsten Fall betrachten wir dabei sog. „preisnehmende“ Firmen. Das sind Firmen, die im vollständigen Wettbewerb miteinander stehen. Wir werden aufzeigen, dass in dem Marktgleichgewicht, das erreicht wird, wenn die Angebots- und die Nachfrageseite des Marktes zusammengeführt werden, Effizienz entsteht, wenn die Firmen sich als Preisnehmer verhalten. Ineffizienzen entstehen bspw. bei Monopolmacht von Firmen, oder bei Informationsasymmetrien zwischen Marktteilnehmern."
            ],
            [
                    "number" : "31071",
                    "name"   : "Introduction to Business Informatics",
                    "content": "Information technology (IT) plays a more or less significant role in almost every area of life today. In most companies, no process runs without IT support. In the private sector, smartphones and similar devices are used. indispensable. Combining professional and individual application scenarios with the corresponding IT solutions requires a discipline capable of bridging this gap. This discipline is business informatics. She deals with digitalization in business, administration and society."
            ],
            [
                    "number" : "31751",
                    "name"   : "Modeling of operational information systems",
                    "content": "Module 31751 Modeling of Business Information Systems deals with the conceptual, theoretical and methodological foundations of modeling business information systems and introduces basic features of data modeling, object-oriented modeling and business process modeling. A Moodle learning environment supports your learning process through online practice exercises, video exercises, and supplementary learning materials. In addition, virtual online presence exercises are offered."
            ],
            [
                    "number" : "31771",
                    "name"   : "Information management",
                    "content": "Information management aims to use information as a critical business resource in a targeted and economical way. It encompasses the planning, development, coordination and use of digital information technologies (IT) and information systems (IS). In times of digital transformation, information management is crucial not only for stable IT operations, but also for agility and business innovation. In larger companies and organizations, information management is typically handled by a or led by a Chief Information Officer (CIO) and supported by various IT roles. However, basic knowledge of information management is indispensable for employees in all company functions."
            ],
//            [
//                    "number": "31101",
//                    "name"  : "Grundlagen der Wirtschaftsmathematik und Statistik",
//                    "content": ""
//            ],
//            [
//                    "number": "61411",
//                    "name"  : "Algorithmische Mathematik",
//                    "content": ""
//            ],
//            [
//                    "number": "63017",
//                    "name"  : "Datenbanken und Sicherheit im Internet",
//                    "content": ""
//            ],
//            [
//                    "number": "64111",
//                    "name"  : "Betriebliche Informationssysteme",
//                    "content": ""
//            ],
//            [
//                    "number": "65001",
//                    "name"  : "Grundlagen der Informatik 1",
//                    "content": ""
//            ],
//            [
//                    "number": "65002",
//                    "name"  : "Grundlagen der Informatik 2",
//                    "content": ""
//            ],
//            [
//                    "number": "8930",
//                    "name"  : "Seminar",
//                    "content": ""
//            ],
//            [
//                    "number": "8995",
//                    "name"  : "Bachelorarbeit",
//                    "content": ""
//            ]
    ]
    List<String> moduleCaseIds = []

    void modules() {
        def searchResult = netService.findByImportId("module")
        if (searchResult.empty) {
            throw new IllegalStateException("Could not find 'module' process")
        }
        def net = searchResult.get()
        MODULES.each { module ->
            def moduleCase = helper.createCase("${module["number"]} ${module["name"]}" as String, net)
            def createTask = this.actionDelegate.assignTask("create", moduleCase)
            this.actionDelegate.setData(createTask, [
                    "number" : [
                            "value": module["number"],
                            "type" : "text"
                    ],
                    "name"   : [
                            "value": module["name"],
                            "type" : "text"
                    ],
                    "ects"   : [
                            "value": 10,
                            "type" : "number"
                    ],
                    "content": [
                            "value": module["content"],
                            "type" : "text"
                    ],
                    "owner"  : [
                            "value": TestUserRunner.TEACHERS.collect { it.stringId },
                            "type" : "userList"
                    ],
                    "website": [
                            "value": "https://www.fernuni-hagen.de/wirtschaftswissenschaft/studium/module/${module["number"]}.shtml",
                            "type" : "text"
                    ]
            ])
            this.actionDelegate.finishTask(createTask)
            moduleCaseIds << moduleCase.stringId
        }
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
            throw new IllegalStateException("Could not find 'study program' process")
        }
        def net = searchResult.get()
        STUDY_PROGRAMMES.each { program ->
            def studyProgramCase = helper.createCase(program["name"] as String, net)
            def createTask = this.actionDelegate.assignTask("create", studyProgramCase)
            this.actionDelegate.setData(createTask, [
                    "name"                   : [
                            "value": program["name"],
                            "type" : "text"
                    ],
                    "description"            : [
                            "value": program["desc"],
                            "type" : "text"
                    ],
                    "degree"                 : [
                            "value": program["deg"],
                            "type" : "enum"
                    ],
                    "ects"                   : [
                            "value": program["ects"],
                            "type" : "number"
                    ],
                    "module_list"            : [
                            "value": moduleCaseIds,
                            "type" : "caseRef"
                    ],
                    "module_list_multichoice": [
                            "value": moduleCaseIds,
                            "type" : "multichoice"
                    ]
            ])
            this.actionDelegate.finishTask(createTask)
        }
    }
}
