# Rusbéckend

Backend but com rusbé.

> Não tem muita coisa pra ver por aqui ainda, mas se você quiser dar uma olhada no código,
> fique a vontade.


# Guia de desenvolvimento

## Requisitos
- Kubectl CLI
- k3d instalado
- Babashka

## Comando mágico

Inicializa o cluster com todos os recrusos e dependências para executar a aplicação localmente

```shell
bb init-resources
```

## Outros comandos

### `bb apply-manifests`

Aplica todos os manifestos ao kubernetes com o comando `kubectl apply -f <file>`

### `bb shutdown-resources`

Delete o cluster kubernetes iniciado pelo k3d

### `bb get-node-ip`

Obtém o IP de um dos nós para que você possa conectar aos PODs via `NodeIP`