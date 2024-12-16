package org.lwjglb.game;

import org.joml.Vector2f;
import org.joml.Vector4f;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import org.lwjglb.engine.Engine;
import org.lwjglb.engine.IAppLogic;
import org.lwjglb.engine.IGuiInstance;
import org.lwjglb.engine.MouseInput;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.graph.Model;
import org.lwjglb.engine.graph.Render;
import org.lwjglb.engine.scene.Camera;
import org.lwjglb.engine.scene.Entity;
import org.lwjglb.engine.scene.ModelLoader;
import org.lwjglb.engine.scene.Scene;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCond;



public class Main implements IAppLogic, IGuiInstance {

    private Entity cubeEntity;
    private Entity rbEntity;
    private Vector4f displInc = new Vector4f();
    private float rotation;
    private static final float MOUSE_SENSITIVITY = 0.1f;
    private static final float MOVEMENT_SPEED = 0.005f;

    public static void main(String[] args) {
        Main main = new Main();
        Engine gameEng = new Engine("This isnt gonna work", new Window.WindowOptions(), main);
        gameEng.run();
    }

    @Override
    public boolean handleGuiInput(Scene scene, Window window) {
        ImGuiIO imGuiIO = ImGui.getIO();
        MouseInput mouseInput = window.getMouseInput();
        Vector2f mousePos = mouseInput.getCurrentPos();
        imGuiIO.addMousePosEvent(mousePos.x, mousePos.y);
        imGuiIO.addMouseButtonEvent(0, mouseInput.isLeftButtonPressed());
        imGuiIO.addMouseButtonEvent(1, mouseInput.isRightButtonPressed());

        return imGuiIO.getWantCaptureMouse() || imGuiIO.getWantCaptureKeyboard();
    }

    @Override
    public void drawGui(){
        ImGui.newFrame();
        ImGui.setNextWindowPos(0, 0, ImGuiCond.Always);
        ImGui.showDemoWindow();
        ImGui.endFrame();
        ImGui.render();    
    }

    @Override
    public void cleanup() {
        // Nothing to be done yet
    }

    @Override
    public void init(Window window, Scene scene, Render render) {
        glfwSetInputMode(window.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        //defines, loads and creates model specified (for loop through all files soon)
        Model cubeModel = ModelLoader.loadModel("cube-model", "chapter-02/resources/models/cube/cube.obj", scene.getTextureCache()); scene.addModel(cubeModel);
        Model rbModel = ModelLoader.loadModel("rb-model", "chapter-02/resources/models/rbmodel/rbmodel.obj", scene.getTextureCache()); scene.addModel(rbModel);

        cubeEntity = new Entity("cube-entity", cubeModel.getId());
        cubeEntity.setPosition(0, -1, -2);
        scene.addEntity(cubeEntity);

        rbEntity = new Entity("rb-model", rbModel.getId());
        rbEntity.setPosition(0, -1, -2);
        scene.addEntity(rbEntity);

        //scene.setGuiInstance(this);   uncomment when i do gui
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis, boolean inputConsumed) {
        if (inputConsumed) {
            return;
        }
        float move = diffTimeMillis * MOVEMENT_SPEED;
        Camera camera = scene.getCamera();
        if (window.isKeyPressed(GLFW_KEY_W)) {
            camera.CamForward(move);
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            camera.CamBackwards(move);
        }
        if (window.isKeyPressed(GLFW_KEY_A )|| window.isKeyPressed(GLFW_KEY_LEFT)) {
            camera.CamLeft(move);
        } else if (window.isKeyPressed(GLFW_KEY_D) || window.isKeyPressed(GLFW_KEY_RIGHT)) {
            camera.CamRight(move);
        }
        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            camera.CamUp(move);
        } else if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            camera.CamDown(move);
        }

        MouseInput mouseInput = window.getMouseInput();
        //if (mouseInput.isLeftButtonPressed()) {
            Vector2f displVec = mouseInput.getDisplVec();
            camera.addRotation((float) Math.toRadians(displVec.x * MOUSE_SENSITIVITY), (float) Math.toRadians(displVec.y * MOUSE_SENSITIVITY));
            //System.out.println(displVec.x + displVec.y);
        //}
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {

        cubeEntity.setRotation(1, 1, 1, (float) Math.toRadians(rotation));
        cubeEntity.updateModelMatrix();
    }

    public void input(Window window, Scene scene, long diffTimeMillis) {
        throw new UnsupportedOperationException("Unimplemented method 'input'");
    }

}
