package com.SUNData.MemberApp.DTOs.Navigation;

public class NavigationItemDTO {

    private String title;
    private String icon;
    private String route;

    public NavigationItemDTO() {
    }

    public NavigationItemDTO(String title, String icon, String route) {
        this.title = title;
        this.icon = icon;
        this.route = route;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
