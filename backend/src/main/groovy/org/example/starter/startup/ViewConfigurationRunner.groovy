package org.example.starter.startup

import com.netgrif.application.engine.menu.domain.MenuItemBody
import com.netgrif.application.engine.menu.domain.configurations.CaseViewBody
import com.netgrif.application.engine.menu.domain.dashboard.DashboardItemBody
import com.netgrif.application.engine.menu.domain.dashboard.DashboardManagementBody
import com.netgrif.application.engine.menu.domain.templates.CustomViewTemplate
import com.netgrif.application.engine.menu.domain.templates.TabbedCaseViewTemplate
import com.netgrif.application.engine.menu.service.MenuItemTemplateHolder
import com.netgrif.application.engine.menu.service.interfaces.DashboardItemService
import com.netgrif.application.engine.menu.service.interfaces.DashboardManagementService
import com.netgrif.application.engine.menu.service.interfaces.IMenuItemService
import com.netgrif.application.engine.petrinet.domain.I18nString
import com.netgrif.application.engine.petrinet.service.interfaces.IPetriNetService
import com.netgrif.application.engine.startup.AbstractOrderedCommandLineRunner
import com.netgrif.application.engine.workflow.domain.Case
import groovy.util.logging.Slf4j
import org.example.starter.CustomActionDelegate
import org.springframework.stereotype.Component

@Slf4j
@Component
class ViewConfigurationRunner extends AbstractOrderedCommandLineRunner {

    protected final IPetriNetService petriNetService
    protected final CustomActionDelegate actionDelegate
    protected final IMenuItemService menuItemService
    protected final DashboardManagementService dashboardManagementService
    protected final DashboardItemService dashboardItemService

    ViewConfigurationRunner(
            IPetriNetService petriNetService,
            CustomActionDelegate actionDelegate,
            DashboardManagementService dashboardManagementService,
            DashboardItemService dashboardItemService,
            IMenuItemService menuItemService
    ) {
        this.petriNetService = petriNetService
        this.actionDelegate = actionDelegate
        this.dashboardManagementService = dashboardManagementService
        this.dashboardItemService = dashboardItemService
        this.menuItemService = menuItemService
        this.allIdentifiers = petriNetService.getAll().collect { it.identifier }
        log.info("All nets: ${allIdentifiers}")
    }
    private List<String> allIdentifiers = []
    private Case personalFolder, admissionsFolder, studyFolder, scienceFolder, settingsFolder
    private List<Case> folders

    @Override
    void run(String... args) throws Exception {
        createFolders()
        createStudyViews(studyFolder.dataSet["nodePath"].value as String)
        createSettingsViews(settingsFolder.dataSet["nodePath"].value as String)
        configureDashboard()
    }

    void createFolders() {
        this.personalFolder = createFolder("Personal", "account_circle")
        this.admissionsFolder = createFolder("Admissions", "school")
        this.studyFolder = createFolder("Study", "book_2")
        this.scienceFolder = createFolder("Research", "science")
        this.settingsFolder = createFolder("Settings", "settings", [("global_admin:GLOBAL_ROLE"): new I18nString("Admin (\uD83C\uDF0D Global role)")])
        folders = [this.personalFolder, this.admissionsFolder, this.studyFolder, this.scienceFolder, this.settingsFolder]
    }

    Case createFolder(String name, String icon, Map<String, I18nString> permissions = [:]) {
        def folder = MenuItemTemplateHolder.get(CustomViewTemplate.IDENTIFIER, "/", new I18nString(name)).get()
        folder.menuIcon = icon
        folder.allowedRoles = permissions
        folder.customViewSelector = "emptyView"
        return menuItemService.createOrIgnoreMenuItem(folder)
    }

    void createStudyViews(String folderUri) {
        MenuItemBody studyProgramsItem = MenuItemTemplateHolder.get(
                TabbedCaseViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("Study Programs", [
                        "sk": "Študijné programy",
                        "de": "Studienprogramme"
                ])
        ).get()
        studyProgramsItem.menuIcon = "school"
        CaseViewBody studyProgramsView = studyProgramsItem.view as CaseViewBody
        studyProgramsView.filterBody.query = "cases: processIdentifier == 'study_program'"
        studyProgramsView.createCaseButtonTitle = new I18nString("Study Program", ["sk": "Študijný program", "de": "Studienprogramm"])
        studyProgramsView.showMoreMenu = true
        studyProgramsView.allAllowedNets = false
        studyProgramsView.allowedNets = ["study_program"]
        studyProgramsView.defaultHeaders = ["meta-title", "study_program-degree", "study_program-ects"]
        studyProgramsView.requireTitleInCreation = false
        menuItemService.createOrIgnoreMenuItem(studyProgramsItem)
    }

