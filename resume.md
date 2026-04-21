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
