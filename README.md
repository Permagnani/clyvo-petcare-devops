# Clyvo PetCare - DevOps

API REST desenvolvida em Java com Spring Boot para apoiar a continuidade do cuidado veterinário, permitindo o cadastro de pets e a criação de alertas preventivos vinculados a cada animal.

## Opção de Deploy Escolhida

Este projeto utiliza exclusivamente:

**Azure App Service + PostgreSQL Flexible Server (PaaS)**

A aplicação e o banco de dados não utilizam containers.

- Aplicação: Azure App Service Linux com Java 17
- Banco de dados: Azure Database for PostgreSQL Flexible Server
- Código-fonte: GitHub
- Provisionamento: Azure CLI
- Persistência: PostgreSQL em serviço PaaS

## Arquitetura da Solução

O usuário acessa a API publicada no Azure App Service.

O App Service executa a aplicação Java Spring Boot e se conecta ao PostgreSQL Flexible Server por meio das seguintes variáveis de ambiente:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

A senha do banco não é armazenada no código-fonte nem enviada ao GitHub.

## Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- Swagger / SpringDoc OpenAPI
- Microsoft Azure
- Azure App Service
- Azure Database for PostgreSQL Flexible Server
- Azure CLI
- Git
- GitHub

## Banco de Dados

A aplicação utiliza duas tabelas principais relacionadas.

### TB_PET

Armazena os pets cadastrados.

- Chave primária: `ID_PET`

### TB_ALERTA

Armazena os alertas associados aos pets.

- Chave primária: `ID_ALERTA`
- Chave estrangeira: `ID_PET`

Relacionamento:

`TB_PET 1:N TB_ALERTA`

O arquivo `script_bd.sql`, disponível na raiz do projeto, contém o DDL necessário para criação das tabelas, chaves, relacionamento e comentários do banco de dados.

## Rotas da API

### Pets

| Método | Rota | Descrição |
|---|---|---|
| GET | `/pets` | Lista todos os pets |
| GET | `/pets/{id}` | Busca um pet por ID |
| POST | `/pets` | Cria um pet |
| PUT | `/pets/{id}` | Atualiza um pet |
| DELETE | `/pets/{id}` | Remove um pet |

### Alertas

| Método | Rota | Descrição |
|---|---|---|
| GET | `/alertas` | Lista todos os alertas |
| GET | `/alertas/{id}` | Busca um alerta por ID |
| GET | `/alertas/pet/{petId}` | Lista os alertas de um pet |
| POST | `/alertas` | Cria um alerta |
| PUT | `/alertas/{id}` | Atualiza um alerta |
| DELETE | `/alertas/{id}` | Remove um alerta |

# Implantação na Azure

Todos os comandos abaixo devem ser executados no Azure Cloud Shell em modo Bash.

## 1. Clonar o repositório

```bash
git clone https://github.com/Permagnani/clyvo-petcare-devops.git
cd clyvo-petcare-devops
```

## 2. Criar a infraestrutura

Dar permissão ao script:

```bash
chmod +x scripts/azure-create-resources.sh
```

Executar:

```bash
./scripts/azure-create-resources.sh
```

O script cria:

- Resource Group `rg-clyvo-petcare`
- PostgreSQL Flexible Server
- banco de dados `clyvo_petcare`
- App Service Plan Linux B1
- Azure Web App com Java 17
- variáveis de ambiente para conexão da aplicação com o PostgreSQL

A senha do PostgreSQL é solicitada durante a execução e não fica armazenada no repositório.

## 3. Criar as tabelas do banco

Conectar ao PostgreSQL:

```bash
psql "host=clyvo-postgres.postgres.database.azure.com port=5432 dbname=clyvo_petcare user=clyvoadmin sslmode=require"
```

Após informar a senha, executar o arquivo `script_bd.sql`.

Também é possível executar diretamente:

```bash
psql "host=clyvo-postgres.postgres.database.azure.com port=5432 dbname=clyvo_petcare user=clyvoadmin sslmode=require" -f script_bd.sql
```

## 4. Gerar o arquivo JAR

