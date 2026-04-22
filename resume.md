# Resumo das Alterações

## SearchBar.kt

**Assinatura do componente refatorada** para suportar controle externo do valor:

- Parâmetros `onTuneClicked: () -> Unit` e `onSearchClicked: () -> Unit` substituídos por `value: String` e `onValueChange: (String) -> Unit`
- `onTuneClicked` mantido como parâmetro opcional com valor padrão `{}`
- `onSearchClicked` removido completamente

**Estado interno sincronizado com valor externo:**

- Variável interna renomeada de `text` para `textFieldValue`
- Adicionado `LaunchedEffect(value)` para sincronizar o `TextFieldValue` interno quando o valor externo mudar (ex: ao limpar filtros)

**Estilo:**

- Background alterado de `MaterialTheme.colors.onPrimary` para `Color.Transparent`

**Comportamento:**

- `onValueChange` agora propaga o texto digitado para o chamador: `{ textFieldValue = it; onValueChange(it.text) }`
- Botão de lupa direito deixou de ser clicável (removidos `.pointerHoverIcon`, `.clickable` e a chamada a `onSearchClicked`)
- `keyboardActions` do `onSearch` deixou de chamar `onSearchClicked()`, agora é `{}`

---

## TransactionViewModel.kt

**Nova dependência adicionada:**

- Injetado `GroupHandler` como parâmetro com valor padrão: `private val groupHandler: GroupHandler = GroupHandler()`

**Novo estado e método:**

- `var groups = MutableStateFlow(emptyList<Group>())` — lista de grupos com suas contas
- `fun getGroups()` — carrega os grupos via `groupHandler.fetchGroups()`

**Inicialização condicional:**

- No bloco `init`, `getGroups()` é chamado apenas quando `account == null` (visão de "todas as transações")

---

## TransactionsScreen.kt

**Novos estados de filtro/busca:**

- `searchQuery: String` — texto da barra de pesquisa
- `filterAccount: BankAccount?` — filtro por conta bancária (somente na visão global)
- `showAccountDropdown: Boolean` — controla abertura do dropdown de contas
- `filterCategoryItem: Pair<Category, Subcategory?>?` — filtro por categoria e/ou subcategoria
- `showCategoryDropdown: Boolean` — controla abertura do dropdown de categorias

**Reset de filtros ao mudar de conta:**

- No `LaunchedEffect(account)`, `searchQuery`, `filterAccount` e `filterCategoryItem` são resetados para os valores iniciais

**Header — barra de ferramentas:**

- `Row` do cabeçalho recebeu `verticalAlignment = Alignment.CenterVertically`
- O `Row` interno do breadcrumb foi ajustado de `Modifier.fillMaxWidth()` para `Modifier` sem largura fixa
- A seção que antes tinha um comentário `// TODO: Implements Searchbar` foi substituída por uma `Row` completa com filtros e barra de pesquisa, visível apenas quando `showTransactionsList == true`

**Filtro por conta (somente quando `account == null`):**

- Dropdown estilizado manualmente (sem `DropdownButton` genérico) com ícone `Bank`, label dinâmico e `CaretDown`
- Lista as contas de todos os grupos via `viewModel.groups.value.flatMap { it.accounts }`
- Opção "Todos os bancos" reseta o filtro

**Filtro por categoria:**

- Calcula `preFilteredTransactions` (busca + conta) para determinar quais categorias/subcategorias estão disponíveis
- Dropdown estilizado com ícone `Shapes`, label dinâmico e `CaretDown`
- Exibe categorias e suas subcategorias recuadas com `padding(start = 16.dp)`
- Opção "Todas as categorias" reseta o filtro

**SearchBar integrada:**

- `SearchBar(value = searchQuery, onValueChange = { searchQuery = it })` agora conectada ao estado da tela

**Lista de transações (`displayedTransactions`):**

