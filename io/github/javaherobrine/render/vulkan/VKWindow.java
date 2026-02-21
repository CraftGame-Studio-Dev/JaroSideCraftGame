package io.github.javaherobrine.render.vulkan;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWVulkan.*;
import static org.lwjgl.vulkan.VK14.*;
import org.lwjgl.glfw.*;
import org.lwjgl.vulkan.*;
import org.lwjgl.system.*;
import xueli.game2.lifecycle.*;
public class VKWindow implements LifeCycle{
	VkInstance instance;
	long window;
	public VKWindow() {
		glfwInit();
		init();
		long monitor=glfwGetPrimaryMonitor();
		var video=glfwGetVideoMode(monitor);
		int monitorWidth=video.width(),monitorHeight=video.height();
		glfwWindowHint(GLFW_CLIENT_API,GLFW_NO_API);
		glfwWindowHint(GLFW_POSITION_X, monitorWidth>>2);
		glfwWindowHint(GLFW_POSITION_Y, monitorHeight>>2);
		window = glfwCreateWindow(monitorWidth>>1,monitorHeight>>1, "CraftGame, but use Vulkan", 0, 0);
		while(!glfwWindowShouldClose(window)) {
			glfwPollEvents();
		}
	}
	@Override
	public void init() {
		//Window
		glfwInit();
		init();
		GLFWErrorCallback.createPrint(System.err).set();
		long monitor=glfwGetPrimaryMonitor();
		var video=glfwGetVideoMode(monitor);
		int monitorWidth=video.width(),monitorHeight=video.height();
		glfwWindowHint(GLFW_CLIENT_API,GLFW_NO_API);
		glfwWindowHint(GLFW_POSITION_X, monitorWidth>>2);
		glfwWindowHint(GLFW_POSITION_Y, monitorHeight>>2);
		window = glfwCreateWindow(monitorWidth>>1,monitorHeight>>1, "CraftGame, but use Vulkan", 0, 0);
		//VKApplicationInfo
		MemoryStack stack=MemoryStack.stackGet();
		int ptr=stack.getPointer();
		stack.nUTF8("CraftGame",true);
		long addr=stack.getPointerAddress();
		VkApplicationInfo app=VkApplicationInfo.create();
		app.sType(VK_STRUCTURE_TYPE_APPLICATION_INFO);
		long struct=app.address();
		MemoryUtil.memPutAddress(struct+VkApplicationInfo.PAPPLICATIONNAME,addr);
		MemoryUtil.memPutAddress(struct+VkApplicationInfo.PENGINENAME,addr);
		app.apiVersion(VK_API_VERSION_1_4);
		app.applicationVersion(0);
		app.engineVersion(0);
		//VkInstanceCreateInfo
		VkInstanceCreateInfo info=VkInstanceCreateInfo.create();
		info.sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO);
		info.pApplicationInfo(app);
		long addr0=stack.nmalloc(MemoryStack.POINTER_SIZE);
		long addr1=nglfwGetRequiredInstanceExtensions(addr0);
		struct=info.address();
		MemoryUtil.memPutAddress(struct+VkInstanceCreateInfo.PPENABLEDEXTENSIONNAMES,addr1);
		VkInstanceCreateInfo.nenabledExtensionCount(struct, MemoryUtil.memGetInt(addr0));
		info.ppEnabledLayerNames(null);
		//instance
		addr=stack.nmalloc(MemoryStack.POINTER_SIZE);
		int result=nvkCreateInstance(struct,0,addr);
		if(result!=0) {
			System.err.println("[FATAL] You can't play CraftGame with Vulkan with error code "+result);
			throw new Error();
		}
		instance=new VkInstance(MemoryUtil.memGetLong(addr),info);
		stack.setPointer(ptr);
	}
	@Override
	public void tick() {
		glfwPollEvents();
	}
	@Override
	public void release() {
		nvkDestroyInstance(instance,0);
		glfwDestroyWindow(window);
		glfwTerminate();
	}
}
