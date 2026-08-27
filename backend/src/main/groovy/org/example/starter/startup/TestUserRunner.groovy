package org.example.starter.startup

import com.netgrif.application.engine.auth.domain.Authority
import com.netgrif.application.engine.auth.domain.IUser
import com.netgrif.application.engine.auth.domain.User
import com.netgrif.application.engine.auth.domain.UserState
import com.netgrif.application.engine.auth.service.interfaces.IAuthorityService
import com.netgrif.application.engine.auth.service.interfaces.IUserService
import com.netgrif.application.engine.petrinet.domain.roles.ProcessRole
import com.netgrif.application.engine.petrinet.service.interfaces.IProcessRoleService
import com.netgrif.application.engine.startup.AbstractOrderedCommandLineRunner
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import java.text.Normalizer

@Slf4j
@Component
class TestUserRunner extends AbstractOrderedCommandLineRunner {

    private final IAuthorityService authorityService
    private final IUserService userService
    private final IProcessRoleService processRoleService

    TestUserRunner(IAuthorityService authorityService, IUserService userService, IProcessRoleService processRoleService) {
        this.authorityService = authorityService
        this.userService = userService
        this.processRoleService = processRoleService
    }

    private final static USER_NAMES = [
            "Robin Bergenthum", "Peter Fettke", "Gabriel Juhás", "Jakub Kovář", "Robert Lorenz", "Wolfgang Reisig"
    ]

    @Override
    void run(String... args) throws Exception {
        Authority adminAuthority = authorityService.getOrCreate(Authority.admin)
        Authority systemAuthority = authorityService.getOrCreate(Authority.systemAdmin)
        Authority userAuthority = authorityService.getOrCreate(Authority.user)
        Set<ProcessRole> allRoles = processRoleService.findAll() as Set<ProcessRole>

        USER_NAMES.each {fullName ->
            String name = fullName.split(" ")[0]
            String surname = fullName.split(" ")[1]
            String email = name2email(fullName)
            IUser user = userService.findByEmail(email, false)
            if (user != null) {
                return
            }
            userService.saveNew(new User(
                    name: name,
                    surname: surname,
                    email: email,
                    password: "password",
                    state: UserState.ACTIVE,
                    authorities: [userAuthority, adminAuthority, systemAuthority] as Set<Authority>,
                    processRoles: allRoles))
        }
    }

    String name2email(String name) {
        return stripAccents(name).split(" ").join(".").toLowerCase()
    }

    String stripAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll('\\p{M}', '')
    }
}
