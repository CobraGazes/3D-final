package org.lwjglb.engine.scene;

import java.util.HashMap;
import java.util.Map;

import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.Model;
import org.lwjglb.engine.graph.TextureCache;

public class Scene {

    private TextureCache textureCache;
    private Map<String, Mesh> meshMap;
    private Projection projection;
    private Map<String, Model> modelMap;
    private String ModelID;

    public Scene(int width, int height) {
        meshMap = new HashMap<>();
        modelMap = new HashMap<>();
        projection = new Projection(width, height);
        textureCache = new TextureCache();
    }


    public void addMesh(String meshId, Mesh mesh) {
        meshMap.put(meshId, mesh);
    }

    public void addEntity(Entity entity){
        ModelID = entity.returnModelID();
        Model model = modelMap.get(ModelID);
        if (model == null){
            throw new RuntimeException("cant find model "+ model);
        }
        model.getEntitiesList().add(entity);
    }

    public void addModel(Model model){
        modelMap.put(model.getId(), model);
    }

    public void cleanupMesh() {
        meshMap.values().forEach(Mesh::cleanup);
    }

    public void cleanupModel() {
        modelMap.values().forEach(Model::cleanup);
    }

    public Map<String, Model> returnModelMap() {
        return modelMap;
    }

    public Projection getProjection() {
        return projection;
    }

    public Map<String, Mesh> getMeshMap() {
        return meshMap;
    }

    public TextureCache getTextureCache(){
        return textureCache;
    }

    public void changesize(int width, int height){
        projection.updateMatrix(width, height);
    }
}