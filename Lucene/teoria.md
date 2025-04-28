**Resumen Consolidado de Lucene: Conceptos y Ejemplos Prácticos**

**1. Conceptos Fundamentales de Lucene**

* **Índice Invertido:** Lucene utiliza un índice invertido, que mapea *términos* (palabras o tokens) a los *documentos* que los contienen. Esto permite búsquedas de texto muy rápidas.
* **Documento (Document):** Es la unidad básica de información en Lucene (por ejemplo, un archivo, un registro de base de datos). Contiene uno o más *Campos*. Lucene le asigna un ID interno (`docId`).
* **Campo (Field):** Es una pieza de información dentro de un Documento, con un nombre (ej: "titulo", "contenido", "fecha") y un valor.
* **Término (Term):** Es la unidad básica de búsqueda, compuesta por el texto del término y el nombre del campo al que pertenece (ej: el término "lucene" en el campo "contenido").

**2. Indexación: Cómo se Construye el Índice (`IndexBuilder.java`)**

El proceso de añadir información a Lucene se llama indexación.

* **`IndexWriter`:** Es la clase principal para crear y modificar el índice. Se configura con un `Directory` (dónde guardar el índice, usualmente `FSDirectory`) y un `Analyzer` (ver más abajo).
    ```java
    Directory indexDir = FSDirectory.open(Paths.get("ruta/al/indice"));
    StandardAnalyzer analyzer = new StandardAnalyzer();
    IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
    IndexWriter indexWriter = new IndexWriter(indexDir, iwc);
    ```
* **Crear `Document` y `Field`:** Para cada unidad de información, se crea un `Document` y se le añaden `Field`s.
    ```java
    Document luceneDoc = new Document();
    ```
* **Tipos de Campos y Opciones:**
    * **`TextField`:** Para texto completo que necesita ser analizado (dividido en términos) y buscado. Ideal para el cuerpo de un texto o títulos.
        ```java
        // Añade el contenido de un archivo, será analizado por StandardAnalyzer
        luceneDoc.add(new TextField("content", new BufferedReader(new InputStreamReader(inputStream))));
        ```
    * **`FieldType` Personalizado:** Permite control granular sobre:
        * **`setStored(true/false)`:** Si el valor original del campo se almacena literalmente en el índice (para poder recuperarlo tal cual).
        * **`setIndexOptions(...)`:** Si y cómo se indexa el campo (NONE, DOCS, DOCS_AND_FREQS, DOCS_AND_FREQS_AND_POSITIONS, etc.). Define si se puede buscar por el campo y cuánta información se guarda para el scoring y búsquedas de frases.
        ```java
        // Campo "path": Almacenado (para mostrarlo) pero no indexado (no se busca por él)
        FieldType pathFieldType = new FieldType();
        pathFieldType.setStored(true);
        pathFieldType.setIndexOptions(IndexOptions.NONE);
        luceneDoc.add(new Field("path", "ruta/al/archivo.txt", pathFieldType));
        ```
* **Añadir al Índice y Cerrar:** Se añade el documento al `IndexWriter` y, al finalizar, se cierra el writer para confirmar los cambios.
    ```java
    indexWriter.addDocument(luceneDoc);
    // ... (loop para más documentos) ...
    indexWriter.close(); // ¡Muy importante!
    ```

**3. Análisis: Procesamiento del Texto con Analyzers (`TestAnalyzer.java`)**

* **Rol:** Los `Analyzer`s procesan el texto de los campos (durante la indexación) y el texto de las consultas (durante la búsqueda) para convertirlos en una secuencia de *tokens* (términos). Realizan tareas como dividir en palabras, pasar a minúsculas, eliminar stopwords (palabras comunes como "el", "la", "y") y aplicar stemming (reducir palabras a su raíz).
* **Importancia:** Es **crucial** usar el mismo `Analyzer` (o uno compatible) al indexar y al buscar para que los términos coincidan.
* **Ejemplos:**
    * **`StandardAnalyzer`:** Bueno para muchos idiomas, elimina stopwords en inglés por defecto, pasa a minúsculas.
    * **`SimpleAnalyzer`:** Divide por no letras y pasa a minúsculas.
    * **`WhitespaceAnalyzer`:** Divide solo por espacios.
    * **`SpanishAnalyzer`:** Específico para español, con stopwords y stemming en español.
