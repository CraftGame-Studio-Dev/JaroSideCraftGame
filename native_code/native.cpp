#include"io_github_javaherobrine_GameUtils.h"
jfieldID addressID;
void* (*func)(JNIEnv*&,jobject&);
void* getAddress(JNIEnv*& env,jobject& direct){
	return env->GetDirectBufferAddress(direct);
}
void* getAddressReflect(JNIEnv*& env,jobject& direct){
	return reinterpret_cast<void*>(env->GetLongField(direct,addressID));
}
JNIEXPORT jlong JNICALL Java_io_github_javaherobrine_GameUtils_address___3B
  (JNIEnv * env, jclass, jbyteArray data){
	return reinterpret_cast<jlong>(env->GetPrimitiveArrayCritical(data,nullptr));
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
		jclass clazz=env->FindClass("java/nio/Buffer");
		addressID=env->GetFieldID(clazz,"address","J");
		func=getAddressReflect;
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
JNIEXPORT void JNICALL Java_io_github_javaherobrine_GameUtils_modelMatrix
  (JNIEnv *, jclass, jlong addr, jfloat x, jfloat y, jfloat z){
	float* ptr=reinterpret_cast<float*>(addr);
	ptr[12]=x;
	ptr[13]=y;
	ptr[14]=z;
}
JNIEXPORT jlong JNICALL Java_io_github_javaherobrine_GameUtils_address__Ljava_nio_Buffer_2
  (JNIEnv* env, jclass, jobject object){
	return reinterpret_cast<jlong>(func(env,object));
}
JNIEXPORT void JNICALL Java_io_github_javaherobrine_GameUtils_makeIdentity
  (JNIEnv *env, jclass, jlong addr){
	float* ptr=reinterpret_cast<float*>(addr);
	ptr[0]=ptr[5]=ptr[10]=ptr[15]=1;
	ptr[1]=ptr[2]=ptr[3]=ptr[4]=ptr[6]=ptr[7]=ptr[8]=ptr[9]=ptr[11]=ptr[12]=ptr[13]=ptr[14]=0;
}
