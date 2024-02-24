package org.tuiasi.engine.ui.components;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class IComponent {
        float width, height;
        int flags;

        public void addFlag(int flag) {
                flags |= flag;
        }

        public void setSize(float width, float height){
                this.width = width;
                this.height = height;
        }

        public abstract void render();
        public abstract String getLabel();
}