- Antes usava diretamente `viewModel.transactions.value`; agora aplica os três filtros combinados: busca por texto, conta e categoria/subcategoria
- Campos pesquisados: `party.name`, `description`, `category.name`, `subcategory?.name`, `tags[*].name`
- Os totais mensais positivo e negativo no `TotalFooter` passaram a usar `displayedTransactions` em vez da lista completa

**Imports:**

- Adicionados: `DropdownMenu`, `DropdownMenuItem`, `Bank`, `CaretDown`, `Shapes`, `Category`, `Subcategory`
- Import individual de `view.shared.*` substituído por wildcard `view.shared.*`
- `AddressView`, `ClickableIcon`, `TextH2`, `TextNormal` removidos como imports individuais (cobertos pelo wildcard)

---

## Alterações adicionais — TransactionsScreen.kt (Filter Bar)

**Novos filtros adicionados:**

- `filterType: TransactionType?` — filtro por tipo de transação (Receitas / Despesas)
- `showTypeDropdown: Boolean` — controla abertura do dropdown de tipo
- `filterTag: TagEntity?` — filtro por tag
- `showTagDropdown: Boolean` — controla abertura do dropdown de tags

**Reset de filtros ampliado:**

- `filterTag` e `filterType` também resetados no `LaunchedEffect(account)`

**Header reestruturado:**

- `Row` único substituído por `Column`, separando o breadcrumb (endereço) da linha de filtros
- `padding(20.dp)` alterado para `padding(start = 20.dp, top = 20.dp, end = 20.dp)` — remove espaço vazio abaixo dos filtros

**Ordem dos filtros na toolbar:**

1. Contas (somente quando `account == null`)
2. Tipo
3. Categoria
4. Tags
5. SearchBar

**Alinhamento da linha de filtros:**

- `verticalAlignment` alterado para `Alignment.Bottom` para alinhar os componentes à base da row

**Filtro por tipo:**

- Dropdown com ícone `ArrowsDownUp`, opções: "Todos os tipos", "Receitas", "Despesas"
- Influencia quais categorias ficam disponíveis no dropdown de categorias (`availableCategoriesMap` filtrado por `filterType`)

**Filtro por tags (dinâmico):**

- Dropdown com ícone `Tag`
- `availableTags` calculado a partir das transações que já passaram pelos filtros de busca, conta, tipo e categoria — a lista se atualiza conforme os outros filtros são aplicados

**`displayedTransactions` atualizado:**

- Adicionados `matchesTag` e `matchesType` à composição do filtro final

**Novos imports:**

- `ArrowsDownUp`, `Tag` (phosphor), `core.entity.Tag as TagEntity`

---

## Text.kt

**`TextMedium` — parâmetro `fontSize` adicionado:**

- Novo parâmetro opcional `fontSize: TextUnit = 14.sp`
- `fontSize` e `lineHeight` agora usam o valor do parâmetro em vez de hardcoded `14.sp`
- Nos 4 dropdowns da toolbar de transações, `TextMedium` é chamado com `fontSize = 12.sp` (redução de 2sp)

---

## IconPaths.kt

**Lista `bankLogos` atualizada:**

- Adicionados: `itau.svg`, `c6bank.svg`

**Novos arquivos de ícone adicionados:**

- `assets/icons/bankLogos/itau.svg`
- `assets/icons/bankLogos/c6bank.svg`
- `assets/icons/bankLogos/santander.svg` (atualizado)

---

## Persistência de preferências do usuário

**Novo arquivo de migration:**

- `V4__create_user_preferences.sql` — cria a tabela `tbl_user_preferences` com uma linha fixa (ID=1) e coluna `is_light_theme`; insere o registro padrão com tema claro

**Novos arquivos:**

