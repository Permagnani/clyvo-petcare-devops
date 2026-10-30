🐾 Clyvo PetCare - DevOps
Descrição da Solução

O Clyvo PetCare é uma API REST desenvolvida em Java com Spring Boot para apoiar a continuidade do cuidado veterinário, permitindo o cadastro de pets e a criação de alertas preventivos vinculados a cada animal.

A solução centraliza informações dos pets e seus alertas preventivos em uma única API, facilitando o acompanhamento dos cuidados veterinários e reduzindo o risco de perda de informações importantes. A associação dos alertas aos pets permite acompanhar consultas preventivas, vacinação e outros cuidados de forma organizada.

A utilização de serviços PaaS da Microsoft Azure facilita a disponibilidade, escalabilidade e manutenção da solução sem a necessidade de administração direta da infraestrutura.

Benefícios para o Negócio
Centralização das informações dos pets.
Gestão de alertas preventivos e acompanhamento veterinário.
Redução do risco de perda de informações.
Facilidade de integração com aplicações futuras.
Disponibilidade em ambiente de nuvem.
Infraestrutura gerenciada através de serviços PaaS.
Tecnologias Utilizadas
Java 17
Spring Boot
Spring Data JPA
PostgreSQL
Maven
Swagger / SpringDoc OpenAPI
Git
GitHub
Azure DevOps
Azure App Service
Azure Database for PostgreSQL Flexible Server
Arquitetura da Solução
Plain Text
┌──────────────────┐
│ Desenvolvedor │
└─────────┬────────┘
│
▼
┌──────────────────┐
│ GitHub │
└─────────┬────────┘
│
▼
┌──────────────────┐
│ Azure DevOps │
│ CI/CD │
└─────────┬────────┘
│
▼
┌──────────────────┐
│ Azure App Service│
└─────────┬────────┘
│
▼
┌──────────────────┐
│ Azure PostgreSQL │
└─────────┬────────┘
│
▼
┌──────────────────┐
│ Usuário Final │
└──────────────────┘
Pipeline CI/CD
Fluxo CI
O desenvolvedor realiza alterações no código.
O código é enviado para o GitHub.
O Azure DevOps detecta alterações na branch principal.
O Maven executa o build da aplicação.
Os testes automatizados são executados.
O artefato .jar é gerado.
O artefato é publicado no Azure DevOps.
Fluxo CD
O artefato gerado é utilizado para deploy.
O Azure App Service recebe a nova versão da aplicação.
A aplicação é disponibilizada para os usuários finais.
Infraestrutura Azure

A solução utiliza exclusivamente serviços PaaS da Microsoft Azure.

Aplicação
Azure App Service Linux
Java 17
Banco de Dados
Azure Database for PostgreSQL Flexible Server
Variáveis de Ambiente

A conexão é realizada através das variáveis:

.properties
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD

Dados sensíveis não são armazenados no código-fonte nem enviados para o GitHub.

Banco de Dados

A aplicação utiliza duas tabelas principais:

TB_PET

Armazena os pets cadastrados.

Chave Primária

Plain Text
ID_PET
TB_ALERTA

Armazena os alertas associados aos pets.

Chave Primária

Plain Text
ID_ALERTA

Chave Estrangeira

Plain Text
ID_PET
Relacionamento
Plain Text
TB_PET 1:N TB_ALERTA

O arquivo script_bd.sql disponível na raiz do projeto contém toda a estrutura do banco de dados.

Rotas da API
Pets
Método	Endpoint	DescriçãoGET	/pets	Lista todos os pets
GET	/pets/{id}	Busca um pet por ID
POST	/pets	Cria um pet
PUT	/pets/{id}	Atualiza um pet
DELETE	/pets/{id}	Remove um pet
Alertas
Método	Endpoint	DescriçãoGET	/alertas	Lista todos os alertas
GET	/alertas/{id}	Busca um alerta por ID
GET	/alertas/pet/{petId}	Lista os alertas de um pet
POST	/alertas	Cria um alerta
PUT	/alertas/{id}	Atualiza um alerta
DELETE	/alertas/{id}	Remove um alerta
Azure DevOps
Configurações do Projeto
Version Control: Git
Work Item Process: Agile
Pipeline: YAML
Deployment Target: Azure App Service
Service Connection: azure-clyvo-sprint4
Repositórios e Acessos
GitHub
Plain Text
https://github.com/Permagnani/clyvo-petcare-devops
Azure DevOps
Plain Text
https://dev.azure.com/RM564995/clyvo-petcare-devops
Swagger
Plain Text
https://clyvo-petcare-sprint4-hcajesetgje4aza3.canadacentral-01.azurewebsites.net/swagger-ui/index.html
Build Local

Clonar o projeto:

Shell
git clone https://github.com/Permagnani/clyvo-petcare-devops.git
cd clyvo-petcare-devops

Gerar o artefato:

Shell
./mvnw clean package

ou

Shell
./mvnw clean package -DskipTests

Artefato gerado:

Plain Text
target/clyvopetcare-0.0.1-SNAPSHOT.jar
Deploy

O deploy da aplicação é realizado através do Azure DevOps utilizando pipeline CI/CD e Azure App Service.

Documentação da API

A documentação da API está disponível através do Swagger:

Plain Text
https://clyvo-petcare-sprint4-hcajesetgje4aza3.canadacentral-01.azurewebsites.net/swagger-ui/index.html
