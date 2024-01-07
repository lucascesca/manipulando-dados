# Manipulação de Dados
Neste projeto apliquei o meu aprendizado de manipulação e acesso a dados por meio da JDBC API. 

Essa API permite a codificação do acesso a dados de forma única e simples, pois não é necessária a implementação de diferentes códigos para diferentes Sistemas de Gerenciamento de Banco de Dados (SGBD).
Isso é possível por meio da instalação de um executável `.jar` referente ao SGBD escolhido. 
O driver é responsável por fazer a conversão dos comandos passados em Java para a linguagem SQL e passá-los para o SGBD.

## Classes e Intefaces utilizadas

### `java.util.Propeties`
Classe de chave-valor que representa um conjunto persistente de propriedades, podendo ser armazenada ou carregada de uma stream. Suas propriedades são de tipo String.

Sua utilização neste projeto é para o armazenamento das informações contidas no arquivo `db.properties`, possibilitando que estas sejam passadas para a classe DriverManager e que uma conexão com o SGBD seja estabelecida. 
As porpriedads do aqurivo são lidas pelo objeto FileInputStream e o seu aramazenamento é feito pelo método `.load(FileInputStreamObject)`.

### `java.sql.Connection`
Interface encarregada de estabelecer uma sessão de conexão com o SGBD.

As declarações SQL são executadas e seus resultados retornados dentro de uma conexão.
Ela permite operações e o fechamneto da conexão aberta por meio da declaração `DriverManager.getConnection(url, properties)`.

#### Métodos:
- `prepareStatement()`: utilizado para gerar um objeto `PreparedStatement`.
- `.setAutoCommit()`: dita se as declarações SQL terão auto-commit. Recebe `false` como argumento para desabilitar o auto-commit e ajudar a criar lógica para garantir as propriedades ACID de uma transação.
- `.commit()`: utilizado para fazer o commit das declarações SQL. Implementado após todas as declarações, garantindo que o commit só será realizado se nenhum erro acontecer.
- `.rollback()`: retorna o banco de dados ao estado original, caso alguma operação da transação apresente algum erro.
- `.close()`: realiza o encerramento da conexão, evitando um possível vazamento de memória.

### `java.sql.DriverManager`
Classe responsável por gerenciar drivers da API.

Utilizada com o método `.getConnection(url, properties)` para criar a conexão com o SGBD. Tanto o url quanto as propriedades são lidos de uma variável do tipo `Properties`.

### `java.sql.PreparedStatement`
Objeto que representa uma declaração SQL pré-compilada.

Essa interface armazena uma declaração SQL, permitindo o reuso desta múltiplas vezes.

## Observações
Este projeto foi utilizado unicamente para o estudo de acesso a dados e manipulação destes.

Algumas possíveis exceções relacionadas a outros temas não foram tratadas.

Quaisquer sugestões de melhorias ou críticas construtivas são bem-vindas.
