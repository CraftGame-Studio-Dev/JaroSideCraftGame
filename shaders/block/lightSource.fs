#version 450 core
out vec4 fragColor;
in vec2 out_coord;
flat in uint textureID;
layout (location = 3) uniform sampler2D tex[6];
layout (location = 9) uniform sampler2D overlap;
void main(){
	vec4 t=texture(tex[textureID],out_coord);
	vec4 o=texture(overlap,out_coord);
	if(o.w<0.1){
		if(t.w==0){
			discard;
		}
		fragColor=t;
	}else{
		fragColor=o;
	}
}