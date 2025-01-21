package org.lwjglb.engine.scene;

import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Matrix3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

public class PhysicsWorld {

    private DiscreteDynamicsWorld dynamicsWorld;
    private List<CollisionObject> collisionObjects;

    public PhysicsWorld() {
        // Initialize Bullet Physics
        DefaultCollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
        CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);
        dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, null, null, collisionConfiguration);
        collisionObjects = new ArrayList<>();

        // Set gravity
        dynamicsWorld.setGravity(new Vector3f(0, -10, 0));
    }

    public void addBox(Vector3f position, Vector3f dimensions) {
        CollisionShape boxShape = new BoxShape(dimensions);
        RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(0, null, boxShape, new Vector3f(0f, 0f, 0f));
        RigidBody body = new RigidBody(rbInfo);
        Transform transform = new Transform();

        body.setWorldTransform(transform);
        dynamicsWorld.addRigidBody(body);
        collisionObjects.add(body);
    }

    public void stepSimulation(float timeStep) {
        dynamicsWorld.stepSimulation(timeStep);
    }

    public List<CollisionObject> getCollisionObjects() {
        return collisionObjects;
    }
}