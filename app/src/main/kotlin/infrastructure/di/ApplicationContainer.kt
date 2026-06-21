package infrastructure.di

import application.account.AccountHandler
import application.account.usecases.MoveAccountPositionUseCase
import application.account.usecases.SaveAccountUseCase
import application.account.usecases.UpdateAccountBalanceUseCase
import application.category.CategoryHandler
import application.category.usecases.DeleteCategoryUseCase
import application.category.usecases.DeleteSubcategoryUseCase
import application.category.usecases.FetchSubcategoriesUseCase
import application.category.usecases.UpdateCategoryUseCase
import application.category.usecases.UpdateSubcategoryUseCase
import application.dashboard.DashboardHandler
import application.dashboard.usecases.BuildDashboardSummaryUseCase
import application.dashboard.usecases.FetchCategoryBreakdownUseCase
import application.dashboard.usecases.FetchCreditCardSnapshotsUseCase
import application.dashboard.usecases.FetchMonthlyFlowUseCase
import application.dashboard.usecases.FetchNetWorthDeltaUseCase
import application.dashboard.usecases.FetchSpendingPaceUseCase
import application.dashboard.usecases.FetchTopPartiesUseCase
import application.dashboard.usecases.FetchTopTransactionsUseCase
import application.dashboard.usecases.FetchUpcomingOccurrencesUseCase
import application.group.GroupHandler
import application.group.usecases.AddNewGroupUseCase
import application.group.usecases.MoveGroupPositionUseCase
import application.group.usecases.RenameGroupUseCase
import application.importStatement.ImportHandler
import application.importStatement.usecases.CheckDuplicatesUseCase
import application.importStatement.usecases.ImportTransactionsUseCase
import application.importStatement.usecases.ParseCsvFileUseCase
import application.importStatement.usecases.ParseOfxFileUseCase
import application.importStatement.usecases.ResolveOrCreateCategoryUseCase
import application.importStatement.usecases.ResolveOrCreatePartyUseCase
import application.importStatement.usecases.ResolveOrCreateTagsUseCase
import application.importStatement.usecases.ResolvePartyByNameUseCase
import application.party.PartyHandler
import application.party.usecases.AddNameUseCase
import application.party.usecases.AddPartyUseCase
import application.party.usecases.DeletePartyUseCase
import application.party.usecases.FetchNamesUseCase
import application.party.usecases.ReassignAndDeletePartyUseCase
import application.party.usecases.UpdateNameUseCase
import application.party.usecases.UpdatePartyUseCase
import application.schedule.ScheduleHandler
import application.schedule.usecases.DeleteScheduleUseCase
import application.schedule.usecases.ExportSchedulesToExcelUseCase
import application.schedule.usecases.FetchSchedulesUseCase
import application.schedule.usecases.GenerateOccurrencesUseCase
import application.schedule.usecases.MarkAsPaidUseCase
import application.schedule.usecases.SaveScheduleUseCase
import application.tag.TagHandler
import application.transaction.TransactionHandler
import application.transaction.usecases.ExportTransactionsToCsvUseCase
import application.transaction.usecases.FetchTransactionsUseCase
import application.transaction.usecases.SaveTransactionUseCase
import application.transaction.usecases.SaveTransferUseCase
import application.userPreference.UserPreferenceHandler
import domain.contracts.IAccountRepository
import domain.contracts.ICategoryRepository
import domain.contracts.IGroupRepository
import domain.contracts.IPartyRepository
import domain.contracts.IScheduleRepository
import domain.contracts.ITagRepository
import domain.contracts.ITransactionRepository
import domain.contracts.IUserPreferenceRepository
import infrastructure.repository.AccountRepository
import infrastructure.repository.CategoryRepository
import infrastructure.repository.GroupRepository
import infrastructure.repository.PartyRepository
import infrastructure.repository.ScheduleRepository
import infrastructure.repository.TagRepository
import infrastructure.repository.TransactionRepository
import infrastructure.repository.UserPreferenceRepository

object ApplicationContainer {

    private val accountRepository: IAccountRepository = AccountRepository()
    private val categoryRepository: ICategoryRepository = CategoryRepository()
    private val groupRepository: IGroupRepository = GroupRepository()
    private val partyRepository: IPartyRepository = PartyRepository()
    private val scheduleRepository: IScheduleRepository = ScheduleRepository()
    private val tagRepository: ITagRepository = TagRepository()
    private val transactionRepository: ITransactionRepository = TransactionRepository()
    private val userPreferenceRepository: IUserPreferenceRepository = UserPreferenceRepository()

