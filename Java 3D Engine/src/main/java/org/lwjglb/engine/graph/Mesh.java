package org.lwjglb.engine.graph;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import org.lwjgl.opengl.GL30;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import org.lwjgl.system.MemoryStack;

public class Mesh {

    private int numVertices;
    private int vaoId;
    private List<Integer> vboIdList;
    public static final int MAX_WEIGHTS = 4;
    private Vector3f aabbMax;
    private Vector3f aabbMin;


    public Mesh(float[] positions, float[] normals, float[] tangents, float[] bitangents, float[] textCoords, int[] indices) {
        this(positions, normals, tangents, bitangents, textCoords, indices,
                new int[Mesh.MAX_WEIGHTS * positions.length / 3], new float[Mesh.MAX_WEIGHTS * positions.length / 3],
                new Vector3f(), new Vector3f());
    }

    public Mesh(float[] positions, float[] normals, float[] tangents, float[] bitangents, float[] textCoords, int[] indices, int[] boneIndices, float[] weights, Vector3f aabbMin, Vector3f aabbMax) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // aabbMax = indices.length;
            this.aabbMax = aabbMax;
            this.aabbMin = aabbMin;
            numVertices = indices.length;
            vboIdList = new ArrayList<>();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);
            

            // Positions VBO
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            FloatBuffer positionsBuffer = stack.callocFloat(positions.length);
            positionsBuffer.put(0, positions);
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            //normals
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            FloatBuffer normalsBuffer = stack.callocFloat(normals.length);
            normalsBuffer.put(0, normals);
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

            // Tangents VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            FloatBuffer tangentsBuffer = stack.callocFloat(tangents.length);
            tangentsBuffer.put(0, tangents);
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, tangentsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(2);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
        
            // Bitangents VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            FloatBuffer bitangentsBuffer = stack.callocFloat(bitangents.length);
            bitangentsBuffer.put(0, bitangents);
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, bitangentsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(3);
            glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0);
            
            // Texture coordinates VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            FloatBuffer textCoordsBuffer = stack.callocFloat(textCoords.length);
            textCoordsBuffer.put(0, textCoords);
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(4);
            glVertexAttribPointer(4, 2, GL_FLOAT, false, 0, 0);
            
            // Bone weights
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            FloatBuffer weightsBuffer = stack.callocFloat(weights.length);
            weightsBuffer.put(weights).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, weightsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(5);
            glVertexAttribPointer(5, 4, GL_FLOAT, false, 0, 0);
        
            // Bone indices
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            IntBuffer boneIndicesBuffer = stack.callocInt(boneIndices.length);
            boneIndicesBuffer.put(boneIndices).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, boneIndicesBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(6);
            glVertexAttribPointer(6, 4, GL_FLOAT, false, 0, 0);
            
            // Index VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            IntBuffer indicesBuffer = stack.callocInt(indices.length);
            indicesBuffer.put(0, indices);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }
    }

    public void cleanup() {
        vboIdList.forEach(GL30::glDeleteBuffers);
        glDeleteVertexArrays(vaoId);
    }

    public int getNumVertices() {
        return numVertices;
    }


    public final int getVaoId() {
        return vaoId;
    }

    public Vector3f getAabbMax() {
        return aabbMax;
    }

    public Vector3f getAabbMin() {
        return aabbMin;
    }
}
