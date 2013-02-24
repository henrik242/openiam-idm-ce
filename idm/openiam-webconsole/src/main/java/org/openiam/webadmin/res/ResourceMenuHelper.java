package org.openiam.webadmin.res;

import org.openiam.idm.srvc.menu.dto.Menu;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
/**
 * Adjusts the menu to show only relevant links based on the Resource Type
 */
public class ResourceMenuHelper {

    private static Map<String, List<String>> resourceMenuMap = new HashMap<String, List<String>>();

    public static void init() {

        if (resourceMenuMap.isEmpty()) {

            List<String> authRepo =  new LinkedList<String>();
            resourceMenuMap.put("AUTH_REPO",authRepo);

            List<String> msysRepo =  new LinkedList<String>();
            msysRepo.add("RESSUMMARY");
            msysRepo.add("RESPOLICYMAP");
            msysRepo.add("RESAPPROVER");
            msysRepo.add("RESRECONCILE");
            msysRepo.add("RESENTITLEMENT");

            resourceMenuMap.put("MANAGED_SYS",msysRepo);


            List<String> pubWorkflow =  new LinkedList<String>();
            pubWorkflow.add("RESSUMMARY");
            pubWorkflow.add("RESAPPROVER");

            resourceMenuMap.put("SYS_ACTION",pubWorkflow);


            List<String> appRep =  new LinkedList<String>();
            appRep.add("RESSUMMARY");
            appRep.add("RESAPPROVER");
            appRep.add("RESENTITLEMENT");

            resourceMenuMap.put("WEB-APP",appRep);

            List<String> privWorkflow =  new LinkedList<String>();
            privWorkflow.add("RESSUMMARY");
            privWorkflow.add("RESAPPROVER");

            resourceMenuMap.put("WORKFLOW",privWorkflow);

        }

    }

    public static List<Menu> resourceTypeMenu(String resourceType, List<Menu> fullMenu) {
      List<Menu> updateMenu = new LinkedList<Menu>();

       init();

        List<String> referenceMenu = resourceMenuMap.get(resourceType);

        if (fullMenu != null) {
            for (Menu m : fullMenu ) {
                for (String refMenu : referenceMenu) {
                    if (m.getId().getMenuId().equalsIgnoreCase(refMenu)) {

                        updateMenu.add(m);

                    }

                }

            }
        }

        return updateMenu;




    }


}