    val accountHandler = AccountHandler(
        accountRepository = accountRepository,
        transactionRepository = transactionRepository,
        scheduleRepository = scheduleRepository,
        moveAccountPosition = MoveAccountPositionUseCase(accountRepository),
        updateAccountBalance = UpdateAccountBalanceUseCase(accountRepository),
        saveAccount = SaveAccountUseCase(accountRepository),
    )

    val categoryHandler = CategoryHandler(
        categoryRepository = categoryRepository,
        fetchSubcategories = FetchSubcategoriesUseCase(categoryRepository),
        updateCategory = UpdateCategoryUseCase(categoryRepository),
        updateSubcategory = UpdateSubcategoryUseCase(categoryRepository),
        deleteCategory = DeleteCategoryUseCase(categoryRepository),
        deleteSubcategory = DeleteSubcategoryUseCase(),
    )

    val groupHandler = GroupHandler(
        groupRepository = groupRepository,
        addNewGroup = AddNewGroupUseCase(groupRepository),
        moveGroupPosition = MoveGroupPositionUseCase(groupRepository),
        renameGroup = RenameGroupUseCase(groupRepository),
    )

    val partyHandler = PartyHandler(
        partyRepository = partyRepository,
        deletePartyUseCase = DeletePartyUseCase(partyRepository),
        reassignAndDeletePartyUseCase = ReassignAndDeletePartyUseCase(),
        addPartyUseCase = AddPartyUseCase(partyRepository),
        updatePartyUseCase = UpdatePartyUseCase(partyRepository),
        addNameUseCase = AddNameUseCase(partyRepository),
        updateNameUseCase = UpdateNameUseCase(partyRepository),
        fetchNamesUseCase = FetchNamesUseCase(partyRepository),
    )

    val tagHandler = TagHandler(tagRepository)

    val transactionHandler = TransactionHandler(
        transactionRepository = transactionRepository,
        saveTransactionUseCase = SaveTransactionUseCase(transactionRepository),
        saveTransferUseCase = SaveTransferUseCase(categoryRepository, partyRepository, transactionRepository, accountRepository),
        fetchTransactionsUseCase = FetchTransactionsUseCase(transactionRepository),
        exportTransactionsToCsvUseCase = ExportTransactionsToCsvUseCase(),
    )

    val scheduleHandler = ScheduleHandler(
        scheduleRepository = scheduleRepository,
        saveScheduleUseCase = SaveScheduleUseCase(scheduleRepository, transactionRepository),
        deleteScheduleUseCase = DeleteScheduleUseCase(scheduleRepository),
        generateOccurrencesUseCase = GenerateOccurrencesUseCase(),
        markAsPaidUseCase = MarkAsPaidUseCase(transactionRepository),
        exportSchedulesToExcelUseCase = ExportSchedulesToExcelUseCase(),
    )

    val userPreferenceHandler = UserPreferenceHandler(userPreferenceRepository)

    private val resolvePartyByName = ResolvePartyByNameUseCase(partyRepository)

    val importHandler = ImportHandler(
        parseOfxFile = ParseOfxFileUseCase(),
        parseCsvFile = ParseCsvFileUseCase(),
        resolveParty = resolvePartyByName,
        checkDuplicates = CheckDuplicatesUseCase(transactionRepository),
        importTransactions = ImportTransactionsUseCase(
            transactionHandler = transactionHandler,
            resolveOrCreateParty = ResolveOrCreatePartyUseCase(partyRepository, resolvePartyByName),
            resolveOrCreateCategory = ResolveOrCreateCategoryUseCase(categoryHandler),
            resolveOrCreateTags = ResolveOrCreateTagsUseCase(tagRepository),
        ),
    )

    private val fetchSchedules = FetchSchedulesUseCase(scheduleRepository)

    val dashboardHandler = DashboardHandler(
        BuildDashboardSummaryUseCase(
            transactionRepository = transactionRepository,
            netWorthUseCase = FetchNetWorthDeltaUseCase(groupRepository, transactionRepository),
            monthlyFlowUseCase = FetchMonthlyFlowUseCase(transactionRepository),
            categoryBreakdownUseCase = FetchCategoryBreakdownUseCase(transactionRepository),
            creditCardsUseCase = FetchCreditCardSnapshotsUseCase(groupRepository, transactionRepository),
            topPartiesUseCase = FetchTopPartiesUseCase(transactionRepository),
            topTransactionsUseCase = FetchTopTransactionsUseCase(transactionRepository),
            spendingPaceUseCase = FetchSpendingPaceUseCase(transactionRepository),
            upcomingOccurrencesUseCase = FetchUpcomingOccurrencesUseCase(
                fetchSchedulesUseCase = fetchSchedules,
                generateOccurrencesUseCase = GenerateOccurrencesUseCase(),
                transactionRepository = transactionRepository,
            ),
        )
    )
}
