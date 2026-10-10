# PUCaronas — Projeto RA2 (Design de Software)

Sistema JavaFX de caronas universitárias da PUCPR, evoluído para a RA2 com aplicação efetiva de padrões GoF.

## Padrões implementados
- **Strategy**: `strategy/PoliticaPreco` e implementações `PrecoFixoStrategy`, `PrecoPorKmStrategy`, `PrecoRateioStrategy`.
- **State**: `state/SolicitacaoState` e estados concreto; `SolicitacaoCarona` é o contexto.
- **Observer**: `EventoObserver`, `NotificacaoObserver` e publicação de eventos do fluxo.
- **Singleton**: instância única de `EventBus`.
- **Facade**: `facade/CaronasFacade` concentra o caso de uso solicitar → aceitar/recusar/cancelar → pagar.
- **Template Method**: `BaseCrudView.inicializarTela()` define a sequência de inicialização; cada tela concreta implementa `inicializarController()` e `criarTela()`. A inicialização ocorre no construtor da subclasse, depois de `super(...)`, evitando chamadas a métodos sobrescritos no construtor da classe-base.

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

## Conferência com o relatório RA2
Esta entrega usa a nomenclatura do relatório: `PoliticaPreco`, `SolicitacaoState`, `EventoObserver`, `EventBus`, `CaronasFacade` e `BaseCrudView`.
A implementação de `Template Method` foi ajustada para não chamar métodos sobrescrevíveis no construtor da classe-base.
O diagrama de classes ainda deve ser finalizado com base **nesta** versão do código.

## Validação antes da entrega
Execute `mvn clean test` e `mvn javafx:run` em ambiente com JDK 21 e Maven e registre os resultados. Não considere os testes aprovados sem executá-los.


## Correções RA2 (versão final)
- `maven-surefire-plugin` 3.5.2 executa os testes JUnit 5. Confirme a linha `Tests run:` e `Failures: 0` no console.
- O controller de solicitações utiliza `CaronasFacade` para criar, aceitar e recusar; aceite usa PIX como método padrão nesta interface.
- Alterações diretas do status no CRUD são bloqueadas; utilize as operações de transição do fluxo.
- A fachada valida estado, método e vagas antes de alterar a ocupação.
- Um único observador de notificações é registrado no EventBus compartilhado.
- Os testes cobrem Strategy, State, Singleton, Observer e cenários de erro do Facade.

### Executar no IntelliJ
Abra a pasta que contém `pom.xml` como projeto Maven. Configure **JDK 21**.
Em Maven > Lifecycle > **test** para testes; em Maven > Plugins > javafx > **javafx:run** para a interface.
Ou no PowerShell, na pasta do `pom.xml`:
```powershell
.\mvnw.cmd clean test
.\mvnw.cmd javafx:run
```

**Limitação:** operações CRUD legadas ainda podem acessar serviços diretamente. O fluxo principal de solicitação/aceite/recusa passa pela fachada; não alegar que todas as telas usam exclusivamente Facade. O diagrama deve representar o código entregue.
