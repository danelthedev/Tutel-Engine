package org.tuiasi.engine.ui;

import imgui.flag.ImGuiWindowFlags;
import lombok.Getter;
import lombok.Setter;
import org.tuiasi.engine.ui.components.basicComponents.button.ButtonWithTitle;
import org.tuiasi.engine.ui.components.basicComponents.checkbox.CheckboxWithTitle;
import org.tuiasi.engine.ui.components.basicComponents.searchbar.SearchbarWithHint;
import org.tuiasi.engine.ui.uiWindow.IUIWindow;
import org.tuiasi.engine.ui.uiWindow.UITabbedWindow;
import org.tuiasi.engine.ui.uiWindow.UIWindow;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class DefaultEngineEditorUI {

        List<IUIWindow> uiWindows;

        public DefaultEngineEditorUI() {
            UITabbedWindow multiTabWindow = new UITabbedWindow("Multi Tab Window");
            multiTabWindow.addFlag(ImGuiWindowFlags.AlwaysAutoResize);

            UIWindow window1 = new UIWindow("Window 1");
            window1.addComponent(new CheckboxWithTitle("Checkbox 1", true));

            UIWindow window2 = new UIWindow("Window 2");
            window2.addComponent(new ButtonWithTitle("Button 1"));

            UIWindow window3 = new UIWindow("Window 3");
            window3.addComponent(new SearchbarWithHint("Searchbar 1", "Hint 1"));

            List<IUIWindow> windows = List.of(window1, window2, window3);

            multiTabWindow.addTabs(windows);

            uiWindows = new ArrayList<>();
            uiWindows.add(multiTabWindow);

        }

        public void renderUI() {

            for (IUIWindow uiWindow : uiWindows) {
                uiWindow.render();
            }
        }
}
