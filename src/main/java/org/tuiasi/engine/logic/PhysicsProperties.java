package org.tuiasi.engine.logic;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// TODO: Figure out how to implement physics and parent - child relation for spatial stuff

@Data
public class PhysicsProperties {
    @Getter @Setter
    private static float gravity = -9.8f;
}
