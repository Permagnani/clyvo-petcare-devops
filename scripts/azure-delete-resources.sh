#!/bin/bash

# ============================================================
# Clyvo PetCare - Azure CLI
# Remove todos os recursos criados para a entrega
# ============================================================

RESOURCE_GROUP="rg-clyvo-petcare"

echo "Removendo Resource Group: $RESOURCE_GROUP"
echo "Todos os recursos associados serao excluidos."

az group delete \
  --name "$RESOURCE_GROUP" \
  --yes \
  --no-wait

echo "Solicitacao de remocao enviada."
echo "Verifique no Portal Azure se o Resource Group foi removido."
