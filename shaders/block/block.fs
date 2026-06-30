#version 450 core
out vec4 fragColor;
in vec2 out_coord;
in vec3 normal;
in vec3 frag_pos;
flat in uint textureID;
layout (location = 3) uniform sampler2D tex[6];
layout (location = 9) uniform sampler2D overlap;
layout (location = 10) uniform vec3 ambient = vec3(1,1,1);
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
	vec3 lightSourcePos=vec3(10,10,10);
	float diffuse=max(0,dot(normalize(lightSourcePos-frag_pos),normal));
	fragColor=fragColor*vec4(vec3(diffuse,diffuse,diffuse)+ambient,1);
}