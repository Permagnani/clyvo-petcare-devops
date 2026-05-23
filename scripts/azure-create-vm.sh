#!/bin/bash

# ============================================================
# Clyvo PetCare - Azure CLI
# Cria infraestrutura em nuvem, instala Docker/Git e executa a aplicação
# ============================================================

RESOURCE_GROUP="rg-clyvo-petcare-devops"
LOCATION="canadacentral"
VM_NAME="vm-clyvo-petcare"
ADMIN_USER="gu564995"
VM_SIZE="Standard_B2ats_v2"
IMAGE="Ubuntu2404"
REPO_URL="https://github.com/Permagnani/clyvo-petcare-devops.git"
APP_DIR="clyvo-petcare-devops"

echo "Criando Resource Group..."
az group create \
  --name "$RESOURCE_GROUP" \
  --location "$LOCATION"

echo "Criando VM Linux Ubuntu..."
az vm create \
  --resource-group "$RESOURCE_GROUP" \
  --name "$VM_NAME" \
  --image "$IMAGE" \
  --size "$VM_SIZE" \
  --admin-username "$ADMIN_USER" \
  --generate-ssh-keys \
  --public-ip-sku Standard

echo "Abrindo porta 8080 para acesso externo da API..."
az vm open-port \
  --resource-group "$RESOURCE_GROUP" \
  --name "$VM_NAME" \
  --port 8080 \
  --priority 1001

echo "Instalando Docker, Docker Compose e Git na VM..."
az vm run-command invoke \
  --resource-group "$RESOURCE_GROUP" \
  --name "$VM_NAME" \
  --command-id RunShellScript \
  --scripts "
    sudo apt-get update -y
    sudo apt-get install -y ca-certificates curl gnupg git

    sudo install -m 0755 -d /etc/apt/keyrings

    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | \
    sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

    sudo chmod a+r /etc/apt/keyrings/docker.gpg

    echo \"deb [arch=\$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \$(. /etc/os-release && echo \$VERSION_CODENAME) stable\" | \
    sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

    sudo apt-get update -y
    sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

    sudo systemctl enable docker
    sudo systemctl start docker

    docker --version
    docker compose version
    git --version
  "

echo "Clonando repositorio e subindo containers Docker..."
az vm run-command invoke \
  --resource-group "$RESOURCE_GROUP" \
  --name "$VM_NAME" \
  --command-id RunShellScript \
  --scripts "
    cd /home/$ADMIN_USER
    rm -rf $APP_DIR
    git clone $REPO_URL
    cd $APP_DIR
    sudo docker compose up -d --build
    sudo docker ps
  "

PUBLIC_IP=$(az vm show \
  --resource-group "$RESOURCE_GROUP" \
  --name "$VM_NAME" \
  --show-details \
  --query publicIps \
  --output tsv)

echo "============================================================"
echo "Deploy finalizado com sucesso."
echo "Swagger: http://$PUBLIC_IP:8080/swagger-ui.html"
echo "============================================================"