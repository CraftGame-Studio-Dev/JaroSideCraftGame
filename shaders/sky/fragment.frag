#version 450 core
out vec4 FragColor;
in vec3 tex;
layout (location = 3) uniform samplerCube skybox;
void main(){
	FragColor=texture(skybox,tex);
}