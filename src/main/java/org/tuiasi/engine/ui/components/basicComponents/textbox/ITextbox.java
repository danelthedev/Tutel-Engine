package org.tuiasi.engine.ui.components.basicComponents.textbox;

import imgui.ImVec2;
import lombok.Getter;
import lombok.Setter;
import org.tuiasi.engine.ui.components.IComponent;

@Getter @Setter
public abstract class ITextbox extends IComponent {

    public abstract void setText(String text);

}
