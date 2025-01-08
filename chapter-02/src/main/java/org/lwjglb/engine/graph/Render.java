package org.lwjglb.engine.graph;

import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.scene.Scene;

public class Render {

    private GuiRender guiRender;
    private SceneRender RenderScene;
    private SkyBoxRender skyBoxRender;

    public Render(Window window) {
        GL.createCapabilities();
        glEnable(GL_MULTISAMPLE);
        glEnable(GL_DEPTH_TEST);

        RenderScene = new SceneRender();
        guiRender = new GuiRender(window);
        skyBoxRender = new SkyBoxRender();

    }

    public void cleanup() {
        RenderScene.cleanup();
        guiRender.cleanup();
    }

    public void render(Window window, Scene scene) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, window.getWidth(), window.getHeight());
        skyBoxRender.render(scene);
        RenderScene.render(scene);
        guiRender.render(scene);
    }
    
    public void resize(int width, int height) {
        guiRender.resize(width, height);
    }
}