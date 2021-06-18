package rs.ac.bg.etf.jj203218m.rg2.dz1;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;

public class Earth extends GraphicObject
{
	float[] vertices = { -1f, -1f, -1f, 1f, -1f, -1f, 1f, 1f, -1f, -1f, 1f, -1f, -1f, -1f, 1f, 1f, -1f, 1f, 1f, 1f, 1f,
			-1f, 1f, 1f };
	int[] indices = { 0, 1, 2, 3, 7, 6, 5, 4 };

	public Earth(GLAutoDrawable drawable, int divisionCount)
	{
		super(drawable, "shaders/earth_vertex.shader", "shaders/earth_fragment.shader");
		// TODO
	}

	@Override
	public void initialize(GLAutoDrawable drawable)
	{
		GL4 gl = drawable.getGL().getGL4();

		FloatBuffer vertexBuffer = Buffers.newDirectFloatBuffer(vertices, 0);
		gl.glBufferData(GL4.GL_ARRAY_BUFFER, vertices.length * Float.BYTES, vertexBuffer, GL4.GL_STATIC_DRAW);

		gl.glVertexAttribPointer(0, 3, GL4.GL_FLOAT, false, 3 * Float.BYTES, 0);
		gl.glEnableVertexAttribArray(0);

		IntBuffer indexBuffer = Buffers.newDirectIntBuffer(indices, 0);
		gl.glBufferData(GL4.GL_ELEMENT_ARRAY_BUFFER, indices.length * Integer.BYTES, indexBuffer, GL4.GL_STATIC_DRAW);
	}

	@Override
	public void display(GLAutoDrawable drawable)
	{
		GL4 gl = drawable.getGL().getGL4();

		gl.glBindVertexArray(vaoId);
		gl.glDrawElements(GL4.GL_QUADS, 8, GL4.GL_UNSIGNED_INT, 0);
	}

}
