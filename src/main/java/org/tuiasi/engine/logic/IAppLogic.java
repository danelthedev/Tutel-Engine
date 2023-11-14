package org.tuiasi.engine.logic;

import org.tuiasi.engine.window.Window;

public interface IAppLogic {

    void init() throws Exception;

    void input(Window window);

    void update(float delta);

    void render(Window window);
}