* **Ver Tokens:** Se puede inspeccionar qué tokens genera un analizador:
    ```java
    Analyzer analyzer = new StandardAnalyzer();
    String text = "Ejemplo de Texto para Analizar.";
    try (TokenStream stream = analyzer.tokenStream("nombre_campo", text)) {
        CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
        stream.reset();
        while (stream.incrementToken()) {
            System.out.println(termAtt.toString()); // Muestra: "ejemplo", "texto", "analizar"
        }
        stream.end();
    }
    ```

**4. Búsqueda: Encontrando Documentos**

Hay dos formas principales de crear consultas:

* **A) Query API (`TheSearcher.java`):** Construir objetos `Query` mediante programación.
    * **`IndexSearcher`:** Clase para realizar búsquedas sobre un índice abierto (`IndexReader`).
        ```java
        IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get("ruta/al/indice")));
        IndexSearcher searcher = new IndexSearcher(indexReader);
        ```
    * **Tipos de `Query` comunes:**
        * `TermQuery`: Busca un término exacto. `new TermQuery(new Term("content", "game"))`
        * `PrefixQuery`: Busca por prefijo. `new PrefixQuery(new Term("content", "ga"))`
        * `WildcardQuery`: Usa comodines (`*`, `?`). `new WildcardQuery(new Term("content", "g?me"))`
        * `FuzzyQuery`: Busca términos similares (distancia de edición). `new FuzzyQuery(new Term("content", "gam"))`
        * `PhraseQuery`: Busca una secuencia exacta de términos. `new PhraseQuery.Builder().add(new Term("content", "video")).add(new Term("content", "review")).build()`
        * `BooleanQuery`: Combina otras queries (AND, OR, NOT).
            ```java
            // Busca "game" Y "store"
            BooleanQuery bq = new BooleanQuery.Builder()
                .add(new TermQuery(new Term("content", "game")), BooleanClause.Occur.MUST)
                .add(new TermQuery(new Term("content", "store")), BooleanClause.Occur.MUST)
                .build();
            ```

* **B) Query Parser (`TheSearcherQueryParser.java`):** Escribir consultas como texto usando una sintaxis específica. Es más flexible y común para interfaces de usuario.
    * **Instanciación:** Requiere el `Analyzer` (el mismo usado al indexar) y un campo por defecto.
        ```java
        QueryParser parser = new QueryParser("content", new StandardAnalyzer()); // "content" es el campo por defecto
        ```
    * **Sintaxis de Consulta (ejemplos):**
        * Término simple: `parser.parse("game")` (busca "game" en "content")
        * Término en campo: `parser.parse("title:lucene")`
        * Frase: `parser.parse("\"information retrieval\"")`
        * Booleanos: `parser.parse("game AND store")`, `parser.parse("review OR analysis")`, `parser.parse("game AND NOT video")`
        * Agrupación: `parser.parse("(review OR game) AND store")`
        * Prefijo: `parser.parse("info*")`
        * Wildcard: `parser.parse("t?st*")`
        * Fuzzy: `parser.parse("lucene~")` (distancia 2), `parser.parse("lucene~1")` (distancia 1)

* **Ejecución y Resultados:**
    1.  Se crea la `Query` (con API o Parser).
    2.  Se usa `searcher.search(query, n)` para obtener los `n` mejores resultados (`TopDocs`).
    3.  Se itera sobre `topDocs.scoreDocs` para obtener cada `ScoreDoc`.
    4.  De cada `ScoreDoc` se obtiene el `docID` y el `score` (relevancia).
    5.  Con `searcher.doc(docID)` se recupera el `Document` original para acceder a los campos almacenados (ej: `doc.get("path")`).
    ```java
    TopDocs topDocs = searcher.search(query, 10); // Buscar top 10
    System.out.println("Encontrados: " + topDocs.totalHits);
    for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
        int docID = scoreDoc.doc;
        Document doc = searcher.doc(docID);
        System.out.println("Score: " + scoreDoc.score + " Path: " + doc.get("path"));
    }
    indexReader.close(); // Cerrar al final
    ```

**5. Scoring (Ranking)**

Lucene calcula una puntuación (`score`) para determinar la relevancia de cada documento respecto a la consulta. La fórmula clásica (TF-IDF) considera la frecuencia del término en el documento (TF) y cuán raro es el término en toda la colección (IDF), ajustado por la longitud del campo. Consultas como `WildcardQuery` o `FuzzyQuery` suelen tener un scoring más simple. Se puede obtener una explicación detallada del cálculo con `searcher.explain(query, docID)`.

Este resumen integra los conceptos clave de Lucene con ejemplos de código ilustrativos para la indexación, análisis y búsqueda.