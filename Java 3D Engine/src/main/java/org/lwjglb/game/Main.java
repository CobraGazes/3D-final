package org.lwjglb.game;

import org.joml.Vector2f;
import org.joml.Vector3f;
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
import org.lwjgl.openal.AL11;
import org.lwjglb.engine.Engine;
import org.lwjglb.engine.IAppLogic;
import org.lwjglb.engine.IGuiInstance;
import org.lwjglb.engine.LightControls;
import org.lwjglb.engine.MouseInput;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.graph.Model;
import org.lwjglb.engine.graph.Render;
import org.lwjglb.engine.scene.AnimationData;
import org.lwjglb.engine.scene.Camera;
import org.lwjglb.engine.scene.Entity;
import org.lwjglb.engine.scene.Fog;
import org.lwjglb.engine.scene.ModelLoader;
import org.lwjglb.engine.scene.Scene;
import org.lwjglb.engine.scene.SkyBox;
import org.lwjglb.engine.scene.lights.AmbientLight;
import org.lwjglb.engine.scene.lights.DirLight;
import org.lwjglb.engine.scene.lights.SceneLights;
import org.lwjglb.engine.sound.SoundBuffer;
import org.lwjglb.engine.sound.SoundListener;
import org.lwjglb.engine.sound.SoundManager;
import org.lwjglb.engine.sound.SoundSource;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCond;



public class Main implements IAppLogic, IGuiInstance {

    private boolean decreasing = true; // Direction flag
    private SoundSource playerSoundSource;
    private SoundManager soundMgr;
    private LightControls lightControls;
    private Entity cubeEntity;
    private Entity rbEntity;
    private Vector4f displInc = new Vector4f();
    private float rotation;
    private static final float MOUSE_SENSITIVITY = 0.1f;
    private static final float MOVEMENT_SPEED = 0.005f;
    private static final int NUM_CHUNKS = 4;
    private AnimationData animationData;

    private float lightAngle;
    private Entity[][] terrainEntities;


    public static void main(String[] args) {
        Main main = new Main();
        Window.WindowOptions opts = new Window.WindowOptions();
        opts.antiAliasing = true;
        Engine gameEng = new Engine("I DID NOT HEAR BANANAS!!!!!!!", opts, main);
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
        soundMgr.cleanup();
    }

    private void initSounds(Vector3f position, Camera camera) {
        soundMgr = new SoundManager();
        soundMgr.setAttenuationModel(AL11.AL_EXPONENT_DISTANCE);
        soundMgr.setListener(new SoundListener(camera.getPosition()));

        SoundBuffer buffer = new SoundBuffer("resources/sounds/creak1.ogg");
        soundMgr.addSoundBuffer(buffer);
        playerSoundSource = new SoundSource(false, false);
        playerSoundSource.setPosition(position);
        playerSoundSource.setBuffer(buffer.getBufferId());
        //soundMgr.addSoundSource("CREAK", playerSoundSource);

        buffer = new SoundBuffer("resources/sounds/supernova.ogg");
        soundMgr.addSoundBuffer(buffer);
        SoundSource source = new SoundSource(true, true);
        source.setBuffer(buffer.getBufferId());
        soundMgr.addSoundSource("MUSIC", source);
        source.play();
    }


