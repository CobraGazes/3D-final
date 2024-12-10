package org.lwjglb.engine.graph;

import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import org.lwjgl.system.MemoryStack;

public class UniformsMap {
    private int ID;
    private Map<String, Integer> uniforms;

    public UniformsMap(int ID) {
        this.ID = ID;
        uniforms = new HashMap<>();
    }

    public void makeUniform(String uniformName) {
        int uniformLocation = glGetUniformLocation(ID, uniformName);
        if (uniformLocation < 0) {
            throw new RuntimeException("Could not find uniform [" + uniformName + "] in shader program [" +
                    ID + "]");
        }
        uniforms.put(uniformName, uniformLocation);
    }
        public void setUniform(String uniformName, Matrix4f value) {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                Integer location = uniforms.get(uniformName);
                if (location == null) {
                    throw new RuntimeException("Could not find uniform [" + uniformName + "]");
                }
                glUniformMatrix4fv(location.intValue(), false, value.get(stack.mallocFloat(16)));
            }
    }
}