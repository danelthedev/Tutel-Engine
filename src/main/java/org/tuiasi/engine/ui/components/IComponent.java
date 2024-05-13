package org.tuiasi.engine.ui.components;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class IComponent {
        float width, height;
        float x, y;
        float ratioX, ratioY;
        int flags;

        public void addFlag(int flag) {
                flags |= flag;
        }

        public void setSize(float width, float height){
                this.width = width;
                this.height = height;
        }

        public void setPosition(float x, float y){
                this.x = x;
                this.y = y;
        }

        public void setRatioedPosition(float x, float y){
                this.ratioX = x;
                this.ratioY = y;
        }

        public abstract void render();
        public abstract String getLabel();
        public abstract void setLabel(String label);
}
