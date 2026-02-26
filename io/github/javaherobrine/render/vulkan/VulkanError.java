package io.github.javaherobrine.render.vulkan;
public class VulkanError extends Error{
	public VulkanError() {
		super();
	}
	public VulkanError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	public VulkanError(String message, Throwable cause) {
		super(message, cause);
	}
	public VulkanError(String message) {
		super(message);
	}
	public VulkanError(Throwable cause) {
		super(cause);
	}
	private static final long serialVersionUID = 1L;
}
