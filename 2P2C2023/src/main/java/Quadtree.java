public class Quadtree {

    private QTNode root;

    public Quadtree(Integer[][] matrix) {
        if (!checkDimIsSquareAndEven(matrix))
            throw new RuntimeException("Invalid Dim");

        this.root = buildQuadtree(matrix, 0, 0, matrix.length);
    }

    public Integer[][] getMatrix() {
        if (root == null) return null;
        
        int dim = root.getDim();
        Integer[][] matrix = new Integer[dim][dim];
        fillMatrix(root, matrix, 0, 0);
        return matrix;
    }

    private static boolean checkDimIsSquareAndEven(Integer[][] matrix) {
        if (matrix == null)
            return false;

        int dim = matrix.length;

        // es par?
        if (dim % 2 == 1)
            return false;

        // todas las filas tienen la misma dimension?
        for (int rec = 0; rec < dim; rec++) {
            if (matrix[rec].length != dim)
                return false;
        }
        return true;
    }

    /**
     * Construye recursivamente un nodo del quadtree para la región especificada
     * @param matrix matriz original
     * @param row fila inicial de la región
     * @param col columna inicial de la región  
     * @param dim dimensión de la región (dim x dim)
     * @return nodo construido
     */
    private QTNode buildQuadtree(Integer[][] matrix, int row, int col, int dim) {
        QTNode node = new QTNode();
        node.dimension = dim;
        
        // Verificar si todos los valores en la región son iguales
        if (isUniform(matrix, row, col, dim)) {
            // Crear nodo hoja
            node.data = matrix[row][col];
            // Los hijos quedan null por defecto
        } else {
            // Crear nodo interno con 4 hijos
            node.data = null;
            int halfDim = dim / 2;
            
            // Crear los 4 cuadrantes en orden: NO, NE, SO, SE
            node.upperLeft = buildQuadtree(matrix, row, col, halfDim);                    // NO
            node.upperRight = buildQuadtree(matrix, row, col + halfDim, halfDim);        // NE  
            node.lowerLeft = buildQuadtree(matrix, row + halfDim, col, halfDim);         // SO
            node.lowerRight = buildQuadtree(matrix, row + halfDim, col + halfDim, halfDim); // SE
        }
        
        return node;
    }
    
    /**
     * Verifica si todos los valores en una región de la matriz son iguales
     * @param matrix matriz a verificar
     * @param row fila inicial
     * @param col columna inicial
     * @param dim dimensión de la región
     * @return true si todos los valores son iguales
     */
    private boolean isUniform(Integer[][] matrix, int row, int col, int dim) {
        Integer firstValue = matrix[row][col];
        
        for (int i = row; i < row + dim; i++) {
            for (int j = col; j < col + dim; j++) {
                if (!matrix[i][j].equals(firstValue)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Rellena recursivamente una región de la matriz a partir del quadtree
     * @param node nodo actual del quadtree
     * @param matrix matriz a rellenar
     * @param row fila inicial de la región
     * @param col columna inicial de la región
     */
    private void fillMatrix(QTNode node, Integer[][] matrix, int row, int col) {
        if (node == null) return;
        
        if (node.data != null) {
            // Nodo hoja: llenar toda la región con el valor del nodo
            int dim = node.getDim();
            for (int i = row; i < row + dim; i++) {
                for (int j = col; j < col + dim; j++) {
                    matrix[i][j] = node.data;
                }
            }
        } else {
            // Nodo interno: procesar recursivamente los 4 cuadrantes
            int halfDim = node.getDim() / 2;
            
            // Procesar en orden: NO, NE, SO, SE
            fillMatrix(node.upperLeft, matrix, row, col);                          // NO
            fillMatrix(node.upperRight, matrix, row, col + halfDim);               // NE
            fillMatrix(node.lowerLeft, matrix, row + halfDim, col);                // SO
            fillMatrix(node.lowerRight, matrix, row + halfDim, col + halfDim);     // SE
        }
    }

    public String toString() {
        Integer[][] m = getMatrix();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < m.length; ++i) {
            for (int j = 0; j < m.length; ++j) {
                sb.append(m[i][j]);
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private class QTNode {
        private Integer data;

        private int dimension;

        public int getDim() {
            return dimension;
        }

        private QTNode upperLeft;
        private QTNode upperRight;
        private QTNode lowerLeft;
        private QTNode lowerRight;
    }

    public static void main(String[] args) {

        // caso de uso A
        Integer[][] matrix1 = new Integer[][] {
                { 1, 2 },
                { 3, 1 }
        };

        Quadtree qt1 = new Quadtree(matrix1);
        System.out.println(qt1);

        // caso de uso B
        Integer[][] matrix2 = new Integer[][] {
                { 1, 1 },
                { 1, 1 }
        };

        Quadtree qt2 = new Quadtree(matrix2);
        System.out.println(qt2);

        // caso de uso C
        Integer[][] matrix3 = new Integer[][] {
                { 1, 1, 3, 3 },
                { 1, 2, 3, 3 },
                { 3, 1, 4, 4 },
                { 2, 1, 4, 4 }
        };

        Quadtree qt3 = new Quadtree(matrix3);
        System.out.println(qt3);

        // caso de uso D
        Integer[][] matrix4 = new Integer[][] {
                { 1, 1, 3, 3, 8, 8, 8, 8 },
                { 1, 1, 3, 3, 8, 8, 8, 8 },
                { 3, 1, 4, 4, 8, 8, 8, 8 },
                { 2, 1, 4, 4, 8, 8, 8, 8 },
                { 1, 1, 1, 1, 7, 7, 7, 7 },
                { 1, 1, 1, 1, 7, 7, 7, 7 },
                { 1, 1, 1, 1, 7, 7, 7, 7 },
                { 1, 1, 1, 1, 7, 7, 7, 7 },
        };

        Quadtree qt4 = new Quadtree(matrix4);
        System.out.println(qt4);

    }

}
