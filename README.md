
# 📦 slicing-mgmt-system

Sistema para controlar a quantidade de **clientes** em cada serviço de **Slice**, com autenticação JWT,
executar a viabilidade técnica para a alocação de usuários em um network slicing. 
**Desenvolvido com Java 17 e Spring Boot 3.**

---

## 🚀 Tecnologias

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- MySQL 8+
- Lombok
- Flyway
- Swagger (springdoc-openapi)
- Apache POI (exportação Excel)
- JWT (autenticação)
- Docker (build da aplicação)
- Docker Compose (somente app)

---

## 📁 Requisitos

- IntelliJ IDEA (ou outro IDE)
- MySQL 8+ (externo)
- Java 17
- Maven 3.8+
- Docker (opcional)

---

## ⚙️ Subindo o projeto no IntelliJ

1. Clone o projeto:
   ```bash
   git clone https://dev.azure.com/zukk-tecnologia/Slicing/_git/slicing-mgmt-system
   ```

2. Crie o banco de dados manualmente no MySQL:
   ```sql
   CREATE DATABASE db-slicing-mgmt-system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
   ```sql
   docker exec -it slicing-mgmt-system-mysql-1 mysql -uroot -p -e "CREATE DATABASE \`db-slicing-mgmt-system\` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
   ```
   
3. Configure seu `application.yml` com usuário e senha do banco:

   ```yaml
   port = localhost: 3306 | docker: 3307
   
   spring:
     datasource:
       url: jdbc:mysql://localhost:[port]/db-slicing-mgmt-system
       username: root
       password: sua_senha
   ```

4. Rode o projeto:
   - Cria (se não existirem) e inicia os containers: 
   ```bash
     docker compose up -d 
   ```
   - Pela IDE (classe `SlicingApplication`)
   - Ou pelo terminal:
     ```bash
     # Com Docker:
     ./docker compose up -d
     ```
     ```bash
     # Sem Docker:
     ./mvn spring-boot:run 
      ```

5. O Flyway criará automaticamente as tabelas:
    - `tb_user`
    - `tb_consultor`
    - `tb_cliente`
    - `tb_ponto`

---

## 🔐 Autenticação

1. Usuário padrão cadastrado pelo Flyway:
    - **Usuário:** `admin@zukk.com.br`
    - **Senha:** `admin`

2. Obtenha o token JWT:
   # Cadastro:
    - `POST /api/v1/auth/signup`
   ```json
   {
    "firstName": "User",
    "lastName": "User",
    "email": "user@zukk.com.br",
    "password": "user" 
   }
   ```
   Retorno:
   ```json
   { "token": "eyJhbGciOi..." }
   ```

   # Gerar Token:
    - `POST /api/v1/auth/signin`
   ```json
   {
    "email": "admin@zukk.com.br",
    "password": "admin" 
   }
   ```
      ```json
   {
    "email": "user@zukk.com.br",
    "password": "user" 
   }
   ```
   Retorno:
   ```json
   { "token": "eyJhbGciOi..." }
   ```  

   # Validar o Token:
    - `GET /api/v1/resource`
   ```header:
         Authorization: Bearer [token gerado no enpoint /api/v1/auth/signin
   ```

3. Use o token em chamadas protegidas:
    - Header:  
      `Authorization: Bearer eyJhbGciOi...`

---

## 🧪 Testando no Swagger

1. Acesse: [http://localhost:[8080-8081]/swagger-ui.html](http://localhost:8080/swagger-ui.html)
2. Clique em `Authorize` e cole o token JWT.
3. Teste os endpoints:
    - `/api/v1/consultores`
    - `/api/v1/clientes`
    - `/api/v1/pontos`
    - `/api/v1/pontos/filtro`
    - `/api/v1/pontos/filtro/excel`

---

## 📬 Testando no Postman

1. Requisição de login:

   **POST** `http://localhost:[8080-8081]/api/v1/auth/signup`
    - cURL:
      curl --location 'http://localhost:[8080-8081]/api/v1/auth/signup' \
      --header 'Content-Type: application/json' \
      --header 'Cookie: JSESSIONID=3D837A1374CB577B65FC623AB4B25030' \
      --data-raw '{
      "firstName": "Admin",
      "lastName": "Admin",
      "email": "admin@zukk.com.br",
      "password": "admin"   
      }'

