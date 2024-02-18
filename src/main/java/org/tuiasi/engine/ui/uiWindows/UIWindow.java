package org.tuiasi.engine.ui.uiWindows;

//import imgui.ImGui;
import imgui.internal.ImGui;
import imgui.ImVec2;
import imgui.internal.ImGuiDockNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tuiasi.engine.global.WindowVariables;
import org.tuiasi.engine.ui.components.IComponent;

@Getter @Setter
public class UIWindow extends IUIWindow{

    public UIWindow(String windowTitle) {
        super(windowTitle);
    }

    public UIWindow(String windowTitle, ImVec2 relativePosition, ImVec2 size){
        super(windowTitle, relativePosition, size);
    }

    @Override
    public void render() {
        super.render();


        ImGui.begin(getWindowTitle(), getFlags());

        for (IComponent component : getComponents()) {
            component.render();
        }

        ImGui.end();

    }

}
