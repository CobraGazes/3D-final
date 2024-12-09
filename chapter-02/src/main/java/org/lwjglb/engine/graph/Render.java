package org.lwjglb.engine.graph;

import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.scene.Scene;

public class Render {

    private SceneRender RenderScene;

    public Render() {
        GL.createCapabilities();
        RenderScene = new SceneRender();
    }

    public void cleanup() {
        RenderScene.cleanup();
    }

    public void render(Window window, Scene scene) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0.33f, 0.07f, 0.45f, 0.0f);
        glViewport(0, 0, window.getWidth(), window.getHeight());
        RenderScene.render(scene);
    }
}