2. Requisição de Token:

   **POST** `http://localhost:[8080-8081]/api/v1/auth/signin`
    - cURL:
      curl --location 'http://localhost:[8080-8081]/api/v1/auth/signin' \
      --header 'Content-Type: application/json' \
      --header 'Cookie: JSESSIONID=3D837A1374CB577B65FC623AB4B25030' \
      --data-raw '{
      "email": "admin@zukk.com.br",
      "password": "admin"   
      }'

3. Listar todos os users ou por nome :
   **GET**
    - http://localhost:[8080-8081]/api/v1/auth/users
    - http://localhost:[8080-8081]/api/v1/auth/users?nome=admin

4. Alterar o user:
   **PUT**
    - cURL:
      curl --location --request PUT 'http://localhost:[8080-8081]/api/v1/auth/update' \
      --header 'Content-Type: application/json' \
      --data-raw '    {
      "id": 1,
      "firstName": "Admin",
      "lastName": "Administrator",
      "email": "admin@zukk.com.br",
      "password": "$2a$10$J7riw3vmIJDqPzVylot0Nepz2xajhd.yxWgKRnEHklmYVKWgl.xey",
      "role": "USER"
      }'
5. Use o token retornado em outras requisições com Header:

   ```
   Authorization: Bearer <token>
   ```

6. Exemplo de filtro de pontos:

   **GET** `http://localhost:[8080-8081]/api/v1/pontos/filtro?dataInicial=2025-07-01&dataFinal=2025-07-31`

---

## 🐳 Docker (apenas app, banco é externo)

### Build e execução

```bash
docker-compose up --build -d
```
- Acesse [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
- O app se conecta ao banco interno do Docker na port 3307

---

## 📂 Estrutura de pacotes

```
br.com.zukk.vivo.projectslicing
├── config           → configurações Swagger, segurança
├── controller       → REST controllers
├── dto              → Data Transfer Objects
|+++++ request
|+++++ response
├── exception        → tratadores globais
├── mapper           → conversores entre DTO e entidade
├── model            → entidades JPA
├── repository       → repositórios Spring Data
├── service          → regras de negócio
|+++++ impl
└── SlicingApplication.java
```

---

## 📑 Endpoints principais

### 🔐 Autenticação
- `POST api/v1/auth/signup`
- `POST api/v1/auth/signin`
- `GET api/v1/auth/users`
- `PUT api/v1/auth/update`
- `GET api/v1/auth/users/{id}`
- `GET api/v1/auth/inToken`
- `GET api/v1/auth/inDetails`
- `POST /api/v1/auth/refresh-token`

### 👤 Consultores
- `GET /api/v1/consultores`
- `POST /api/v1/consultores`
- `PUT /api/v1/consultores/{id}`
- `DELETE /api/v1/consultores/{id}`
- `GET /api/v1/consultores/cpf/{cpf}`
- `GET /api/v1/token/expiration`
  curl --location --request POST 'http://localhost:[8080-8081]/api/v1/auth/refresh-token' \
  --header 'Authorization: Bearer SEU_TOKEN_JWT_AQUI'
  🔁 Substitua SEU_TOKEN_JWT_AQUI pelo seu token válido de autenticação.

### 🏢 Clientes
- `GET /api/v1/clientes`
- `POST /api/v1/clientes`
- `GET /api/v1/clientes/cnpj/{cnpj}`

### ⏱ Pontos
- `GET /api/v1/pontos`
- `POST /api/v1/pontos`
- `GET /api/v1/pontos`
- `GET /api/v1/pontos/pagina`
- `GET /api/v1/pontos/filtro?dataInicial=...&dataFinal=...`
- `GET /api/v1/pontos/filtro/excel` → gera planilha `.xlsx`
- `GET /api/v1/pontos/gerar/excel` → gera planilha `.xlsx`

---

## 📄 Licença

Distribuído para fins comerciais. Company: ZUKK SERVIÇOS EM TECNOLOGIA LTDA.