- `core/entity/UserPreference.kt` — entidade JPA mapeada para `tbl_user_preferences`
- `core/contracts/IUserPreferenceDao.kt` — interface com `get()` e `update()`
- `infra/dao/UserPreferenceDao.kt` — implementação com Hibernate; usa `session.get` por ID fixo 1
- `domain/userPreference/usecases/FetchUserPreferenceUseCase.kt`
- `domain/userPreference/usecases/UpdateUserPreferenceUseCase.kt`
- `domain/userPreference/UserPreferenceHandler.kt` — orquestra os use cases; expõe `fetchPreferences()` e `updateTheme(Boolean)`
- `viewModel/SettingsViewModel.kt` — persiste a mudança de tema no DB e atualiza `UserPreferences.isLightTheme`

**Arquivos modificados:**

- `infra/config/HibernateUtil.kt` — registra `UserPreference::class.java` na configuração do Hibernate
- `view/modules/UserPreferences.kt` — adicionada função `loadFromDatabase()` que carrega o tema salvo ao iniciar
- `Main.kt` — chama `UserPreferences.loadFromDatabase()` após `DatabaseConfig.runMigrations()`
- `view/modules/settings/SettingsScreen.kt` — recebe `SettingsViewModel` como parâmetro; troca de `isLightTheme = !isLightTheme` para `viewModel.setTheme(!isLightTheme)` para persistir a preferência

---

## Dashboard (feature nova)

**Structs de domínio (`core/structs/`):**

- `DashboardPeriod.kt` — sealed class com 5 períodos (`ThisMonth`, `LastMonth`, `Last3Months`, `ThisYear`, `Last3Years`); cada um expõe `label` em pt-BR e `range(today)` devolvendo `Pair<LocalDateTime, LocalDateTime>`
- `DashboardSummary.kt` — agrega todos os dados da tela: `NetWorthSnapshot`, `CashFlow`, `SpendingPace?`, `List<CreditCardSnapshot>`, `List<CategoryBreakdown>`, `List<MonthlyFlow>`, `List<PartyVolume>`, top `Transaction`s e `savingsRatePercent`

**Camada de domínio (`domain/dashboard/`):**

- `DashboardHandler.kt` — orquestra o `BuildDashboardSummaryUseCase`
- `usecases/BuildDashboardSummaryUseCase.kt` — compõe o `DashboardSummary` chamando os 7 use cases; `spendingPace` só é calculado quando `period == ThisMonth`; granularidade do `monthlyEvolution` vira `YEAR` quando `period == Last3Years`, caso contrário `MONTH`
- `usecases/FetchNetWorthDeltaUseCase.kt` — soma saldo de todas as contas via `GroupDao`, calcula `monthDelta` a partir das transações do mês corrente e devolve `NetWorthSnapshot(total, deltaAmount, deltaPercent)`
- `usecases/FetchMonthlyFlowUseCase.kt` — dois modos de agrupamento: `byMonth` (labels pt-BR abreviados via `Locale.of("pt","BR")`, limite `maxBuckets = 60`) e `byYear` (labels = ano, um bucket por ano do range)
- `usecases/FetchCategoryBreakdownUseCase.kt` — agrupa transações por categoria (default `EXPENSE`), ordena desc por valor, e quando ultrapassa o limite (`6`) consolida o excedente em uma categoria sintética "Outros" (id `-1L`, icon `_default.svg`)
- `usecases/FetchCreditCardSnapshotsUseCase.kt` — para cada `CreditCardAccount`, calcula o ciclo atual a partir de `closingDay`, soma a fatura corrente, calcula `availableLimit` e `nextDueDate`/`daysToDue`; usa `safeDate` para tratar meses com menos dias
- `usecases/FetchSpendingPaceUseCase.kt` — compara gasto do mês corrente até o dia de hoje contra a média dos mesmos dias dos últimos 3 meses (`baselineMonths = 3`); retorna `null` se não há histórico
- `usecases/FetchTopPartiesUseCase.kt` — top 5 beneficiários por volume (default `EXPENSE`)
- `usecases/FetchTopTransactionsUseCase.kt` — top 5 transações por valor absoluto (default `EXPENSE`)

