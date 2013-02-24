package org.openiam.idm.srvc.menu.ws;

import org.openiam.base.ws.Response;
import org.openiam.idm.srvc.menu.dto.Menu;
import org.openiam.idm.srvc.menu.dto.Permission;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.ws.RoleListResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

/**
 * Interface of the <code>NavigatorDataService</code>.
 * <p>
 * The <code>NavigatorDataService</code> is used to provide content for a dynamic menuing system.
 * The menu options are typically generated based on the groups and roles that a user
 * belongs to.  The NavigatorDataService also supports the
 * notion of a guest mode as would typically be found in a public site to show menus that
 * are not linked to a group or role.</p>
 * <p>
 * <b>Multilanguage Support:</b><br>
 * Menu options also contain a language indicator, allowing you display the menu text in
 * an unlimited number of languages as well provide different responses to each option.
 * The interface to the Navigator contains 2 sets of methods - Those that accept a
 * language code and those which do not. In most cases, where a language code
 * is not a parameter, the default language code from the deployment descriptor is used.
 * </p>
 * <b>Examples:</b>Generate the top level menu for user that has logged in<br>
 * <pre>
 *
 * </pre>
 * <p/>
 * <b>Examples:</b>Generate a Sub Menu:<br>
 * To get a sub-menu call either of the methods below with a menu id that was
 * obtained from the list returned by getInitialMenuOptions or an earlier call
 * to getMenuGroup.
 * <pre>
 * nav.getMenuGroup(menuid);
 * or
 * nav.getMenuGroupByUser(menuid, user_id);
 * </pre>
 * <p/>
 * <b>Examples:</b>Iterate through a list of menu options:<br>
 * <pre>
 * list = nav.getMenuGroup(menuid);
 * it = list.listIterator();
 * while (it.hasNext()) {
 * menu = (Menu)it.next();
 * }
 * </pre>
 */
@WebService(targetNamespace = "urn:idm.openiam.org/srvc/menu/service", name = "NavigationDataService")
public interface NavigatorDataWebService {


    /**
     * Returns a list of valid menu options for the requested menu group without taking the user into
     * account.  This mode is typically used in public or anonymous mode where the user id is not known.
     * Language code defaults to the default language code in the deployment descriptor.
     *
     * @param menuGroupId The menu group for which you are trying to locate the options
     * @param languageCd  -The language in which the menu information is to be retrieved.  Defaults to 'en' if null
     * @return java.util.List
     */
    @WebMethod
    MenuListResponse menuGroup(
            @WebParam(name = "menuGroupId", targetNamespace = "")
            String menuGroupId,
            @WebParam(name = "langCd", targetNamespace = "")
            String langCd);


    /**
     * Returns a list of valid menu options for the requested menu group for the user. Takes into account a users role
     *
     * @param menuGroupId The menu group for which you are trying to locate the options
     * @param userId      The user cd for which you are trying to find valid menu options
     * @param languageCd  - The language for which the menus are to be selected. Defaults to "en" if null
     * @returns array A list containing valid menu options.
     */
    @WebMethod
    MenuListResponse menuGroupByUser(
            @WebParam(name = "menuGroupId", targetNamespace = "")
            String menuGroupId,
            @WebParam(name = "userId", targetNamespace = "")
            java.lang.String userId,
            @WebParam(name = "languageCd", targetNamespace = "")
            String languageCd);


    /**
     * Returns an array of all menus in a menuGroup. For menu items that a user is allowed to access,
     * based on their role, will have the "selected" flag set to true.
     *
     * @param menuGroupId
     * @param userId
     * @param languageCd
     * @return
     */
    @WebMethod
    MenuListResponse menuGroupSelectedByUser(
            @WebParam(name = "menuGroupId", targetNamespace = "")
            String menuGroupId,
            @WebParam(name = "userId", targetNamespace = "")
            java.lang.String userId,
            @WebParam(name = "languageCd", targetNamespace = "")
            String languageCd);


    /**
     * Returns a <code>String</code> of MenuIds for all the menus in the
     * parentMenuGroupId hierarchy. This includes menu options found several level deep in the
     * hierarchy. The String of Ids is comma separated.
     *
     * @param parentMenuGroupId
     * @param languageCd
     * @return String
     */
    @WebMethod
    Response getAllMenuOptionIDs(
            @WebParam(name = "parentMenuGroupId", targetNamespace = "")
            String parentMenuGroupId,
            @WebParam(name = "languageCd", targetNamespace = "")
            String languageCd);


    /**
     * Adds a new menu option
     *
     * @param data
     */
    @WebMethod
    public MenuResponse addMenu(
            @WebParam(name = "data", targetNamespace = "")
            Menu data);

