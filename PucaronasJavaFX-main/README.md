# PUCaronas — Projeto RA2 (Design de Software)

Sistema JavaFX de caronas universitárias da PUCPR, evoluído para a RA2 com aplicação efetiva de padrões GoF.

## Padrões implementados
- **Strategy**: `strategy/PoliticaPreco` e implementações `PrecoFixoStrategy`, `PrecoPorKmStrategy`, `PrecoRateioStrategy`.
- **State**: `state/SolicitacaoState` e estados concreto; `SolicitacaoCarona` é o contexto.
- **Observer**: `EventoObserver`, `NotificacaoObserver` e publicação de eventos do fluxo.
- **Singleton**: instância única de `EventBus`.
- **Facade**: `facade/CaronasFacade` concentra o caso de uso solicitar → aceitar/recusar/cancelar → pagar.
- **Template Method (estrutura herdada)**: `BaseCrudView` define a sequência de inicialização e delega passos às telas concretas.

## Requisitos
- JDK 21
- Maven 3.9+ (ou Maven Wrapper incluído)
- Internet na primeira execução para baixar JavaFX/JUnit, se as dependências ainda não estiverem no cache local.

## Executar
Windows (PowerShell/CMD):
```text
mvnw.cmd clean javafx:run
```
Linux/macOS:
```text
./mvnw clean javafx:run
```
Alternativamente, com Maven instalado:
```text
mvn clean javafx:run
```

## Testes
```text
mvn test
```
Os testes de padrões estão em `src/test/java/com/example/caronas/PatternTests.java`.

## Persistência
Os dados são serializados localmente na pasta `data/`, criada automaticamente pelos repositórios.

## Observação sobre a evolução RA2
O projeto-base já possuía entidades, repositórios, serviços, controllers e telas CRUD. A evolução RA2 adiciona regras de fluxo e padrões de projeto em classes próprias, reduzindo condicionais e acoplamento no caso de uso principal.
