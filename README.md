# Argos Auth System (V1)

![Java](https://img.shields.io/badge/java-000000.svg?style=for-the-badge&logo=openjdk&logoColor=orange)
![JDBC](https://img.shields.io/badge/Jdbc-000000?style=for-the-badge&logo=openjdk&logoColor=orange)
![SpringBoot](https://img.shields.io/badge/springboot-000000?style=for-the-badge&logo=springboot&logoColor=green)
![SpringSecurity](https://img.shields.io/badge/Spring%20Security-000000?style=for-the-badge&logo=spring-security&logoColor=green)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=spring-security&logoColor=green)
![JUnit](https://img.shields.io/badge/junit-000000?style=for-the-badge&logoColor=green)
![Mockito](https://img.shields.io/badge/mockito-000000?style=for-the-badge&logoColor=green)
![TestContainers](https://img.shields.io/badge/testcontainers-000000?style=for-the-badge&logo=docker&logoColor=blue)
![Docker](https://img.shields.io/badge/docker-000000?style=for-the-badge&logo=docker&logoColor=blue)
![MySQL](https://img.shields.io/badge/MySQL-000000.svg?style=for-the-badge&logo=MySQL&logoColor=blue)
![repo size](https://img.shields.io/github/repo-size/GuinhoFSilva/argos?style=for-the-badge&color=000000&labelColor=000000) 


> O **Argos Auth (V1)** é o microsserviço de autenticação e gerenciamento de identidade do ecossistema Olympus.
> Nesta primeira versão, oferece cadastro de jogadores, autenticação baseada em JWT e consulta do perfil autenticado.

## Features

- Cadastro de jogadores
- Login com JWT
- Recuperação do perfil autenticado
- Senhas protegidas com BCrypt
- Validação de credenciais
- Tratamento Centralizado de Exceções

## Arquitetura

- Clean Architecture
- Domain-Driven Design (DDD)
- Dependency Injection
- JWT Authentication
- BCrypt Password Hashing
- JDBC Persistence
- REST API

## Tecnologias

- Java 21
- Spring Boot
- Spring Security
- JDBC
- MySQL
- JUnit 5
- Mockito
- TestContainers
- JWT
- Docker

## Testes

O projeto possui:

- Testes unitários do domínio
- Testes unitários dos casos de uso
- Testes de integração utilizando:
  - Testcontainers
  - MySQL
  - MockMvc
- Teste dos Fluxos HTTP completos
 
---

### Endpoints
|Método |Endpoint |Auth |Descrição
|--------|----------|-----------|-----------|
| POST   | /v1/auth/register |  ❌ | Cadastro
| POST   | /v1/auth/login    |  ❌ | Login
| GET    | /v1/players/{id} |  ✅ | Retornar perfil

---

## Entidade

### Player

| Campo | Tipo |
|--------|------|
| id | UUID |
| nickname | String |
| email | String |
| passwordHash | String |
| createdAt | DateTime |
| updatedAt | DateTime |

---

# Casos de Uso
> Nota: Esses são os casos de uso referentes à versão 1 do projeto.
## Register Player

### Objetivo

Criar uma nova conta de jogador.

### Entrada

- Nickname
- Email
- Senha

### Fluxo

1. Receber os dados enviados pelo usuário.
2. Validar o formato do email e da senha.
3. Verificar se email e nickname são únicos.
4. Gerar o hash da senha.
5. Persistir o jogador.
6. Retornar confirmação do cadastro.

### Saída

- DTO de resposta do Player.

### Possíveis Erros

- Email já cadastrado.
- Nickname já cadastrado.
- Email inválido.
- Senha inválida.

### Regras de Negócio

- Nickname é obrigatório.
- Nickname deve ser único.
- Nickname deve ser armazenado em lowercase.
- Máximo de 20 caracteres.

- Email é obrigatório.
- Email deve ser único.
- Email deve ser armazenado em lowercase.
- Máximo de 255 caracteres.
- Deve possuir um formato válido.

- Senha é obrigatória.
- Máximo de 128 caracteres.
- Nunca pode ser armazenada em texto puro.
- Deve conter:
  - no mínimo 8 caracteres;
  - uma letra maiúscula;
  - uma letra minúscula;
  - um número;
  - um caractere especial.

---

## Login Player

### Objetivo

Autenticar um jogador.

### Entrada

- Email
- Senha

### Fluxo

1. Validar os dados recebidos.
2. Buscar o jogador pelo email.
3. Validar a senha.
4. Gerar um Access Token JWT.
5. Retornar o token.

### Saída

- Access Token JWT.

### Possíveis Erros

- Credenciais inválidas.

### Regras de Negócio

- Email é obrigatório.
- Senha é obrigatória.
- O email deve existir no sistema.
- As credenciais devem corresponder a um jogador válido.

---

## Get Profile

### Objetivo

Retornar os dados do jogador autenticado.

### Entrada

- Access Token JWT

### Fluxo

1. Validar o JWT.
2. Identificar o jogador.
3. Buscar suas informações.
4. Retornar os dados.

### Saída

- id
- nickname
- email
- createdAt
- updatedAt

### Possíveis Erros

- JWT inválido.
- JWT expirado.
- Jogador não encontrado.

### Regras de Negócio

- O JWT é obrigatório.
- O token deve estar válido e dentro do prazo de expiração.
- Apenas o próprio jogador autenticado pode acessar seus dados.

---

# Roadmap V2
- Adicionar campo role (player e admin)✅
- Implementar autorização com base nas roles✅
- Validar acesso aos endpoints protegidos✅
- Adicionar campo Rank
- Implementar refresh token
- Centralizar autenticação no Spring Security 
