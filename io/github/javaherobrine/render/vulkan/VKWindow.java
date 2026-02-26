package io.github.javaherobrine.render.vulkan;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWVulkan.*;
import static org.lwjgl.vulkan.VK14.*;
import org.lwjgl.glfw.*;
import org.lwjgl.vulkan.*;
import org.lwjgl.system.*;
import xueli.game2.lifecycle.*;
public class VKWindow implements LifeCycle{
	public static final boolean VULKAN_VALIDATION_LAYER=true;
	VkInstance instance;
	VkPhysicalDevice GPU;
	long window;
	public VKWindow() {
		init();
		while(!glfwWindowShouldClose(window)) {
			glfwPollEvents();
		}
		release();
	}
	@Override
	public void init() {
		//Window
		glfwInit();
		GLFWErrorCallback.createPrint(System.err).set();
		long monitor=glfwGetPrimaryMonitor();
		var video=glfwGetVideoMode(monitor);
		int monitorWidth=video.width(),monitorHeight=video.height();
		glfwWindowHint(GLFW_CLIENT_API,GLFW_NO_API);
		glfwWindowHint(GLFW_POSITION_X, monitorWidth>>2);
		glfwWindowHint(GLFW_POSITION_Y, monitorHeight>>2);
		window = glfwCreateWindow(monitorWidth>>1,monitorHeight>>1, "CraftGame, but use Vulkan", 0, 0);
		//Validation Layer
		int count=0;
		long ptrAddr=0;
		MemoryStack stack=MemoryStack.stackGet();
		int ptr=stack.getPointer();
		long addr,countAddr;
		if(VULKAN_VALIDATION_LAYER) {
			countAddr=stack.nmalloc(MemoryStack.POINTER_SIZE);
			nvkEnumerateInstanceLayerProperties(countAddr,0);
			count=MemoryUtil.memGetInt(countAddr);
			ptrAddr=stack.nmalloc(count*VkLayerProperties.SIZEOF);
			nvkEnumerateInstanceLayerProperties(countAddr,ptrAddr);
		}
		//VKApplicationInfo
		stack.nUTF8("CraftGame",true);
		addr=stack.getPointerAddress();
		VkApplicationInfo app=VkApplicationInfo.create();
		app.sType(VK_STRUCTURE_TYPE_APPLICATION_INFO);
		long struct=app.address();
		MemoryUtil.memPutAddress(struct+VkApplicationInfo.PAPPLICATIONNAME,addr);
		MemoryUtil.memPutAddress(struct+VkApplicationInfo.PENGINENAME,addr);
		app.apiVersion(VK_API_VERSION_1_4);
		app.applicationVersion(0);
		app.engineVersion(0);
		//VkInstance
		VkInstanceCreateInfo info=VkInstanceCreateInfo.create();
		info.sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO);
		info.pApplicationInfo(app);
		long addr0=stack.nmalloc(MemoryStack.POINTER_SIZE);
		long addr1=nglfwGetRequiredInstanceExtensions(addr0);
		struct=info.address();
		MemoryUtil.memPutAddress(struct+VkInstanceCreateInfo.PPENABLEDEXTENSIONNAMES,addr1);
		VkInstanceCreateInfo.nenabledExtensionCount(struct, MemoryUtil.memGetInt(addr0));
		MemoryUtil.memPutAddress(struct+VkInstanceCreateInfo.PPENABLEDLAYERNAMES,ptrAddr);
		VkInstanceCreateInfo.nenabledLayerCount(struct+VkInstanceCreateInfo.ENABLEDLAYERCOUNT, count);
		long addr2=stack.nmalloc(MemoryStack.POINTER_SIZE);
		int result=nvkCreateInstance(struct,0,addr2);
		if(result!=0) {
			System.err.println("[FATAL] You can't play CraftGame with Vulkan with error code "+result);
			throw new VulkanError("Can't create a Vulkan instance");
		}
		instance=new VkInstance(MemoryUtil.memGetLong(addr2),info);
		//VkDevice
		nvkEnumeratePhysicalDevices(instance,countAddr,0);
		count=MemoryUtil.memGetInt(countAddr);
		if(count==0) {
			System.err.println("[FATAL] You can't play CraftGame with Vulkan as you have 0 device");
			throw new VulkanError("No Device");
		}
		ptrAddr=stack.nmalloc(count<<MemoryStack.POINTER_SHIFT);
		VkPhysicalDevice[] handles=new VkPhysicalDevice[5];
		nvkEnumeratePhysicalDevices(instance,countAddr,ptrAddr);
		var dev=VkPhysicalDeviceProperties.malloc();
		for(int i=0;i<count;++i) {
			var handle=new VkPhysicalDevice(MemoryUtil.memGetLong(ptrAddr),instance);
			//Judge whether the device is suitable
			vkGetPhysicalDeviceProperties(handle, dev);
			System.err.println("[INFO] Vulkan: found device: "+dev.deviceNameString());
			System.err.println("[INFO] Vulkan: device type="+vulkanPhysicalDeviceType(dev.deviceType()));
			if(dev.apiVersion()<VK_API_VERSION_1_4) {
				System.err.println("[INFO] Vulkan: This device does not support Vulkan 1.4");
			}else {
				handles[dev.deviceType()]=handle;
			}
			ptrAddr+=MemoryStack.POINTER_SIZE;
		}
		int selectedDevice=-1;
		if(handles[2]!=null) {
			selectedDevice=2;
		}
		if(selectedDevice==-1&&handles[1]!=null) {
			selectedDevice=1;
		}
		if(selectedDevice==-1&&handles[3]!=null) {
			selectedDevice=3;
		}
		if(selectedDevice==-1&&handles[0]!=null) {
			selectedDevice=0;
		}
		if(selectedDevice==-1&&handles[4]!=null) {
			selectedDevice=4;
		}
		if(selectedDevice==-1) {
			System.err.println("[FATAL] Vulkan: No suitable device");
			throw new VulkanError("No Device");
		}else if(selectedDevice!=2) {
			System.err.println("[INFO] It's recommended to install a discrete GPU");
			if(selectedDevice==4) {
				System.err.println("[WARNING] CPU is not suitable for rendering!");
			}
		}
		GPU=handles[selectedDevice];
		vkGetPhysicalDeviceProperties(GPU, dev);
		System.err.println("[INFO] selected device name: "+dev.deviceNameString());
		dev.free();
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
	private static String vulkanPhysicalDeviceType(int type) {
		switch(type) {
		case 0:
			return "Other Device(Unknown)";
		case 1:
			return "Integrated GPU";
		case 2:
			return "Discrete GPU";
		case 3:
			return "Virtual Device";
		case 4:
			return "CPU";
		default:
			return "Invalid Device Type";	
		}
	}
}
