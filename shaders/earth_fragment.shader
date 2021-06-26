#version 330 core

out vec4 FragColor;

in vec2 TexCoord;
in vec3 position;

uniform sampler2D texture;
uniform sampler2D specularMap;
uniform vec3 lightPos;
uniform vec3 viewPos;

void main()
{
	vec3 normal = normalize(position);
	vec3 light = normalize(lightPos - position);
	vec3 reflected = reflect(-light, normal);
	vec3 view = normalize(viewPos - position);
	float lambert = max(dot(normal, light), 0);
	float specular = pow(max(dot(view, reflected), 0), 7);
    FragColor = lambert * texture(texture, TexCoord) + specular * texture(specularMap, TexCoord);
}