package org.tuiasi.engine.global.nodes.physics;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PhysicsProperties {
    @Getter @Setter
    private static float gravity = -9.8f * 0.01f;
}
