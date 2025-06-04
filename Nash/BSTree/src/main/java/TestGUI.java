import core.AVLTree;
import core.BST;

// bajar el paquete nativo  
// https://gluonhq.com/products/javafx/ 

// en el VM poner el lib del paquete nativo
// --module-path /Users/nachopedemonte/Downloads/javafx-sdk-17.0.11/lib --add-modules javafx.fxml,javafx.controls


import controller.GraphicsTree;
import core.Person;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;

public class TestGUI extends Application {

	public static void main(String[] args) {
		// GUI
		launch(args);
	}

    @Override
	public void start(Stage stage) {
		stage.setTitle("Drawing the core.BST");
		StackPane root = new StackPane();
		Scene scene = new Scene(root, 1300, 700);

		//BST<Integer> myTree = createModel();
		//GraphicsTree<Integer> c = new GraphicsTree<>(myTree);
		//core.BST<Person> myTree = createModel2();
		//GraphicsTree<Person> c = new GraphicsTree<>(myTree);

		//core.BST<Integer> myTree = createModel3();
		AVLTree<Integer> avl = createModel4();
		GraphicsTree<Integer> c = new GraphicsTree<>(avl);

		c.widthProperty().bind(scene.widthProperty());
		c.heightProperty().bind(scene.heightProperty());
	
		root.getChildren().add(c);
		stage.setScene(scene);
		stage.show();
		

	}

    
	private BST<Integer> createModel() {
		BST<Integer> myTree = new BST<>();
		myTree = new BST<>();
		myTree.insert(50);
		myTree.insert(60);
		myTree.insert(80);
		myTree.insert(20);
		myTree.insert(70);
		myTree.insert(40);
		myTree.insert(44);
		myTree.insert(10);
		myTree.insert(40);

		return myTree;
	}

	private BST<Person> createModel2() {
		BST<Person> myTree = new BST<>();
		myTree = new BST<>();
		myTree.insert(new Person( 50, "Ana" ));
		myTree.insert(new Person( 60, "Juan") );
		myTree.insert(new Person( 80, "Sergio") );
		myTree.insert(new Person( 20, "Lila ") );
		myTree.insert(new Person( 77, "Ana") );
		myTree.inOrder();

		return myTree;
	}

	private BST<Integer> createModel3() {
		BST<Integer> myTree = new BST<>();
		myTree = new BST<>();
		myTree.insert(50);
		myTree.insert(60);
		myTree.insert(80);
		myTree.insert(20);
		myTree.insert(70);
		myTree.insert(40);
		myTree.insert(44);
		myTree.insert(10);
		myTree.insert(40);
		//myTree.inOrder();
		myTree.delete(80);
		myTree.delete(10);

		return myTree;
	}

	private AVLTree<Integer> createModel4() {
		AVLTree<Integer> avl = new AVLTree<>();
		/*
		avl.insert(1);
		avl.insert(2);
		avl.insert(4);
		avl.insert(7);
		avl.insert(15);
		avl.insert(3);
		avl.insert(10);
		avl.insert(17);
		avl.insert(19);
		avl.insert(16);
		*/
		avl.insert(2);
		avl.insert(4);
		avl.insert(6);
		avl.insert(10);
		avl.insert(9);

		return avl;
	}
}