#include"io_github_javaherobrine_GameUtils.h"
#include<functional>
std::function<void*(JNIEnv*&,jobject&)> func;
void* getAddress(JNIEnv*& env,jobject& direct){
	return env->GetDirectBufferAddress(direct);
}
struct GetAddressByReflection{
	jfieldID address;
	void* operator()(JNIEnv*& env,jobject& direct){
		return reinterpret_cast<void*>(env->GetLongField(direct,address));
	}
};
JNIEXPORT jlong JNICALL Java_io_github_javaherobrine_GameUtils_address
  (JNIEnv * env, jclass, jbyteArray data){
	return reinterpret_cast<jlong>(env->GetPrimitiveArrayCritical (data,nullptr));
}
JNIEXPORT void JNICALL Java_io_github_javaherobrine_GameUtils_allowGC
  (JNIEnv *env, jclass, jlong addr, jbyteArray data){
	env->ReleasePrimitiveArrayCritical(data,reinterpret_cast<void*>(addr),0);
}
JNIEXPORT void JNICALL Java_io_github_javaherobrine_GameUtils_supportsNIOAccess
  (JNIEnv *env, jclass, jobject direct){
	if(env->GetDirectBufferAddress(direct)){
		func=getAddress;
	}else{
		fprintf(stderr,"%s\n","[WARNING] Performance Decreased: Access to the address of direct buffers is unreachable");
		fflush(stderr);
		GetAddressByReflection reflect;
		jclass clazz=env->FindClass("java/nio/Buffer");
		reflect.address=env->GetFieldID(clazz,"address","J");
		func=reflect;
	}
}
JNIEXPORT void JNICALL Java_io_github_javaherobrine_GameUtils_to3x3
  (JNIEnv *env, jclass, jobject direct){
	float* data=static_cast<float*>(func(env,direct));
	data[3]=0;
	data[7]=0;
	data[11]=0;
	data[12]=0;
	data[13]=0;
	data[14]=0;
}
JNIEXPORT jlong JNICALL Java_io_github_javaherobrine_GameUtils_pointerOfPointer
  (JNIEnv *env, jclass, jlongArray ptr){
	return reinterpret_cast<jlong>(env->GetPrimitiveArrayCritical(ptr,nullptr));
}
JNIEXPORT void JNICALL Java_io_github_javaherobrine_GameUtils_freePointerOfPointer
  (JNIEnv *env, jclass, jlong addr, jlongArray data){
	env->ReleasePrimitiveArrayCritical(data,reinterpret_cast<void*>(addr),0);
}
