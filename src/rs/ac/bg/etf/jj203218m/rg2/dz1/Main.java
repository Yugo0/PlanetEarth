package rs.ac.bg.etf.jj203218m.rg2.dz1;

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

	private Earth earth;

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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public void display(GLAutoDrawable drawable)
	{
		GL4 gl = drawable.getGL().getGL4();

		gl.glClearColor(0f, 0f, 0f, 0f);
		gl.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);
		
		earth.display(drawable);
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

		earth = new Earth(drawable, DIVISION_COUNT, "shaders/earth_vertex.shader", "shaders/earth_fragment.shader");
		earth.init(drawable);
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
