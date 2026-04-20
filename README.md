# Projeto_II_web

Aplicação web em **Spring Boot** para gestão de uma gráfica, com áreas distintas para **cliente**, **funcionário** e **gerente**.

## Stack Tecnológica

- Java 17
- Spring Boot 3.5
- Spring MVC + Thymeleaf
- Spring Data JPA
- PostgreSQL
- Gradle

## Pré-requisitos

- Java 17 instalado
- PostgreSQL ativo localmente
- Gradle Wrapper (já incluído no projeto)

## Configuração da Base de Dados

O projeto usa a configuração em `src/main/resources/application.properties`.

1. Criar a base de dados:

```sql
CREATE DATABASE projeto2;
```

2. Ajustar credenciais no ficheiro application.properties (ou via variáveis de ambiente / perfil local).

Propriedades usadas:

- `spring.datasource.url=jdbc:postgresql://localhost:5432/projeto2`
- `spring.datasource.username=...`
- `spring.datasource.password=...`
- `spring.jpa.hibernate.ddl-auto=update`

## Como Executar

No diretório raiz do projeto:

### Windows

```bash
.\gradlew.bat bootRun
```

### Linux/Mac

```bash
./gradlew bootRun
```

A aplicação ficará disponível em:

- `http://localhost:8080`

## Perfis e Fluxos Principais

### Página inicial

- `GET /` - Home

### Cliente

- `GET /cliente/login` - login
- `GET /cliente/registar` - registo
- `GET /cliente/home` - dashboard cliente
- `GET /cliente/projetos` - gestão de projetos
- `GET /cliente/perfil` - perfil do cliente

### Funcionário

- `GET /funcionario` - login funcionário
- `GET /funcionario2/home` - dashboard funcionário
- `GET /funcionario2/stock/listar` - consulta de stock
- `GET /funcionario2/cliente/listar-projetos` - gestão de serviços/pagamentos de cliente

### Gerente

- `GET /gerente/home` - dashboard gerente
- `GET /gerente/perfil` - perfil gerente
- `GET /gerente/funcionarios/*` - gestão de funcionários
- `GET /gerente/stock/*` - gestão de stock/produtos
- `GET /gerente/fornecedor/*` - gestão de fornecedores e pagamentos
- `GET /gerente/projetos/*` - tipos de projeto/impressão
- `GET /gerente/encomenda/*` - encomendas de material

## Estrutura do Projeto

- `src/main/java/com/example/projeto2web/controllers` - controllers MVC
- `src/main/java/com/example/projeto2web/utils` - utilitários de sessão
- `src/main/resources/templates` - páginas Thymeleaf
- `src/main/resources/static` - recursos estáticos
- `libs/projeto2-0.0.1-SNAPSHOT.jar` - módulo externo com models/services/repositories

## Testes

Para correr testes:

```bash
./gradlew test
```

No Windows:

```bash
.\gradlew.bat test
```

## Notas

- O projeto depende do `jar` local em `libs/projeto2-0.0.1-SNAPSHOT.jar`.
- autenticação atual é baseada em sessão HTTP (HttpSession).
