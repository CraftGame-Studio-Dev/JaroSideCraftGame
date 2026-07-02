#version 450 core
out vec4 fragColor;
in vec4 out_coord;
layout (location = 3) uniform sampler2D tex;
void main(){
	fragColor=texture(tex,vec2(mix(out_coord.z,out_coord.x,gl_PointCoord.x),mix(out_coord.w,out_coord.y,gl_PointCoord.y)));
}