    /**
     * Gets a Menu object for the menuId and languageCd passed in
     *
     * @param id         - menuId
     * @param languageCd
     * @return Menu
     */
    @WebMethod
    MenuResponse getMenu(@WebParam(name = "menuId", targetNamespace = "")
                         String menuId,
                         @WebParam(name = "languageCd", targetNamespace = "")
                         String languageCd);

    /**
     * Update an existing menu option
     *
     * @param data
     */
    @WebMethod
    public Response updateMenu(
            @WebParam(name = "data", targetNamespace = "")
            Menu data);

    /**
     * Removes a menu option.  The deleteChildren parameter is set to true, then the service will delete all
     * child menu options.
     *
     * @param menuId
     * @param deleteChildren
     */
    @WebMethod
    public Response removeMenu(
            @WebParam(name = "menuid", targetNamespace = "")
            String menuId,
            @WebParam(name = "deleteChildren", targetNamespace = "")
            boolean deleteChildren);



    /**
     * Adds the permission.
     *
     * @param menuId    the menu id
     * @param roleId    the role id
     * @param serviceId the service id
     * @return the permission
     */
    @WebMethod
    PermissionResponse addPermission(
            @WebParam(name = "menuId", targetNamespace = "")
            String menuId,
            @WebParam(name = "roleId", targetNamespace = "")
            String roleId,
            @WebParam(name = "serviceId", targetNamespace = "")
            String serviceId);

    /**
     * Updates permission.
     *
     * @param permission the permission
     * @return the permission
     */
    @WebMethod
    PermissionResponse updatePermission(
            @WebParam(name = "permission", targetNamespace = "")
            Permission permission);

    /**
     * Gets the permission.
     *
     * @param menuId    the menu id
     * @param roleId    the role id
     * @param serviceId the service id
     * @return the permission
     */
    @WebMethod
    PermissionResponse getPermission(
            @WebParam(name = "menuId", targetNamespace = "")
            String menuId,
            @WebParam(name = "roleId", targetNamespace = "")
            String roleId,
            @WebParam(name = "serviceId", targetNamespace = "")
            String serviceId);

    /**
     * Gets the all permissions.
     *
     * @return the all permissions
     */
    @WebMethod
    PermissionListResponse getAllPermissions();

    /**
     * Removes the permission.
     *
     * @param menuId    the menu id
     * @param roleId    the role id
     * @param serviceId the service id
     */
    @WebMethod
    void removePermission(
            @WebParam(name = "menuId", targetNamespace = "")
            String menuId,
            @WebParam(name = "roleId", targetNamespace = "")
            String roleId,
            @WebParam(name = "serviceId", targetNamespace = "")
            String serviceId);

    /**
     * Removes the all permissions.
     *
     * @return the int
     */
    @WebMethod
    int removeAllPermissions();

    /**
     * Gets the roles by menu.
     *
     * @param menuId the menu id
     * @return the roles by menu
     */
    @WebMethod
    RoleListResponse getRolesByMenu(
            @WebParam(name = "menuId", targetNamespace = "")
            String menuId);

    /**
     * Gets the menus by role.
     *
     * @param roleId    the role id
     * @param serviceId the service id
     * @return the menus by role
     */
    @WebMethod
    MenuListResponse getMenusByRole(
            @WebParam(name = "roleId", targetNamespace = "")
            String roleId,
            @WebParam(name = "serviceId", targetNamespace = "")
            String serviceId);

    /**
     * Finds menus by user.
     *
     * @param menuGroup the menu group
     * @param roleId    the role id
     * @param userId    the user id
     * @return the list
     */
    @WebMethod
    MenuListResponse getMenusByUser(
            @WebParam(name = "menuGroup", targetNamespace = "")
            String menuGroup,
            @WebParam(name = "roleId", targetNamespace = "")
            String roleId,
            @WebParam(name = "userId", targetNamespace = "")
            String userId);

    /**
     * Find a menu and its descendants and return as a flat List.
     *
     * @param menuId
     * @param languageCd
     * @return a flat list of menu objects
     */
    @WebMethod
    MenuListResponse getMenuFamily(@WebParam(name = "menuId", targetNamespace = "")
                                   String menuId,
                                   @WebParam(name = "languageCd", targetNamespace = "")
                                   String languageCd);

    /**
     * Find a menu and its descendants and return as nested List.
     *
     * @param menuId
     * @param languageCd
     * @return list of nested lists of menu objects
     */
    @WebMethod
    MenuListResponse getMenuTree(
            @WebParam(name = "menuId", targetNamespace = "")
            String menuId,
            @WebParam(name = "languageCd", targetNamespace = "")
            String languageCd);


}
