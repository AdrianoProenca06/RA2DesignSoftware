# Pucaronas — Sistema de Caronas Universitárias (JavaFX)

Sistema desktop (JavaFX) para organizar caronas entre estudantes: oferta de vagas, solicitação,
aceite/recusa/cancelamento, cálculo de preço, pagamento, notificações e histórico.

Projeto da disciplina **Design de Software** (PUCPR) — Avaliação Somativa **RA2** (padrões de projeto GoF).

## Padrões GoF implementados

| Padrão | Onde | Para quê |
|---|---|---|
| **Strategy** | `strategy/` | Políticas de preço da carona (fixo, por km, rateio de custo) |
| **State** | `state/` + `model/SolicitacaoCarona` | Ciclo de vida da solicitação (pendente → aceita/recusada/cancelada) |
| **Observer** | `event/` | Notificações, histórico e auditoria reagem a eventos do domínio |
| **Singleton** | `event/BarramentoEventos` | Barramento de eventos único e compartilhado |
| **Facade** | `facade/CaronasFacade` | Casos de uso do fluxo da carona em um único ponto de entrada |
| **Template Method** | `repository/BaseRepository` (e `view/BaseCrudView`) | Esqueleto da persistência; subclasses só fornecem `idDe()` e ganchos |

Diagramas de classes: `docs/diagramas/` (completo: `00_completo.png`; um por padrão: `01` a `06`; fontes `.puml`).

## Requisitos

* **JDK 21**
* Maven (já incluso via wrapper: `mvnw` / `mvnw.cmd`) — baixa o JavaFX 21 automaticamente

## Como executar

```bash
# Linux / macOS
./mvnw clean javafx:run

# Windows
mvnw.cmd clean javafx:run
```

No IntelliJ: abrir o projeto como Maven e executar a classe `com.example.pucaronasjavafx.Launcher`
(ou `com.example.caronas.Main`, com as opções de VM do JavaFX configuradas).

Os dados são salvos em arquivos `.dat` na pasta `data/` (criada automaticamente na primeira execução,
ignorada pelo git). Para recomeçar do zero, apague a pasta `data/`.

## Como executar os testes

```bash
./mvnw test
```

34 testes JUnit 5 cobrem cada padrão isoladamente e o fluxo completo pelo Facade
(`src/test/java/com/example/caronas`). Os testes usam uma pasta temporária
(propriedade `caronas.data.dir`) e **não** tocam na pasta `data/` real.

## Roteiro rápido de demonstração

1. Abra o app e clique em **▶ Fluxo da Carona (demonstração dos padrões)**.
2. Clique em **Carregar dados de demonstração** (cria motorista, 3 passageiros e 3 caronas: uma por política de preço).
3. Escolha um passageiro e uma carona (o preço estimado aparece, conforme a política — *Strategy*) e clique em **1. Solicitar**.
4. Selecione a solicitação na tabela e clique em **2. Aceitar (motorista)**: o estado muda (*State*), a vaga é ocupada,
   o pagamento pendente é gerado com o valor da política e o passageiro é notificado (*Observer*).
5. Tente **Recusar** a mesma solicitação já aceita: a transição é rejeitada (*State*).
6. Clique em **3. Pagar**: o pagamento é concluído, a corrida entra no histórico e o motorista é notificado.
7. Com a política **RATEIO**, aceite um segundo passageiro e veja o valor cair (o custo é dividido entre mais ocupantes).

## Observações

* Caronas gravadas **antes** desta versão não têm distância/valor base/política. Edite-as na tela
  *Gerenciar Caronas* para preencher esses campos (ou apague `data/`).
* Senhas de usuário são armazenadas em texto puro (herança do projeto-base; fora do escopo do RA2).

## Estrutura

```
src/main/java/com/example/caronas
├── model/        entidades (Carona, SolicitacaoCarona, Pagamento, ...)
├── repository/   persistência por serialização (BaseRepository = Template Method)
├── service/      regras de cada entidade
├── facade/       CaronasFacade (Facade)
├── state/        estados da solicitação (State)
├── strategy/     políticas de preço (Strategy)
├── event/        barramento e observadores (Singleton + Observer)
├── controller/   adaptadores entre telas e serviços/facade
├── view/         telas JavaFX (BaseCrudView = Template Method)
└── util/         DialogUtil, IdGenerator, DadosDemonstracao
docs/             diagramas de classes e evidências
```
