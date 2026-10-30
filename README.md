# 🐾 Clyvo PetCare - DevOps

## Descrição da Solução

O **Clyvo PetCare** é uma API REST desenvolvida em Java com Spring Boot para apoiar a continuidade do cuidado veterinário, permitindo o cadastro de pets e a criação de alertas preventivos vinculados a cada animal.

A solução centraliza informações dos pets e seus alertas preventivos em uma única API, facilitando o acompanhamento dos cuidados veterinários e reduzindo o risco de perda de informações importantes. A associação dos alertas aos pets permite acompanhar consultas preventivas, vacinação e outros cuidados de forma organizada.

A utilização de serviços PaaS da Microsoft Azure facilita a disponibilidade, escalabilidade e manutenção da solução sem a necessidade de administração direta da infraestrutura.

---

## Benefícios para o Negócio

- Centralização das informações dos pets.
- Gestão de alertas preventivos e acompanhamento veterinário.
- Redução do risco de perda de informações.
- Facilidade de integração com aplicações futuras.
- Disponibilidade em ambiente de nuvem.
- Infraestrutura gerenciada através de serviços PaaS.

---

## Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- Swagger / SpringDoc OpenAPI
- Git
- GitHub
- Azure DevOps
- Azure App Service
- Azure Database for PostgreSQL Flexible Server

---

## Arquitetura da Solução

```text
[Desenvolvedor]
       |
       v
[GitHub]
       |
       v
[Azure DevOps]
 (CI/CD)
       |
       v
[Azure App Service]
       |
       v
[Azure PostgreSQL]

       ^
       |
[Usuário Final]
```

---

## Pipeline CI/CD

### Fluxo CI

1. O desenvolvedor realiza alterações no código.
2. O código é enviado para o GitHub.
3. O Azure DevOps detecta alterações na branch principal.
4. O Maven executa o build da aplicação.
5. Os testes automatizados são executados.
6. O artefato `.jar` é gerado.
7. O artefato é publicado no Azure DevOps.

### Fluxo CD

8. O artefato gerado é utilizado para deploy.
9. O Azure App Service recebe a nova versão da aplicação.
10. A aplicação é disponibilizada para os usuários finais.

---

## Infraestrutura Azure

A solução utiliza exclusivamente serviços PaaS da Microsoft Azure.

### Aplicação

- Azure App Service Linux
- Java 17

### Banco de Dados

- Azure Database for PostgreSQL Flexible Server

### Variáveis de Ambiente

A conexão é realizada através das variáveis:

```properties
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
```
