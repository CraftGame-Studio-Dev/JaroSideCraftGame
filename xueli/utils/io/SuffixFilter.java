package xueli.utils.io;
import java.io.File;
import java.io.FileFilter;
public class SuffixFilter implements FileFilter {// modification: preprocess the suffix to avoid frequent "."+suffix
	private String suffix;
	/**
	 * Create a file filter to match a specific suffix
	 * 
	 * @param suffix the suffix to be filtered, doesn't include the '.'
	 * @author LovelyZeeiam
	 */
	public SuffixFilter(String suffix) {
		if (suffix.equals("")) this.suffix = "";
		else this.suffix = "." + suffix;
	}
	@Override
	public boolean accept(File pathname) {// which always create StringBuilder object, cause performance loss
		return pathname.getName().endsWith(suffix);
	}
}
