#version 450 core
layout (location = 0) in vec3 in_coord;
layout (location = 1) in vec2 tex_coord;
layout (location = 2) in vec3 normalVector;
out vec2 out_coord;
out vec3 normal;
out uint textureID;
out vec3 frag_pos;
layout (location = 0) uniform mat4 model;
layout (location = 1) uniform mat4 view;
layout (location = 2) uniform mat4 projection;
void main(){
	gl_Position = projection * view * model * vec4(in_coord, 1.0);
	out_coord=tex_coord;
	normal=normalVector;
	textureID=gl_VertexID>>2;
	frag_pos=(model * vec4(in_coord, 1.0)).xyz;
}