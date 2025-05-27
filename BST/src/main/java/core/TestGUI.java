package core;
import core.BST;

// bajar el paquete nativo  
// https://gluonhq.com/products/javafx/ 

// en el VM poner el lib del paquete nativo
// --module-path C:\Users\lgomez\Downloads\javafx-sdk-11.0.2\lib --add-modules javafx.fxml,javafx.controls


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
		stage.setTitle("Drawing the BST");
		StackPane root = new StackPane();
		Scene scene = new Scene(root, 1300, 700);

		BST<Integer> myTree = createModel();
		GraphicsTree<Integer> c = new GraphicsTree<>(myTree);
		/*BST<Person> myTree2 = createModel2();
		GraphicsTree<Person> c2 = new GraphicsTree<>(myTree2);*/

		c.widthProperty().bind(scene.widthProperty());
		c.heightProperty().bind(scene.heightProperty());
	
		root.getChildren().add(c);
		stage.setScene(scene);
		stage.show();

		myTree.remove(40);
		stage.show();

		myTree.remove(35);
		stage.show();

		myTree.remove(8);
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
		myTree.inOrder();

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
	

}