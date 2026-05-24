# ☁️ Nahnuveem - Sistema de Gestão de Produtos & Autenticação

[![Java](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=for-the-badge&logo=spring)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-blue?style=for-the-badge&logo=springsecurity)](https://spring.io/projects/spring-security)

Uma API REST robusta e escalável desenvolvida em **Java** com o ecossistema **Spring**, projetada para a gestão eficiente de produtos e usuários. O sistema conta com uma arquitetura de segurança moderna baseada em autenticação **Stateless via tokens JWT (JSON Web Tokens)** e persistência em banco de dados relacional com paginação dinâmica de dados.

---

## 🛠️ Tecnologias e Ferramentas

* **Linguagem Principal:** Java 17+
* **Framework:** Spring Boot 3.x
* **Persistência de Dados:** Spring Data JPA
* **Segurança & Criptografia:** Spring Security & BCrypt Password Encoder
* **Autenticação:** JWT (Java-JWT por Auth0)
* **Gerenciamento de Dependências:** Maven
* **Banco de Dados:** H2 Database (Ambiente de Testes / Volátil)
* **Validações de Entrada:** Jakarta Validation (`@Valid`)

---

## 🔒 Arquitetura de Segurança e Fluxo de Autenticação

A API adota o modelo de segurança **Stateless**. Isso significa que o servidor não armazena sessões na memória; a validação da identidade de cada requisição é feita integralmente através do cabeçalho HTTP.

1. **Criptografia Base:** Nenhuma senha é salva em texto limpo. Ao cadastrar um usuário, a senha é processada pelo algoritmo de hash **BCrypt** antes de atingir o banco de dados.
2. **Emissão de Token (Login):** Ao enviar as credenciais para o endpoint `/auth/login`, o sistema valida o hash. Se correto, gera um token JWT assinado com chave HMAC256 contendo o e-mail, UUID do usuário e expiração programada para 2 horas.
3. **Filtro de Interceptação (`SecurityFilter`):** Toda requisição enviada para os endpoints protegidos (`/produtos/**`) é interceptada por um filtro customizado antes de atingir o Controller. O filtro extrai a chave do cabeçalho `Authorization: Bearer <TOKEN>`, valida sua integridade e injeta o usuário autenticado no contexto de segurança do Spring.

---

## 🛣️ Rotas da API e Estrutura de Payload

### 👥 Autenticação e Usuários (Rotas Públicas)

#### 🟩 Cadastrar Novo Usuário
* **Endpoint:** `POST /usuarios`
* **Request Body (JSON):**
  ```json
  {
    "nome": "Pedro Dev",
    "email": "pedro.dev@email.com",
    "senha": "senha_secreta_123"
  }