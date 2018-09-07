#version 330

in vec2 outTexCoord;

out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec3 blockLight;
uniform vec3 ambientLight;

vec4 maximumLight = vec4(1.0, 1.0, 1.0, 1.0);

void main() {
	vec4 light = (vec4(ambientLight, 1.0) + vec4(blockLight, 1.0));
	fragColor = min(light, maximumLight) * texture(texture_sampler, outTexCoord);
}