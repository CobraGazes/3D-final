package org.lwjglb.game;

    
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.joml.Vector4f;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import org.lwjglb.engine.Engine;
import org.lwjglb.engine.IAppLogic;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.Model;
import org.lwjglb.engine.graph.Render;
import org.lwjglb.engine.scene.Entity;
import org.lwjglb.engine.scene.Scene;



public class Main implements IAppLogic {

    private Entity cubeEntity;
    private Vector4f displInc = new Vector4f();
    private float rotation;

    public static void main(String[] args) {
        Main main = new Main();
        Engine gameEng = new Engine("This isnt gonna work", new Window.WindowOptions(), main);
        gameEng.start();
    }

    @Override
    public void cleanup() {
        // Nothing to be done yet
    }

    @Override
    public void init(Window window, Scene scene, Render render) {
        //define an array of floats that contain the coordinates of the vertices of a triangle
        float[] positions = new float[]{
            // VO
            -0.5f, 0.5f, 0.5f,
            // V1
            -0.5f, -0.5f, 0.5f,
            // V2
            0.5f, -0.5f, 0.5f,
            // V3
            0.5f, 0.5f, 0.5f,
            // V4
            -0.5f, 0.5f, -0.5f,
            // V5
            0.5f, 0.5f, -0.5f,
            // V6
            -0.5f, -0.5f, -0.5f,
            // V7
            0.5f, -0.5f, -0.5f,
        };
        float[] colors = new float[]{
            0.5f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.5f,
            0.0f, 0.5f, 0.5f,
            0.5f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.5f,
            0.0f, 0.5f, 0.5f,
        };
        int[] indices = new int[]{
            // Front face
            0, 1, 3, 3, 1, 2,
            // Top Face
            4, 0, 3, 5, 4, 3,
            // Right face
            3, 2, 7, 5, 3, 7,
            // Left face
            6, 1, 0, 6, 0, 4,
            // Bottom face
            2, 1, 6, 2, 6, 7,
            // Back face
            7, 6, 4, 7, 4, 5,
        };
        List<Mesh> meshList = new ArrayList<>();
        Mesh mesh = new Mesh(positions, colors, indices);
        meshList.add(mesh);
        String cubeModelId = "cube-model";
        Model model = new Model(cubeModelId, meshList);
        rotation = 56;
        scene.addModel(model);
        cubeEntity = new Entity("cube-entity", cubeModelId);
        cubeEntity.setPosition(0, 0, -2);
        cubeEntity.setRotation(1, 1, 1, (float) Math.toRadians(rotation));
        scene.addEntity(cubeEntity);
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis) {
        displInc.zero();
        if (window.isKeyPressed(GLFW_KEY_W)) {
            displInc.y = 1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            displInc.y = -1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            displInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            displInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            displInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            displInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_E)) {
            rotation -= 1.0;
            if (rotation > 360) {
                rotation = 0;
            }
        } else if (window.isKeyPressed(GLFW_KEY_Q)) {
            rotation += 1.0;
            if (rotation > 360) {
                rotation = 0;
            }
        }

        displInc.mul(diffTimeMillis / 1000.0f);

        Vector3f entityPos = cubeEntity.returnPosition();
        cubeEntity.setPosition(displInc.x + entityPos.x, displInc.y + entityPos.y, displInc.z + entityPos.z);
        cubeEntity.setScale(cubeEntity.returnScale() + displInc.w);
        cubeEntity.updateModelMatrix();
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {

        cubeEntity.setRotation(1, 1, 1, (float) Math.toRadians(rotation));
        cubeEntity.updateModelMatrix();

        // while (true) { LITTERALLY SUCH A STUPID REASON TO WHY IT WAS CRASHING BRUH
            
        // }
    }
}
