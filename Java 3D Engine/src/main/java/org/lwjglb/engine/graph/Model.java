package org.lwjglb.engine.graph;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjglb.engine.scene.Entity;

public class Model {

    private List<Animation> animationList;
    private final String id;
    private List<Entity> entitiesList;
    private List<Material> materialList;
    public List<Animation> getAnimationList() {
        return animationList;
    }


    public Model(String id, List<Material> materialList, List<Animation> animationList) {
        entitiesList = new ArrayList<>();
        this.id = id;
        this.materialList = materialList;
        this.animationList = animationList;
    }

    public void cleanup() {
        materialList.forEach(Material::cleanup);
    }

    public List<Entity> getEntitiesList() {
        return entitiesList;
    }

    public String getId() {
        return id;
    }

    public List<Material> getMaterialList() {
        return materialList;
    }
    
    public record AnimatedFrame(Matrix4f[] boneMatrices) {
    }

    public record Animation(String name, double duration, List<AnimatedFrame> frames) {
    }

}