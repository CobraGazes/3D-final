package org.lwjglb.game;

    
import org.lwjglb.engine.Engine;
import org.lwjglb.engine.IAppLogic;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.Render;
import org.lwjglb.engine.scene.Scene;

public class Main implements IAppLogic {


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
            -0.5f, 0.5f, -2.0f,
            -0.5f, -0.5f, -1.0f,
            0.5f, -0.5f, -1.0f,
            0.5f, 0.5f, -1.0f,
        };
        float[] colors = new float[]{
                0.5f, 0.0f, 0.0f,
                1.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };
        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2
        };
        Mesh mesh = new Mesh(positions, colors, indices);
        scene.addMesh("quad", mesh);
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis) {
        // Nothing to be done yet
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
        while (true) { 
            
        }
        // Nothing to be done yet
        //rand = new Random() becasue dad wants to
    }
}
