package rs.ac.bg.etf.jj203218m.rg2.dz1;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

public class Main implements GLEventListener, MouseListener
{
	private final GLWindow window;
	private final FPSAnimator animator;
	private static final int FPS = 60;
	private static final String TITLE = "Earth";
	private int width = 800, height = 600;
	private static final int DIVISION_COUNT = 32;
	private int lastX, lastY;
	private float fov = 45f;

	private Vector3f cameraFront = new Vector3f(0f, 0f, 1f);
	private final float cameraDistance = 4f;
	private Vector3f cameraPosition = cameraFront.mul(-1 * cameraDistance);
	private final Vector3f cameraTarget = new Vector3f(0f, 0f, 0f);
	private final Vector3f cameraUp = new Vector3f(0f, 1f, 0f);
	private float yaw = 90f, pitch = 0f;

	private Earth earth;
	private Skybox skybox;

	public Main()
	{
		GLProfile profile = GLProfile.getDefault();

		GLCapabilities capabilities = new GLCapabilities(profile);
		capabilities.setAlphaBits(8);
		capabilities.setDepthBits(24);
		capabilities.setDoubleBuffered(true);

		window = GLWindow.create(capabilities);

		animator = new FPSAnimator(window, FPS, false);

		window.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowDestroyNotify(WindowEvent e)
			{
				animator.stop();
				System.exit(0);
			}
		});
		window.addGLEventListener(this);
		window.addMouseListener(this);
		window.setSize(width, height);
		window.setTitle(TITLE);
		window.setResizable(false);
		window.setVisible(true);

		animator.start();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		yaw += e.getX() - lastX;
		pitch = Math.min(Math.max(pitch - (e.getY() - lastY), -90f), 90f);

		Vector3f direction = new Vector3f();
		direction.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
		direction.y = (float) Math.sin(Math.toRadians(pitch));
		direction.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
		cameraFront = direction.normalize();
		cameraPosition = cameraFront.mul(-1 * cameraDistance);

		lastX = e.getX();
		lastY = e.getY();
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{}

	@Override
	public void mouseExited(MouseEvent e)
	{}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		lastX = e.getX();
		lastY = e.getY();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{}

	@Override
	public void mouseReleased(MouseEvent e)
	{}

	@Override
	public void mouseWheelMoved(MouseEvent e)
	{
		fov = Math.min(Math.max(fov - e.getRotation()[1], 1f), 178f);
	}

	@Override
	public void display(GLAutoDrawable drawable)
	{
		GL4 gl = drawable.getGL().getGL4();

		gl.glClearColor(0f, 0f, 0f, 0f);
		gl.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);

		Matrix4f view = new Matrix4f();
		view.lookAt(cameraPosition, cameraTarget, cameraUp);

		Matrix4f projection = new Matrix4f();
		projection.perspective((float) Math.toRadians(fov), (float) width / height, 0.1f, 10f);

		Matrix4f projectionSky = new Matrix4f();
		projectionSky.perspective((float) Math.toRadians(60), (float) width / height, 0.1f, 100f);
		
		Vector3f lightPosition = new Vector3f(3f, 3f, -3f);

		earth.getShaderProgram().use(drawable);
		earth.activateAndBindTextures(drawable);

		earth.getShaderProgram().setMatrix4f(drawable, "view", view);
		earth.getShaderProgram().setMatrix4f(drawable, "projection", projection);
		earth.getShaderProgram().setVector3f(drawable, "lightPos", lightPosition);

		earth.display(drawable);

		skybox.getShaderProgram().use(drawable);
		skybox.activateAndBindTextures(drawable);

		skybox.getShaderProgram().setMatrix4f(drawable, "view", view);
		skybox.getShaderProgram().setMatrix4f(drawable, "projection", projectionSky);

		skybox.display(drawable);
	}

	@Override
	public void dispose(GLAutoDrawable drawable)
	{
		earth.dispose(drawable);
	}

	@Override
	public void init(GLAutoDrawable drawable)
	{
		GL4 gl = drawable.getGL().getGL4();

		gl.glEnable(GL4.GL_DEPTH_TEST);

		earth = new Earth(drawable, DIVISION_COUNT);
		earth.init(drawable);

		skybox = new Skybox(drawable);
		skybox.init(drawable);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
	{
		GL4 gl = drawable.getGL().getGL4();

		this.width = width;
		this.height = height;

		gl.glViewport(0, 0, width, height);
	}

	public static void main(String[] args)
	{
		new Main();
	}
}
