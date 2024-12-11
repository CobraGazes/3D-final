package org.lwjglb.engine.graph;

import org.lwjglb.engine.scene.Entity;
import java.util.*;

public class Model {
    private final String id;
    private List<Entity> entitiesList; //DIRECTLY FROM CHAPTER 06: As you can see, a model, by now, stores a list of Mesh instances and has a unique identifier. In addition to that, we store the list of game entities (modelled by the Entity class) that are associated to that model. If you are going to create a full engine, you may want to store those relationships in somewhere else (not in the model), however, for simplicity we will store those links in the Model class. This way, render process will be simpler.
    private List<Mesh> meshList;

    public List<Entity> getEntitiesList() {
        return entitiesList;
    }

    public String getId() {
        return id;
    }

    public List<Mesh> getMeshList() {
        return meshList;
    }

    public Model(String id, List<Mesh> meshList) {
        this.id = id;
        this.meshList = meshList;
        entitiesList = new ArrayList<>();
    }

    public void cleanup() {
        meshList.forEach(Mesh::cleanup);
    }

}
