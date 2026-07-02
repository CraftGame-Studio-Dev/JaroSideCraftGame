#version 450 core
layout (location = 0) in vec3 in_coord;
layout (location = 1) in vec4 tex_coord;
layout (location = 2) in float psize;
out vec4 out_coord;
layout (location = 0) uniform mat4 model;
layout (location = 1) uniform mat4 view;
layout (location = 2) uniform mat4 projection;
void main(){
	gl_Position = projection * view * model * vec4(in_coord, 1.0);
	out_coord=tex_coord;
	gl_PointSize=psize;
}
