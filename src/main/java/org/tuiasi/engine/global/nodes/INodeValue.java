package org.tuiasi.engine.global.nodes;

public interface INodeValue {

    Object saveState();
    void loadState(Object state);
}
