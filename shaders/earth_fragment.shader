#version 330 core

out vec4 FragColor;

in vec2 TexCoord;
in vec3 position;

uniform sampler2D texture;
uniform vec3 lightPos;

void main()
{
	vec3 normal = normalize(position);
	vec3 light = normalize(lightPos - position);
	float lambert = max(dot(normal, light), 0);
    FragColor = lambert * texture(texture, TexCoord);
}