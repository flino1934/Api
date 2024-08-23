# API DS-Catalog

Esta API é responsável pela gestão de processos, incluindo operações CRUD de clientes, produtos e categorias. A aplicação utiliza JPA para mapeamento de dados e o banco de dados H2 para armazenamento.

## Tecnologias Utilizadas

- Java
- Spring Boot
- Maven
- JPA
- JUnit 5

## Pré-requisitos

Para rodar a aplicação, você precisará ter instalado:

- [Java 11](https://www.oracle.com/java/technologies/downloads/#java11)
- [Maven](https://maven.apache.org/download.cgi)
- H2 (acessível via [H2 Database Console](http://localhost:8080/h2-console/login.jsp))
## Instalação

1. Clone o repositório:

    ```bash
    git clone
    ```

2. Navegue até o diretório do projeto:

    ```bash
    cd Api
    ```

3. Compile o projeto e baixe as dependências:

   Com Maven:

    ```bash
    mvn clean install
    ```

## Executando a Aplicação

Para rodar a aplicação localmente, use o seguinte comando:

Com Maven:

```bash
mvn spring-boot:run
```

## Acessando o H2 Console

Após iniciar a aplicação, você pode acessar o console do H2 para visualizar o banco de dados em memória
via o seguinte URL: http://localhost:8080/h2-console.

As credenciais padrão (caso não tenham sido alteradas) são:

JDBC URL: jdbc:h2:mem
User: sa
Password:


**Certifique-se de que o console do H2 está habilitado nas configurações de seu `application.properties`

## Executando Testes

Para rodar os testes automatizados com JUnit 5, use o seguinte comando:

```bash
mvn test
```

### 3. **Endpoints da API**
- Lista com os principais endpoints da API.

```markdown
Aqui estão os principais endpoints disponíveis na API:

Clientes

GET /api/clients: Lista todos os clientes
POST /api/clients: Cria um novo cliente
GET /api/clients/{id}: Busca um cliente por ID
PUT /api/clients/{id}: Atualiza um cliente existente
DELETE /api/clients/{id}: Remove um cliente
Produtos

GET /api/products: Lista todos os produtos
POST /api/products: Cria um novo produto
GET /api/products/{id}: Busca um produto por ID
PUT /api/products/{id}: Atualiza um produto existente
DELETE /api/products/{id}: Remove um produto
Categorias

GET /api/categories: Lista todas as categorias
POST /api/categories: Cria uma nova categoria
GET /api/categories/{id}: Busca uma categoria por ID
PUT /api/categories/{id}: Atualiza uma categoria existente
DELETE /api/categories/{id}: Remove uma categoria