    @Override
    public void init(Window window, Scene scene, Render render) {
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        glfwSetInputMode(window.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);


        String terrainModelId = "terrain";
        Model terrainModel = ModelLoader.loadModel(terrainModelId, "resources/models/terrain/terrain.obj",
                scene.getTextureCache(), false);
        scene.addModel(terrainModel);
        Entity terrainEntity = new Entity("terrainEntity", terrainModelId);
        terrainEntity.setScale(100.0f);
        terrainEntity.updateModelMatrix();
        scene.addEntity(terrainEntity);

        String bobModelId = "bobModel";
        Model bobModel = ModelLoader.loadModel(bobModelId, "resources/models/bob/boblamp.md5mesh", scene.getTextureCache(), true);
        scene.addModel(bobModel);
        Entity bobEntity = new Entity("bobEntity", bobModelId);
        bobEntity.setPosition(3, 0, -2);
        bobEntity.setRotation(0, 80, 0, 6);
        bobEntity.setScale(0.05f);
        bobEntity.updateModelMatrix();
        animationData = new AnimationData(bobModel.getAnimationList().get(0));
        bobEntity.setAnimationData(animationData);
        scene.addEntity(bobEntity);


        // String rbModelID = "rbModel";
        // Model rbModel = ModelLoader.loadModel(rbModelID, "resources/models/room/room.obj", scene.getTextureCache(), false);
        // scene.addModel(rbModel);
        // rbEntity = new Entity("rb-model", rbModel.getId());
        // scene.addEntity(rbEntity);


        SceneLights sceneLights = new SceneLights();
        AmbientLight ambientLight = sceneLights.getAmbientLight();
        ambientLight.setIntensity(0.5f);
        ambientLight.setColor(0.3f, 0.3f, 0.3f);

        DirLight dirLight = sceneLights.getDirLight();
        dirLight.setPosition(0, 1, 0);
        dirLight.setIntensity(1.0f);
        scene.setSceneLights(sceneLights);

        SkyBox skyBox = new SkyBox("resources/models/skybox/skybox.obj", scene.getTextureCache());
        skyBox.getSkyBoxEntity().setScale(100);
        skyBox.getSkyBoxEntity().updateModelMatrix();
        scene.setSkyBox(skyBox);

        scene.setFog(new Fog(true, new Vector3f(0.5f, 0.5f, 0.5f), 0.02f));

        Camera camera = scene.getCamera();
        camera.setPosition(2.0f, 3.0f, 4.5f);
        //camera.addRotation((float) Math.toRadians(95.0f), (float) Math.toRadians(390.f));
        lightAngle = 45;
        initSounds(bobEntity.returnPosition(), camera);
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
        if (window.isKeyPressed(GLFW_KEY_A)) {
            camera.CamLeft(move);
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            camera.CamRight(move);
        }
        if (window.isKeyPressed(GLFW_KEY_SPACE)){
            camera.CamUp(move);
        } else if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)){
            camera.CamDown(move);
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            lightAngle -= 2.5f;
            if (lightAngle < -90) {
                lightAngle = 180;
            }
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            lightAngle += 2.5f;
            if (lightAngle > 90) {
                lightAngle = 90;
            }
        }

        soundMgr.updateListenerPosition(camera);

        MouseInput mouseInput = window.getMouseInput();
        //if (mouseInput.isRightButtonPressed()) {
            Vector2f displVec = mouseInput.getDisplVec();
            camera.addRotation((float) Math.toRadians(displVec.x * MOUSE_SENSITIVITY), (float) Math.toRadians(displVec.y * MOUSE_SENSITIVITY));
        //}

        SceneLights sceneLights = scene.getSceneLights();
        DirLight dirLight = sceneLights.getDirLight();
        double angRad = Math.toRadians(lightAngle);
        dirLight.getDirection().x = (float) Math.sin(angRad);
        dirLight.getDirection().y = (float) Math.cos(angRad);
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
        //I HAVE NUTTHHINGGGGGGG :((((
        //updateTerrain(scene);
        animationData.nextFrame();
        if (animationData.getCurrentFrameIdx() == 45) {
            playerSoundSource.play();
        }
    }

    public void input(Window window, Scene scene, long diffTimeMillis) {
        throw new UnsupportedOperationException("Unimplemented method 'input'");
    }

    
    public void updateTerrain(Scene scene) {
        int cellSize = 10;
        Camera camera = scene.getCamera();
        Vector3f cameraPos = camera.getPosition();
        int cellCol = (int) (cameraPos.x / cellSize);
        int cellRow = (int) (cameraPos.z / cellSize);

        int numRows = NUM_CHUNKS * 2 + 1;
        int numCols = numRows;
        int zOffset = -NUM_CHUNKS;
        float scale = cellSize / 2.0f;
        for (int j = 0; j < numRows; j++) {
            int xOffset = -NUM_CHUNKS;
            for (int i = 0; i < numCols; i++) {
                Entity entity = terrainEntities[j][i];
                entity.setScale(scale);
                entity.setPosition((cellCol + xOffset) * 2.0f, 0, (cellRow + zOffset) * 2.0f);
                entity.returnMatrix().identity().scale(scale).translate(entity.returnPosition());
                xOffset++;
            }
            zOffset++;
        }
    }

}