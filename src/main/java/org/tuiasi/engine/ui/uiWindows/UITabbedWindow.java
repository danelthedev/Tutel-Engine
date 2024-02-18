package org.tuiasi.engine.ui.uiWindows;

import imgui.ImGui;
import imgui.ImVec2;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tuiasi.engine.global.WindowVariables;
import org.tuiasi.engine.ui.components.IComponent;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class UITabbedWindow extends IUIWindow {

    private List<IUIWindow> tabWindows = new ArrayList<>();
    private int selectedTabIndex = 0;
    private List<IComponent> displayedComponents = new ArrayList<>();

    public UITabbedWindow(String windowTitle) {
        super(windowTitle);
    }

    public UITabbedWindow(String windowTitle, ImVec2 position, ImVec2 size){
        super(windowTitle, position, size);
    }

    public void addTab(UIWindow window) {
        tabWindows.add(window);
    }

    public void addTabs(List<IUIWindow> windows) {
        tabWindows.addAll(windows);
    }

    @Override
    public void render() {
        super.render();

        if(ImGui.beginTabBar(getWindowTitle()))
        {
            // iterate through the window titles
            for(int i = 0; i < tabWindows.size(); i++) {
                if (ImGui.beginTabItem(tabWindows.get(i).getWindowTitle())) {
                    displayedComponents = tabWindows.get(i).getComponents();
                    // iterate through the components of the window
                    for(int j = 0; j < tabWindows.get(i).getComponents().size(); j++) {
                        displayedComponents.get(j).render();
                    }

                    ImGui.endTabItem();
                }
            }

            ImGui.endTabBar();
        }

        ImGui.end();
    }

}