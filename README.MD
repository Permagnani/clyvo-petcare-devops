# Clyvo PetCare - DevOps

API REST desenvolvida em Java Spring Boot para apoiar a continuidade do cuidado veterinário, permitindo o cadastro de pets e a criação de alertas preventivos.

## Arquitetura Macro da Solução

A solução foi implantada em uma máquina virtual Linux na Microsoft Azure. O acesso externo ocorre pelo IP público da VM na porta 8080, permitindo acesso à API via Swagger.

```mermaid
flowchart TD
    A[Usuário] --> B[Navegador Web]
    B --> C[IP Público da VM Azure :8080]
    C --> D[VM Linux Ubuntu - vm-clyvo-petcare]
    D --> E[Docker Compose]
    E --> F[Container API Spring Boot<br/>clyvo-petcare-api]
    F --> G[Container Banco H2<br/>clyvo-h2-db]
    G --> H[Volume Docker Nomeado<br/>clyvo_h2_data]

    I[GitHub<br/>clyvo-petcare-devops] --> D
```

## Link da aplicação em nuvem

```text
http://20.104.20.59:8080/swagger-ui.html
```

## Tecnologias

- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database
- Swagger / SpringDoc OpenAPI
- Docker
- Docker Compose
- Microsoft Azure
- GitHub

## Execução com Docker Compose

```bash
docker compose up -d --build
```

## Verificar containers

```bash
docker ps
```

## Repositório

```text
https://github.com/Permagnani/clyvo-petcare-devops
```