/* 
 * JBoss, Home of Professional Open Source 
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved. 
 * See the copyright.txt in the distribution for a 
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use, 
 * modify, copy, or redistribute it subject to the terms and conditions 
 * of the GNU Lesser General Public License, v. 2.1. 
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details. 
 * You should have received a copy of the GNU Lesser General Public License, 
 * v.2.1 along with this distribution; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.switchyard.as7.extension.admin;

import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;
import org.switchyard.admin.Component;
import org.switchyard.admin.SwitchYard;
import org.switchyard.as7.extension.services.SwitchYardAdminService;

/**
 * SwitchYardSubsystemListComponents
 * 
 * Operation returning the components registered with the SwitchYard subsystem.
 * 
 * @author Rob Cernich
 */
public final class SwitchYardSubsystemListComponents implements OperationStepHandler {

    /**
     * The global instance for this operation.
     */
    public static final SwitchYardSubsystemListComponents INSTANCE = new SwitchYardSubsystemListComponents();

    private SwitchYardSubsystemListComponents() {
        // forbidden inheritance
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jboss.as.controller.OperationStepHandler#execute(org.jboss.as.controller
     * .OperationContext, org.jboss.dmr.ModelNode)
     */
    @Override
    public void execute(final OperationContext context, final ModelNode operation) throws OperationFailedException {

        context.addStep(new OperationStepHandler() {
            @Override
            public void execute(final OperationContext context, final ModelNode operation)
                    throws OperationFailedException {
                final ModelNode components = context.getResult();
                final ServiceController<?> controller = context.getServiceRegistry(false).getRequiredService(
                        SwitchYardAdminService.SERVICE_NAME);

                SwitchYard switchYard = SwitchYard.class.cast(controller.getService().getValue());
                for (Component component : switchYard.getComponents()) {
                    components.add(component.getName());
                }
                context.completeStep();
            }
        }, OperationContext.Stage.RUNTIME);
        context.completeStep();
    }
}