```bash
chmod +x mvnw
./mvnw clean package -DskipTests
```

Arquivo gerado:

```text
target/clyvopetcare-0.0.1-SNAPSHOT.jar
```

## 5. Publicar no Azure App Service

```bash
az webapp deploy \
  --resource-group rg-clyvo-petcare \
  --name clyvo-petcare-larissa \
  --src-path target/clyvopetcare-0.0.1-SNAPSHOT.jar \
  --type jar
```

## 6. Testar a aplicação publicada

URL pública:

```text
https://clyvo-petcare-larissa.azurewebsites.net
```

Listar pets:

```bash
curl https://clyvo-petcare-larissa.azurewebsites.net/pets
```

Listar alertas:

```bash
curl https://clyvo-petcare-larissa.azurewebsites.net/alertas
```

Swagger:

```text
https://clyvo-petcare-larissa.azurewebsites.net/swagger-ui.html
```

# Demonstração de CRUD

A demonstração deve ser feita usando a aplicação publicada na Azure e, após cada operação, deve ser realizada uma consulta diretamente no PostgreSQL para comprovar a persistência.

## CRUD - PET

### INSERT

```bash
curl -X POST https://clyvo-petcare-larissa.azurewebsites.net/pets \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Thor",
    "especie": "Cachorro",
    "raca": "Pastor Alemão",
    "peso": 32.0,
    "dataNascimento": "2021-03-12",
    "nomeResponsavel": "Carlos Lima",
    "observacoes": "Pet criado para demonstração de CRUD"
  }'
```

Comprovar no banco:

```sql
SELECT * FROM TB_PET ORDER BY ID_PET;
```

### UPDATE

```bash
curl -X PUT https://clyvo-petcare-larissa.azurewebsites.net/pets/3 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Thor",
    "especie": "Cachorro",
    "raca": "Pastor Alemão",
    "peso": 34.5,
    "dataNascimento": "2021-03-12",
    "nomeResponsavel": "Carlos Lima",
    "observacoes": "Pet atualizado durante a demonstração de CRUD"
  }'
```

Comprovar no banco:

```sql
SELECT ID_PET, NM_PET, NR_PESO, DS_OBSERVACOES
FROM TB_PET
WHERE ID_PET = 3;
```

### DELETE

```bash
curl -X DELETE https://clyvo-petcare-larissa.azurewebsites.net/pets/3
```

Comprovar no banco:

```sql
SELECT * FROM TB_PET WHERE ID_PET = 3;
```

Resultado esperado:

```text
(0 rows)
```

## CRUD - ALERTA

### INSERT

```bash
curl -X POST https://clyvo-petcare-larissa.azurewebsites.net/alertas \
  -H "Content-Type: application/json" \
  -d '{
    "petId": 1,
    "tipo": "CHECKUP",
    "descricao": "Alerta criado para demonstração de CRUD",
    "dataAlerta": "2026-09-25",
    "status": "PENDENTE"
  }'
```

Comprovar no banco:

```sql
SELECT * FROM TB_ALERTA ORDER BY ID_ALERTA;
```

### UPDATE

```bash
curl -X PUT https://clyvo-petcare-larissa.azurewebsites.net/alertas/3 \
  -H "Content-Type: application/json" \
  -d '{
    "petId": 1,
    "tipo": "CHECKUP",
    "descricao": "Alerta atualizado durante a demonstração de CRUD",
    "dataAlerta": "2026-09-27",
    "status": "CONCLUIDO"
  }'
```

Comprovar no banco:

```sql
SELECT ID_ALERTA, TP_ALERTA, DS_ALERTA, DT_ALERTA, ST_ALERTA, ID_PET
FROM TB_ALERTA
WHERE ID_ALERTA = 3;
```

### DELETE

```bash
curl -X DELETE https://clyvo-petcare-larissa.azurewebsites.net/alertas/3
```

Comprovar no banco:

```sql
SELECT * FROM TB_ALERTA WHERE ID_ALERTA = 3;
```

Resultado esperado:

```text
(0 rows)
```