    void createSettingsViews(String folderUri) {
        MenuItemBody processesMenuItem = MenuItemTemplateHolder.get(
                CustomViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("Processes", [
                        "sk": "Procesy",
                        "de": "Prozesse",
                        "cz": "Procesy",
                ])
        ).get()
        processesMenuItem.menuIcon = "device_hub"
        processesMenuItem.autoSelect = true
        processesMenuItem.customViewSelector = "builder"
        menuItemService.createOrIgnoreMenuItem(processesMenuItem)

        MenuItemBody roleManagementMenuItem = MenuItemTemplateHolder.get(
                CustomViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("Role management", [
                        "sk": "Správa rolí",
                        "de": "Rollenmanagement",
                        "cz": "Správa rolí",
                ])
        ).get()
        roleManagementMenuItem.menuIcon = "psychology"
        roleManagementMenuItem.customViewSelector = "console"
        menuItemService.createOrIgnoreMenuItem(roleManagementMenuItem)

        MenuItemBody menuItemsMenuItem = MenuItemTemplateHolder.get(
                TabbedCaseViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("Menu items", [
                        "sk": "Položky menu",
                        "de": "Menüpunkte",
                        "cz": "Položky menu",
                ])
        ).get()
        menuItemsMenuItem.menuIcon = "menu_open"
        CaseViewBody menuItemsView = menuItemsMenuItem.view as CaseViewBody
        menuItemsView.filterBody.query = "cases: processIdentifier == \"menu_item\""
        menuItemsView.createCaseButtonIcon = "playlist_add"
        menuItemsView.createCaseButtonTitle = new I18nString("Create Menu Item", ["sk": "Vytvor položku menu", "de": "Menüpunkt erstellen"])
        menuItemsView.showMoreMenu = true
        menuItemsView.allAllowedNets = false
        menuItemsView.allowedNets = ["menu_item"]
        menuItemsView.defaultHeaders = ["meta-title", "menu_item-nodePath", "menu_item-menu_item_identifier", "menu_item-view_configuration_type"]
        menuItemsView.requireTitleInCreation = false
        menuItemService.createOrIgnoreMenuItem(menuItemsMenuItem)

        MenuItemBody dashboardMenuItem = MenuItemTemplateHolder.get(
                TabbedCaseViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("Dashboard", [
                        "sk": "Dashboard",
                        "de": "Dashboard",
                        "cz": "Dashboard",
                ])
        ).get()
        dashboardMenuItem.menuIcon = "dashboard"
        CaseViewBody dashboardView = dashboardMenuItem.view as CaseViewBody
        dashboardView.filterBody.query = "cases: processIdentifier in ('dashboard_item', 'dashboard_management')"
        dashboardView.createCaseButtonIcon = "dashboard_customize"
        dashboardView.createCaseButtonTitle = new I18nString("Create Dashboard Item", ["sk": "Vytvor položku dashboardu", "de": "Dashboard-Element erstellen"])
        dashboardView.showMoreMenu = true
        menuItemService.createOrIgnoreMenuItem(dashboardMenuItem)
    }

    void configureDashboard() {
        Case dashboard = dashboardManagementService.findDashboardManagement("main_dashboard")
        def dashboardConfig = new DashboardManagementBody("main_dashboard", new I18nString("BYOM 2026"))
        dashboardConfig.dashboardItems = [:]
        this.folders.each {folder ->
            Case dashboardItem = dashboardItemService.getOrCreate(toDashboardItem(folder))
            dashboardConfig.dashboardItems << [(dashboardItem.stringId): dashboardItem.getFieldValue("item_name")]
        }
        dashboardConfig.logo = "assets/netgrif_logo.svg"
        dashboardConfig.simpleDashboard = true
        Thread.sleep(1000)
        dashboardManagementService.updateDashboardManagement(dashboard, dashboardConfig)
    }

    DashboardItemBody toDashboardItem(Case folder) {
        def item = new DashboardItemBody(
                folder.getFieldValue("menu_item_identifier") as String,
                folder.getStringId(),
                folder.getFieldValue("menu_icon") as String,
                folder.getFieldValue("menu_name") as I18nString,
                true
        )
        item.fontColor = item.iconColor = "#0b3c6e"
        return item
    }
}
