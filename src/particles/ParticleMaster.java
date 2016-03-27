package particles;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import renderEngine.Loader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by colt on 3/27/16.
 */

public class ParticleMaster {

    private static List<Particle> particles = new ArrayList<>();
    private static ParticleRenderer renderer;

    public static void init(Loader loader, Matrix4f projectionMatrix) {
        renderer = new ParticleRenderer(loader, projectionMatrix);
    }

    public static void update() {
        //Iterator is used to remove dead particles from the list.
        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle p = iterator.next();
            boolean stillAlive = p.update();
            if (!stillAlive)
                iterator.remove();
        }
    }

    public static void renderParticles(Camera camera) {
        renderer.render(particles, camera);
    }

    public static void addParticle(Particle particle) {
        particles.add(particle);
    }

    public static void cleanUp() {
        renderer.cleanUp();
    }

}