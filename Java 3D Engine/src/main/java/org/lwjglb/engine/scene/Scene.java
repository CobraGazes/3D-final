package org.lwjglb.engine.scene;

import java.util.HashMap;
import java.util.Map;

import org.lwjglb.engine.IGuiInstance;
import org.lwjglb.engine.graph.Model;
import org.lwjglb.engine.graph.TextureCache;
import org.lwjglb.engine.scene.lights.SceneLights;

public class Scene {

    private IGuiInstance guiInstance;
    private SkyBox skyBox;
    private Camera camera;
    private Fog fog;
    private TextureCache textureCache;
    private Projection projection;
    private Map<String, Model> modelMap;
    private Entity selectedEntity;
    private SceneLights sceneLights;
    private String ModelID;

    public Scene(int width, int height) {
        modelMap = new HashMap<>();
        projection = new Projection(width, height);
        textureCache = new TextureCache();
        camera = new Camera();
        fog = new Fog();
    }

    public void setSceneLights(SceneLights sceneLights) {
        this.sceneLights = sceneLights;
    }

    public SceneLights getSceneLights() {
        return sceneLights;
    }

    public Camera getCamera() {
        return camera;
    }

    public Fog getFog() {
        return fog;
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

    public void cleanup() {
        modelMap.values().forEach(Model::cleanup);
    }

    public Map<String, Model> returnModelMap() {
        return modelMap;
    }

    public Projection getProjection() {
        return projection;
    }

    public TextureCache getTextureCache(){
        return textureCache;
    }

    public void changesize(int width, int height){
        projection.updateMatrix(width, height);
    }

    public void setGuiInstance(IGuiInstance guiInstance){
        this.guiInstance = guiInstance;
    }

    public IGuiInstance getGuiInstance() {
        return guiInstance;
    }

    public SkyBox getSkyBox() {
        return skyBox;
    }
    
    public void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }

    public void setFog(Fog fog) {
        this.fog = fog;
    }

    public Entity getSelectedEntity() {
        return selectedEntity;
    }
    
    public void setSelectedEntity(Entity selectedEntity) {
        this.selectedEntity = selectedEntity;
    }


}