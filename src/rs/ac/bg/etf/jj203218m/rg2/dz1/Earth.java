package rs.ac.bg.etf.jj203218m.rg2.dz1;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.joml.Vector3f;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class Earth extends GraphicObject
{
	private float[] vertices;
	private int[] indices;
	private HashMap<FloatKey, Integer> map;
	private int curretIndex = 0;
	private List<Float> vertexList;
	private List<Integer> indexList;
	private BufferedImage heightMap;
	private final float heightStep = 8848.86f / 12742000 / 255;

	private enum TexCoordOption
	{
		STANDARD, END
	}

	public Earth(GLAutoDrawable drawable, int divCount)
	{
		super(drawable, "shaders/earth_vertex.shader", "shaders/earth_fragment.shader");

		if (divCount % 2 != 0)
		{
			divCount++;
		}

		map = new HashMap<>();

		vertexList = new LinkedList<>();
		indexList = new LinkedList<>();

		try
		{
			heightMap = ImageIO.read(new File("img/heightMap.jpg"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		drawSide(new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -1f), new Vector3f(-1f, 0f, 0f), 2f, divCount);
		drawSide(new Vector3f(-1f, -1f, 1f), new Vector3f(0f, 0f, -1f), new Vector3f(1f, 0f, 0f), 2f, divCount);
		drawSide(new Vector3f(-1f, -1f, -1f), new Vector3f(1f, 0f, 0f), new Vector3f(0f, 1f, 0f), 2f, divCount);
		drawSide(new Vector3f(1f, 1f, 1f), new Vector3f(-1f, 0f, 0f), new Vector3f(0f, -1f, 0f), 2f, divCount);
		drawSide(new Vector3f(-1f, -1f, 1f), new Vector3f(0f, 0f, -1f), new Vector3f(0f, 1f, 0f), 2f, divCount);
		drawSide(new Vector3f(1f, 1f, -1f), new Vector3f(0f, 0f, 1f), new Vector3f(0f, -1f, 0f), 2f, divCount);

		vertices = new float[vertexList.size()];

		int count = 0;
		for (Float value : vertexList)
		{
			vertices[count] = value;
			count++;
		}

		indices = new int[indexList.size()];

		count = 0;
		for (Integer value : indexList)
		{
			indices[count] = value;
			count++;
		}
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

		File texFile = new File("img/earth.jpg");

		Texture texture = null;

		try
		{
			texture = TextureIO.newTexture(texFile, true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		textureId = texture.getTextureObject();

		shaderProgram.use(drawable);
		shaderProgram.setInt(drawable, "texture", 0);

		gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_S, GL4.GL_REPEAT);
		gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_T, GL4.GL_REPEAT);

		gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR);
		gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR);
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

				if (current.z == 0 && current.x < 0 && current.z > vec1.z)
				{
					updateLists(current, TexCoordOption.END);
				}
				else
				{
					updateLists(current, TexCoordOption.STANDARD);
				}

				updateLists(vec1, TexCoordOption.STANDARD);

				updateLists(vec2, TexCoordOption.STANDARD);

				if (vec3.z == 0 && vec3.x < 0 && vec3.z > vec2.z)
				{
					updateLists(vec3, TexCoordOption.END);
				}
				else
				{
					updateLists(vec3, TexCoordOption.STANDARD);
				}

				current = new Vector3f(dir1).mulAdd(increment, current);
			}

			current = new Vector3f(dir2).mulAdd((i + 1) * increment, start);
		}
	}

	@Override
	public void activateAndBindTextures(GLAutoDrawable drawable)
	{
		GL4 gl = drawable.getGL().getGL4();

		gl.glActiveTexture(GL.GL_TEXTURE0);
		gl.glBindTexture(GL4.GL_TEXTURE_2D, textureId);
	}

	private void updateLists(Vector3f inVector, TexCoordOption option)
	{
		Vector3f vector = new Vector3f(inVector);
		vector.normalize();

		float u = option == TexCoordOption.STANDARD
				? (float) ((Math.atan2(-vector.z, vector.x) + Math.PI) / (2 * Math.PI))
				: 1f;
		float v = (float) (Math.acos(-vector.y) / Math.PI);

		int heightValue = heightMap.getRGB((int) (u * (heightMap.getWidth() - 1)),
				(int) ((1f - v) * (heightMap.getHeight() - 1))) & 0xff;

		vector.mul(1 + heightValue * heightStep);

		FloatKey key = new FloatKey(vector, u, v);

		if (!map.containsKey(key))
		{
			map.put(key, curretIndex);
			curretIndex++;

			vertexList.add(vector.x);
			vertexList.add(vector.y);
			vertexList.add(vector.z);
			vertexList.add(u);
			vertexList.add(v);
		}
		indexList.add(map.get(key));
	}
}
