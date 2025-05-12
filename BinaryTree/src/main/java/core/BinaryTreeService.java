package core;

import java.io.IOException;

public interface BinaryTreeService {

	void toFile(String fileName) throws IOException;

	void printHierarchy();
	
	void preorder();

	void postorder();

}