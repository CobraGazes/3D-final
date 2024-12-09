package org.lwjglb.engine.graph;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
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
    
    public int getNumVertices() {
        return numVertices;
    }

    public final int getVaoId() {
        return vaoId;
    }

    private int VaoHandle(){
        vaoId = glGenVertexArrays(); //gather an array of vertex "names"  
        glBindVertexArray(vaoId); // ^ bind it, more info > https://stackoverflow.com/questions/26552642/when-is-what-bound-to-a-vao
        return vaoId;
    }

    private FloatBuffer BufferFinish(int vboId, FloatBuffer positionsBuffer){
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        return positionsBuffer;
    }

    public Mesh(float[] positions, int numVertices) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            this.numVertices = numVertices;
            vboIdList = new ArrayList<>();

            VaoHandle();

            // create vbo (actual list of vertices used for rendering)
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            FloatBuffer positionsBuffer = stack.callocFloat(positions.length); //converting to use for C since opengl is C based.
            positionsBuffer.put(0, positions);
            //details here: https://blog.lwjgl.org/memory-management-in-lwjgl-3/.

            BufferFinish(vboId, positionsBuffer);
        }
    }

    public void cleanup() {
        vboIdList.forEach(GL30::glDeleteBuffers);
        glDeleteVertexArrays(vaoId);
    }
}