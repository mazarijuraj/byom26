//file:noinspection GrMethodMayBeStatic
package org.example.starter

import com.netgrif.application.engine.petrinet.domain.dataset.logic.action.ActionDelegate
import org.springframework.stereotype.Component

@Component
class CustomActionDelegate extends ActionDelegate {

    String pfql(Object input) {
        if (input instanceof Collection<String>) {
            return "(${input.collect {"'${it}'"}.join(",")})"
        }
        throw new UnsupportedOperationException("Unsupported class ${input.class}")
    }
}