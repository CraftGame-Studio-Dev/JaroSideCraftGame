#version 450 core
layout (location = 0) in vec3 position;
layout (location = 1) in vec3 tex_coord;
out vec3 tex;
layout (location = 1) uniform mat4 view;
layout (location = 2) uniform mat4 projection;
void main(){
	vec4 coord=projection*view*vec4(position,0);
	gl_Position=coord.xyww;
	tex=tex_coord;
}