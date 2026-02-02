#version 450 core
out vec4 fragColor;
in vec2 out_coord;
layout (location = 3) uniform sampler2D tex;
layout (location = 4) uniform sampler2D overlap;
layout (location = 5) uniform vec3 ambient = vec3(1,1,1);
void main(){
	vec4 o=texture(overlap,out_coord);
	if(o.w<0.1){
		fragColor=texture(tex,out_coord);
	}else{
		fragColor=o;
	}
	fragColor=fragColor*vec4(ambient,1);
	//fragColor=mix(texture(tex,out_coord),texture(overlap,out_coord),0.4);
	//fragColor=vec4(out_color,1.0);	
	//fragColor=texture(tex,out_coord);
	//fragColor=vec4(1,1,1,1);
}