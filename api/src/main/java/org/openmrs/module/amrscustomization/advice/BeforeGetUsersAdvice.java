/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.amrscustomization.advice;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.springframework.aop.MethodBeforeAdvice;

/**
 *
 * @author jkeiper
 */
public class BeforeGetUsersAdvice implements MethodBeforeAdvice {

        private Log log = LogFactory.getLog(this.getClass());

        /**
         * @see org.springframework.aop.MethodBeforeAdvice#before(java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
         */
        public void before(Method m, Object[] args, Object target) throws Throwable {

                if (log.isDebugEnabled()) {
                        log.debug("Calling user service advice from: " + m.getName());
                }

                if (m.getName().equals("getUsers")) {

                        if (args.length > 1) {
                                List<Role> roles = (List<Role>) args[1];

                                // find and add all child roles for requested roles
                                Set<Role> allRoles = new HashSet<Role>();
                                for (Role r : roles) {
                                        allRoles.add(r);
                                        allRoles.addAll(this.getDescendantRoles(r));
                                }
                         
                                // re-assign the new set of roles back to the argument
                                args[1] = new ArrayList<Role>(allRoles);
                        }
                }
        }
        
        /**
	 * gets descendant roles for the given role
	 */
	private Set<Role> getDescendantRoles(Role role) {
		List<Role> allRoles = Context.getUserService().getAllRoles();
		Set<Role> descendents = new HashSet<Role>();
		for (Role r : allRoles)
			if (r.getAllParentRoles().contains(role))
				descendents.add(r);
		return descendents;
	}
}
