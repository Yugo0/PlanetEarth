package rs.ac.bg.etf.jj203218m.rg2.dz1;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import org.joml.Vector3f;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;

public class Earth extends GraphicObject
{
	private float[] vertices;
	private int vertexOffset = 0;
	private int[] indices;
	private int indexOffset = 0;
	private HashMap<FloatTrio, Integer> map;
	private int entryNumber = 0;

	public Earth(GLAutoDrawable drawable, int divCount)
	{
		super(drawable, "shaders/earth_vertex.shader", "shaders/earth_fragment.shader");

		if (divCount % 2 != 0)
		{
			divCount++;
		}

		map = new HashMap<>();

		vertices = new float[((divCount + 1) * (divCount + 1) + 4 * divCount * divCount
				+ (divCount - 1) * (divCount - 1)) * 5];
		indices = new int[6 * 4 * divCount * divCount];

		drawSide(new Vector3f(-1f, 1f, -1f), new Vector3f(1f, 0f, 0f), new Vector3f(0f, 0f, 1f), 2f, divCount);
		drawSide(new Vector3f(1f, -1f, 1f), new Vector3f(-1f, 0f, 0f), new Vector3f(0f, 0f, -1f), 2f, divCount);
		drawSide(new Vector3f(-1f, -1f, -1f), new Vector3f(1f, 0f, 0f), new Vector3f(0f, 1f, 0f), 2f, divCount);
		drawSide(new Vector3f(1f, 1f, 1f), new Vector3f(-1f, 0f, 0f), new Vector3f(0f, -1f, 0f), 2f, divCount);
		drawSide(new Vector3f(-1f, -1f, 1f), new Vector3f(0f, 0f, -1f), new Vector3f(0f, 1f, 0f), 2f, divCount);
		drawSide(new Vector3f(1f, 1f, -1f), new Vector3f(0f, 0f, 1f), new Vector3f(0f, -1f, 0f), 2f, divCount);
	}

	@Override
	public void initialize(GLAutoDrawable drawable)
	{
		GL4 gl = drawable.getGL().getGL4();

		FloatBuffer vertexBuffer = Buffers.newDirectFloatBuffer(vertices, 0);
		gl.glBufferData(GL4.GL_ARRAY_BUFFER, vertices.length * Float.BYTES, vertexBuffer, GL4.GL_STATIC_DRAW);

		gl.glVertexAttribPointer(0, 3, GL4.GL_FLOAT, false, 5 * Float.BYTES, 0);
		gl.glEnableVertexAttribArray(0);

		gl.glVertexAttribPointer(1, 3, GL4.GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
		gl.glEnableVertexAttribArray(1);

		IntBuffer indexBuffer = Buffers.newDirectIntBuffer(indices, 0);
		gl.glBufferData(GL4.GL_ELEMENT_ARRAY_BUFFER, indices.length * Integer.BYTES, indexBuffer, GL4.GL_STATIC_DRAW);
	}

	@Override
	public void display(GLAutoDrawable drawable)
	{
		GL4 gl = drawable.getGL().getGL4();

		gl.glBindVertexArray(vaoId);
		gl.glDrawElements(GL4.GL_QUADS, indices.length, GL4.GL_UNSIGNED_INT, 0);
	}

	private void drawSide(Vector3f start, Vector3f dir1, Vector3f dir2, float size, int divCount)
	{
		Vector3f current = new Vector3f(start);
		float increment = size / divCount;

		for (int i = 0; i < divCount; i++)
		{
			for (int j = 0; j < divCount; j++)
			{
				Vector3f vec1 = new Vector3f(dir1).mulAdd(increment, current);
				Vector3f vec2 = new Vector3f(dir2).mulAdd(increment, vec1);
				Vector3f vec3 = new Vector3f(dir2).mulAdd(increment, current);

				updateArrays(current);
				updateArrays(vec1);
				updateArrays(vec2);
				updateArrays(vec3);

				current = new Vector3f(dir1).mulAdd(increment, current);
			}

			current = new Vector3f(dir2).mulAdd((i + 1) * increment, start);
		}
	}

	private void updateArrays(Vector3f vector)
	{
		FloatTrio trio = new FloatTrio(vector);

		if (!map.containsKey(trio))
		{
			map.put(trio, entryNumber);
			entryNumber++;

			vertices[vertexOffset] = vector.x;
			vertices[vertexOffset + 1] = vector.y;
			vertices[vertexOffset + 2] = vector.z;

			// TODO - texture coordinates

			vertexOffset += 5;
		}
		int temp = map.get(trio);
		indices[indexOffset++] = map.get(trio);
	}
}