**Infraestrutura (`infra/dao/TransactionDao.kt`):**

- Novo método `getByDateRange(from: LocalDateTime, to: LocalDateTime, type: TransactionType? = null)` — criteria `between` em `date`, filtro opcional por `type`, ordenação desc; inicializa `party`, `account`, `category`, `subcategory`, `tags` eagerly

**ViewModel (`viewModel/DashboardViewModel.kt`):**

- `MutableStateFlow`s: `summary: DashboardSummary?` (null = loading inicial), `period: DashboardPeriod` (default `ThisMonth`), `isLoading: Boolean`
- `selectPeriod(newPeriod)` atualiza o flow e chama `reload()`
- `reload()` atualmente **síncrono** — chama `dashboardHandler.buildSummary(period.value)` na thread chamadora. Uma tentativa de tornar assíncrono foi feita e revertida (ver memória do projeto)
- `init { reload() }` para carregar o estado inicial

**UI (`view/modules/dashboard/`):**

- `DashboardScreen.kt` — reestruturada: header com breadcrumb + `PeriodSelector` à direita; body em 4 linhas (Patrimônio + Fluxo + Ritmo / Cartões / Categorias + Evolução / Top beneficiários + Top despesas); fallback `"Carregando..."` quando `summary == null`
- `component/SummaryCard.kt` — card-base reutilizado por todos os widgets (RoundedCornerShape 12dp, border 0.5dp, `defaultMinSize`, ícone + título em `TextH2`)
- `component/PeriodSelector.kt` — botão arredondado com ícone `CalendarBlank` e `CaretDown` + `DropdownMenu` com os 5 períodos
- `component/NetWorthCard.kt` — exibe total do patrimônio e delta mensal (valor + percentual)
- `component/CashFlowCard.kt` — receitas/despesas/saldo líquido + taxa de poupança
- `component/SpendingPaceCard.kt` — só aparece no período `ThisMonth`; mostra gasto-até-hoje vs média dos 3 meses anteriores
- `component/CreditCardsCard.kt` — lista cartões com fatura corrente, limite disponível e `daysToDue`
- `component/CategoryBreakdownCard.kt` — breakdown por categoria com percentuais
- `component/MonthlyEvolutionCard.kt` — gráfico custom em `Canvas`: barras duplas (receita vs despesa) + linha de saldo líquido sobreposta; grid de 5 linhas horizontais; legenda com 3 dots coloridos
- `component/TopPartiesCard.kt` — top 5 beneficiários
- `component/TopTransactionsCard.kt` — top 5 transações por valor

**Bug conhecido (pendente):** período "Últimos 3 anos" às vezes não atualiza os cards ao ser selecionado; navegar para outra tela e voltar + clicar de novo resolve. Uma tentativa de fix (reload assíncrono com coroutines) foi implementada e revertida porque não resolveu o sintoma — causa raiz ainda não identificada.

---

## Ajustes pós-Dashboard

**`HibernateUtil.kt`:**

- `hibernate.show_sql` e `hibernate.format_sql` alterados de `"true"` para `"false"` — silencia os logs SQL no console

**`DashboardScreen.kt`:**

- Assinatura simplificada: parâmetro `viewModel: DashboardViewModel = DashboardViewModel()` removido; instância criada internamente com `val viewModel = remember { DashboardViewModel() }` para evitar recomposição desnecessária
- Lógica do `SpendingPaceCard` simplificada: o `if/else` que renderizava um `Spacer` quando `pace == null` foi substituído por chamada direta `SpendingPaceCard(pace = current.spendingPace)`, passando nullable ao componente

**`SpendingPaceCard.kt`:**

- Parâmetro `pace` alterado de `SpendingPace` para `SpendingPace?`
- Adicionado estado vazio (`pace == null`): exibe ícone `Gauge` opaco centralizado e texto `"Disponível após 3 meses de registros"`, com `return@SummaryCard` para encerrar o conteúdo
