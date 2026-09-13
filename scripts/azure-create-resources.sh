#!/bin/bash

# ============================================================
# Clyvo PetCare - Azure CLI
# App Service + PostgreSQL Flexible Server
# ============================================================

set -e

RESOURCE_GROUP="rg-clyvo-petcare"
LOCATION="brazilsouth"

DB_SERVER="clyvo-postgres-larissa-2026"
DB_NAME="clyvo_petcare"
DB_USER="clyvoadmin"

APP_NAME="clyvo-petcare-larissa-video"
PLAN_NAME="plan-clyvo-petcare-video"

echo "Digite a senha do administrador PostgreSQL:"
read -s DB_PASSWORD
echo

echo "Registrando providers necessários..."
az provider register --namespace Microsoft.DBforPostgreSQL
az provider register --namespace Microsoft.Web

echo "Criando Resource Group..."
az group create \
  --name "$RESOURCE_GROUP" \
  --location "$LOCATION"

echo "Criando PostgreSQL Flexible Server..."
az postgres flexible-server create \
  --resource-group "$RESOURCE_GROUP" \
  --name "$DB_SERVER" \
  --location "$LOCATION" \
  --admin-user "$DB_USER" \
  --admin-password "$DB_PASSWORD" \
  --sku-name Standard_B1ms \
  --tier Burstable \
  --storage-size 32 \
  --version 16 \
  --public-access 0.0.0.0 \
  --output none

echo "Criando banco de dados..."
az postgres flexible-server db create \
  --resource-group "$RESOURCE_GROUP" \
  --server-name "$DB_SERVER" \
  --name "$DB_NAME"

echo "Criando App Service Plan..."
az appservice plan create \
  --name "$PLAN_NAME" \
  --resource-group "$RESOURCE_GROUP" \
  --location "$LOCATION" \
  --is-linux \
  --sku B1

echo "Criando Web App Java 17..."
az webapp create \
  --resource-group "$RESOURCE_GROUP" \
  --plan "$PLAN_NAME" \
  --name "$APP_NAME" \
  --runtime "JAVA:17-java17"

echo "Configurando conexão com PostgreSQL..."
az webapp config appsettings set \
  --resource-group "$RESOURCE_GROUP" \
  --name "$APP_NAME" \
  --settings \
    SPRING_DATASOURCE_URL="jdbc:postgresql://$DB_SERVER.postgres.database.azure.com:5432/$DB_NAME?sslmode=require" \
    SPRING_DATASOURCE_USERNAME="$DB_USER" \
    SPRING_DATASOURCE_PASSWORD="$DB_PASSWORD" \
    --output none

echo
echo "Infraestrutura criada com sucesso."
echo "Web App: https://$APP_NAME.azurewebsites.net"
