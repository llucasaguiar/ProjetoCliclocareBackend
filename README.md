# CicloCare API - Backend Spring Boot

## Descrição

Backend completo em Spring Boot para o sistema de acompanhamento do ciclo menstrual feminino - **CicloCare**.

## 🚀 Tecnologias

- **Java 17+**
- **Spring Boot 3.3.0**
- **Spring Security + JWT**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **Lombok**
- **BCrypt** para criptografia de senha

## 📁 Estrutura do Projeto

```
src/main/java/com/ciclocare/
├── entity/              # Entidades JPA
├── dto/
│   ├── request/        # DTOs para requisições
│   └── response/       # DTOs para respostas
├── repository/         # Interfaces do Spring Data JPA
├── service/            # Lógica de negócio
├── controller/         # Endpoints REST
├── security/           # Configuração de segurança
├── config/             # Configurações da aplicação
├── exception/          # Tratamento de erros
└── CicloCareApplication.java  # Main Application
```

## 🔧 Configuração

### 1. **Pré-requisitos**
- JDK 17+
- Maven 3.8+
- PostgreSQL 12+

### 2. **Variáveis de Ambiente**

Modifique `src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/ciclocare_db
spring.datasource.username=postgres
spring.datasource.password=sua_senha

# JWT
jwt.secret.key=sua_chave_secreta_muito_longa_minimo_256_bits
jwt.expiration=86400000        # 24 horas
jwt.refresh.expiration=604800000  # 7 dias

# CORS
cors.allowed-origins=http://localhost:3000,http://localhost:5173
```

### 3. **Banco de Dados**

Crie o banco de dados:

```sql
CREATE DATABASE ciclocare_db;
```

As tabelas serão criadas automaticamente pelo Hibernate (ddl-auto=update)

### 4. **Build e Execução**

```bash
# Build
mvn clean install

# Executar
mvn spring-boot:run

# Ou
java -jar target/ciclocare-api-1.0.0.jar
```

## 📡 Endpoints Disponíveis

### Autenticação

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "usuario@email.com",
  "senha": "senha123"
}

Response:
{
  "sucesso": true,
  "mensagem": "Login realizado com sucesso",
  "dados": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "usuario": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "nome": "Maria Silva",
      "email": "usuario@email.com",
      "dataCriacao": "2026-05-13T10:00:00",
      "ultimaAtualizacao": "2026-05-13T10:00:00",
      "ativo": true
    }
  }
}
```

#### Registro
```http
POST /api/auth/register
Content-Type: application/json

{
  "nome": "Maria Silva",
  "email": "maria@email.com",
  "senha": "senha123"
}
```

### Usuário

#### Buscar por ID
```http
GET /api/usuarios/{id}
Authorization: Bearer {token}
```

#### Meu Perfil
```http
GET /api/usuarios/perfil/meu
Authorization: Bearer {token}
```

#### Atualizar Perfil
```http
PUT /api/usuarios/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "Maria Silva Atualizado"
}
```

#### Deletar Conta
```http
DELETE /api/usuarios/{id}
Authorization: Bearer {token}
```

### Ciclo Menstrual

#### Criar Ciclo
```http
POST /api/ciclos
Authorization: Bearer {token}
Content-Type: application/json

{
  "dataInicio": "2026-05-01",
  "dataFim": "2026-05-06",
  "duracaoCiclo": 28,
  "duracaoMenstruacao": 5,
  "ultimaMenstruacao": "2026-05-01",
  "intensidadeFluxo": "Moderado"
}
```

#### Buscar Último Ciclo
```http
GET /api/ciclos/ultimo
Authorization: Bearer {token}
```

### Humor Diário

#### Registrar Humor
```http
POST /api/humores
Authorization: Bearer {token}
Content-Type: application/json

{
  "data": "2026-05-13",
  "humor": "feliz",
  "notas": "Dia muito bom!"
}
```

#### Histórico de Humor (últimos 30 dias)
```http
GET /api/humores/historico?dias=30
Authorization: Bearer {token}
```

### Energia Diária

#### Registrar Energia
```http
POST /api/energias
Authorization: Bearer {token}
Content-Type: application/json

{
  "data": "2026-05-13",
  "nivelEnergia": 7,
  "notas": "Energia normal"
}
```

#### Histórico de Energia (últimos 30 dias)
```http
GET /api/energias/historico?dias=30
Authorization: Bearer {token}
```

## 🔐 Autenticação

Todos os endpoints (exceto `/auth/login` e `/auth/register`) requerem um token JWT no header:

```http
Authorization: Bearer {seu_token_jwt}
```

O token deve ser enviado ao fazer login e pode ser renovado usando:

```http
POST /api/auth/refresh
Authorization: Bearer {refresh_token}
```

## 🛡️ Segurança

- ✅ **Senhas criptografadas** com BCrypt
- ✅ **JWT** para autenticação stateless
- ✅ **CORS** configurado para aceitar apenas origens especificadas
- ✅ **Validações** de entrada em todas as requisições
- ✅ **Exception Handler** global para erros padronizados

## 📝 Exemplos com cURL

### Registrar
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Silva",
    "email": "maria@email.com",
    "senha": "senha123"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "maria@email.com",
    "senha": "senha123"
  }'
```

### Registrar Ciclo
```bash
curl -X POST http://localhost:8080/api/ciclos \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "dataInicio": "2026-05-01",
    "dataFim": "2026-05-06",
    "duracaoCiclo": 28,
    "duracaoMenstruacao": 5,
    "ultimaMenstruacao": "2026-05-01",
    "intensidadeFluxo": "Moderado"
  }'
```

## 📚 Integração com Frontend

O frontend pode fazer requisições usando `fetch()` ou `axios`:

```javascript
// Login
fetch('http://localhost:8080/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    email: 'usuario@email.com',
    senha: 'senha123'
  })
})
.then(res => res.json())
.then(data => {
  localStorage.setItem('token', data.dados.token);
  localStorage.setItem('usuario', JSON.stringify(data.dados.usuario));
});

// Requisições autenticadas
fetch('http://localhost:8080/api/ciclos', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + localStorage.getItem('token')
  },
  body: JSON.stringify({...})
});
```

## 🐛 Troubleshooting

### Conexão com banco recusada
- Verifique se PostgreSQL está rodando
- Confirme as credenciais em `application.properties`
- Verifique se o database existe

### Erro 401 Unauthorized
- Token expirado: use `/api/auth/refresh`
- Token inválido: faça login novamente

### Erro de CORS
- Verifique se a origem está em `cors.allowed-origins`
- Confirme que o header `Authorization` está em `cors.allowed-headers`

## 📦 Dependências Principais

| Dependência | Versão | Propósito |
|-------------|--------|----------|
| spring-boot-starter-web | 3.3.0 | API REST |
| spring-boot-starter-security | 3.3.0 | Autenticação |
| spring-boot-starter-data-jpa | 3.3.0 | Banco de dados |
| jjwt | 0.12.3 | JWT |
| postgresql | 42.7.2 | Driver DB |
| lombok | Latest | Redução de boilerplate |

## 📄 Licença

MIT License

## 🤝 Suporte

Em caso de dúvidas ou problemas, abra uma issue no repositório.
