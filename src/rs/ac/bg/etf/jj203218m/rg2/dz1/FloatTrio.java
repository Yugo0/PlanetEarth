package rs.ac.bg.etf.jj203218m.rg2.dz1;

import org.joml.Vector3f;

public class FloatTrio
{
	private static final float TRESHOLD = 0.001f;

	public float first;
	public float second;
	public float third;

	public FloatTrio(float first, float second, float third)
	{
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public FloatTrio(Vector3f vector)
	{
		this.first = vector.x;
		this.second = vector.y;
		this.third = vector.z;
	}

	@Override
	public int hashCode()
	{
		return Float.hashCode(first) ^ Float.hashCode(first) ^ Float.hashCode(second) ^ Float.hashCode(third);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		if (!(obj instanceof FloatTrio))
		{
			return false;
		}
		if (Math.abs(this.first - ((FloatTrio) obj).first) < TRESHOLD
				&& Math.abs(this.second - ((FloatTrio) obj).second) < TRESHOLD
				&& Math.abs(this.third - ((FloatTrio) obj).third) < TRESHOLD)
		{
			return true;
		}
		return false;
	}

}
