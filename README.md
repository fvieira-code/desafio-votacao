# 📦 Desafio Votação – Backend API

Este projeto implementa o **Desafio Votação**, uma API REST para gerenciamento de pautas, sessões de votação e votos em assembleias, com foco na **comunicação entre backend e aplicativo mobile por meio de mensagens JSON**, conforme especificado no **Anexo 1 do desafio**.

A aplicação foi desenvolvida com **Java 17 e Spring Boot 3**, utilizando autenticação **JWT**, persistência em **MySQL**, versionamento de banco com **Flyway** e exposição de documentação via **Swagger/OpenAPI**.

---

## 🧩 Visão Geral

No cooperativismo, cada associado possui direito a um voto, e as decisões são tomadas por meio de assembleias.  
Este sistema permite:

- Cadastro de pautas.
- Abertura de sessões de votação com tempo configurável (padrão: 60 segundos).
- Registro de votos (`SIM` / `NÃO`), garantindo **1 voto por associado por pauta**.
- Contabilização e consulta de resultados.
- Exposição de **telas dinâmicas (FORMULARIO / SELECAO)** para consumo por aplicativo mobile.
- Autenticação via JWT (segurança abstraível, conforme o desafio).

> **Nota:** O cliente mobile **não faz parte do escopo**, sendo o backend responsável apenas por **descrever as telas e ações**.

---

## 🚀 Tecnologias Utilizadas

* **Java 17** & **Spring Boot 3.x**
* **Spring Data JPA** & **Hibernate**
* **MySQL 8+**
* **Lombok** (Produtividade)
* **Flyway** (Migrations)
* **Swagger / OpenAPI** (Documentação)
* **JWT** (Segurança)
* **Apache POI** (Suporte futuro a Excel)
* **Docker & Docker Compose**

---

## ⚙️ Configuração e Execução

### 1️⃣ Pré-requisitos
* Java 17+
* Maven 3.8+
* Docker (opcional)

### 2️⃣ Clonar o Repositório
   ```bash
      git clone https://github.com/dbserver/desafio-votacao.git
   ```

### 3️⃣ Banco de Dados
Você pode optar pelo MySQL local ou via Docker:

   ```sql
   CREATE DATABASE db-desafio-votacao CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
   ```sql
   docker exec -it desafio-votacao-mysql-1 mysql -uroot -p -e "CREATE DATABASE \`db-desafio-votacao\` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
   ```

### 4️⃣ Configurar application.yml
Ajuste as credenciais em src/main/resources/application.yml:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/db-desafio-votacao
    username: root
    password: sua_senha
```

### 5️⃣ Executar a Aplicação

Via Docker: 
```bash 
  docker compose up --build -d 
```
Via Maven:
```bash 
  mvn spring-boot:run 
```

## 🔐 Autenticação


1. Usuário padrão cadastrado pelo Flyway:
    - **Usuário:** `admin@dbserver.com.br`
    - **Senha:** `admin`

2. Obtenha o token JWT:
   ### Gerar Token:
    - `POST /api/v1/auth/signin`
   ```json
   {
    "email": "admin@dbserver.com.br",
    "password": "admin" 
   }
   ```
   Retorno:
   ```json
   { "token": "eyJhbGciOi..." }
   ```  

## 🧪 Documentação da API

A documentação interativa da API está disponível por meio do **Swagger UI**:

- **Ambiente local:**  
  http://localhost:8080/swagger-ui/index.html

- **Ambiente Docker:**  
  http://localhost:8081/swagger-ui/index.html


## 📬 Principais Endpoints (Domínio)

Todos os endpoints do domínio exigem **autenticação via JWT**.  
O token deve ser informado no header HTTP `Authorization`, conforme o padrão:
    -   `Authorization: Bearer <token>`
---

### Endpoints Disponíveis

| Ação                | Método | Endpoint                                  | Autenticação |
|---------------------|--------|-------------------------------------------|--------------|
| Criar pauta         | POST   | `/api/v1/pautas`                          | Obrigatória  |
| Abrir sessão        | POST   | `/api/v1/pautas/{pautaId}/sessoes`        | Obrigatória  |
| Registrar voto      | POST   | `/api/v1/pautas/{pautaId}/votos`          | Obrigatória  |
| Consultar resultado | GET    | `/api/v1/pautas/{pautaId}/resultado`      | Obrigatória  |

---

### Observações

- O controle de acesso é realizado por meio de **JWT (JSON Web Token)**.
- Requisições sem o token ou com token inválido retornam **HTTP 401 (Unauthorized)**.
- Regras de negócio adicionais, como **unicidade de voto por associado**, são validadas no backend.

## 📱 Mobile UI (Anexo 1)

Conforme definido no **Anexo 1**, o backend é responsável por **descrever a interface de usuário**, enquanto o aplicativo mobile realiza apenas a renderização das telas e a execução das ações.

---

### Tipos de Tela

- **FORMULARIO**  
  Tela composta por uma coleção de campos e botões de ação.  
  Ao acionar um botão, o aplicativo executa automaticamente uma requisição **POST** para a URL configurada, enviando os dados preenchidos no corpo da requisição.

- **SELECAO**  
  Tela que apresenta uma lista de opções, onde cada item representa uma ação específica a ser executada por meio de uma requisição **POST**.

---

### Endpoints Mobile UI


| Tela                | Tipo de Tela | Método | Endpoint                                   |
|---------------------|--------------|--------|--------------------------------------------|
| Home                | SELECAO      | GET    | `/api/v1/mobile/home`                      |
| Criar pauta         | FORMULARIO   | GET    | `/api/v1/mobile/pautas/nova`               |
| Abrir sessão        | FORMULARIO   | GET    | `/api/v1/mobile/sessoes/abrir`             |
| Registrar voto      | FORMULARIO   | GET    | `/api/v1/mobile/votos/registrar`           |
| Consultar resultado | FORMULARIO   | GET    | `/api/v1/mobile/resultados/consultar`      |

> **Observação**  
> Embora as telas sejam obtidas via **GET**, as ações definidas nelas são executadas pelo aplicativo mobile por meio de requisições **POST**, conforme especificação do Anexo 1.

---

## 📂 Estrutura de Pacotes
```
br.com.dbserver.desafiovotacao
├── config        → Swagger, segurança, JWT
├── controller    → Controllers REST e Mobile UI
├── dto
│   ├── request
│   └── response
│       └── mobileui
├── exception     → Tratamento global (ProblemDetail)
├── mapper        → Conversão DTO ↔ Entidade
├── model         → Entidades JPA
├── repository    → Spring Data JPA
├── service
│   └── impl
└── DesafioVotacaoApplication.java
       
```
---
## 📄 Licença

    Distribuído para fins comerciais. Company: Fernando Vieira.