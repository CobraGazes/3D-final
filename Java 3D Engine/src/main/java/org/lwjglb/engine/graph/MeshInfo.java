package org.lwjglb.engine.graph;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;

public class MeshInfo {
    public String id;
    public Vector4f aabbMin;
    public Vector4f aabMax;
    private static List<MeshInfo> meshInfos = new ArrayList<>();

    public MeshInfo(String id, Vector4f aabbMin, Vector4f aabMax) {
        this.id = id;
        this.aabbMin = aabbMin;
        this.aabMax = aabMax;
    }

    public static void addMeshinfo (Vector4f aabMin, Vector4f aabMax, String ID){
        meshInfos.add(new MeshInfo(ID, aabMin, aabMax));
    }
    
    public static MeshInfo findMeshById(String id) {
        for (MeshInfo info : meshInfos) {
            if (info.id.equals(id)) {
                //System.out.print(info + "\n");
                return info;
            }
        }
        return null; // Mesh not found
    }

    @Override
    public String toString() {
    return "MeshInfo{" +
            "id='" + id + '\'' +
            ", aabbMin=" + aabbMin +
            ", aabMax=" + aabMax +
            '}';
    }

    
}

