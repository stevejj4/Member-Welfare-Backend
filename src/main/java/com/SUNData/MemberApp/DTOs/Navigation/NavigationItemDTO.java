package com.SUNData.MemberApp.DTOs.Navigation;

/**
 * Data Transfer Object representing a single navigation item
 * returned from the backend to the frontend (e.g. sidebar menu).
 *
 * <p>
 * This DTO is used in the endpoint:
 * GET /api/v1/me/navigation
 * </p>
 *
 * <p>
 * Each navigation item defines:
 * - The display text shown in the UI
 * - The icon identifier used by the frontend
 * - The route/path for navigation
 * </p>
 */
public class NavigationItemDTO {

    /**
     * The display label shown in the sidebar or navigation menu.
     * Example: "Dashboard", "Members", "Reports"
     */
    private String title;

    /**
     * Icon identifier used by the frontend UI framework.
     * Example values: "Home", "Users", "Settings"
     *
     * The frontend maps this string to an actual icon component.
     */
    private String icon;

    /**
     * Frontend route path used for navigation.
     * Example: "/dashboard", "/members", "/reports"
     */
    private String route;

    /**
     * Default no-args constructor.
     *
     * Required by frameworks like Spring Boot and Jackson
     * for JSON serialization/deserialization.
     */
    public NavigationItemDTO() {
    }

    /**
     * All-arguments constructor for easy object creation.
     *
     * @param title  display text shown in UI
     * @param icon   icon identifier used by frontend
     * @param route  frontend navigation path
     */
    public NavigationItemDTO(String title, String icon, String route) {
        this.title = title;
        this.icon = icon;
        this.route = route;
    }

    /**
     * Gets the navigation item title.
     *
     * @return title of the navigation item
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the navigation item title.
     *
     * @param title display text to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the icon identifier.
     *
     * @return icon name used by frontend
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets the icon identifier.
     *
     * @param icon icon name to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * Gets the navigation route/path.
     *
     * @return frontend route path
     */
    public String getRoute() {
        return route;
    }

    /**
     * Sets the navigation route/path.
     *
     * @param route frontend route to set
     */
    public void setRoute(String route) {
        this.route = route;
    }
}